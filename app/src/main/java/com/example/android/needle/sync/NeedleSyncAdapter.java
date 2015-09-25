package com.example.android.needle.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.example.android.needle.CloudEndPointBuilderHelper;
import com.example.android.needle.R;
import com.example.android.needle.backend.needle.Needle;
import com.example.android.needle.backend.needle.model.Advertisement;
import com.example.android.needle.backend.needle.model.AdvertisementCollection;
import com.example.android.needle.data.NeedleContract;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonfisk on 23/09/15.
 */
public class NeedleSyncAdapter extends AbstractThreadedSyncAdapter{

    public final String LOG_TAG = NeedleSyncAdapter.class.getSimpleName();


    private static final String COUNTRY_CODE_BUNDLE_KEY = "countryCode";
    private static final String ZIP_CODE_BUNDLE_KEY = "zipCode";

    // Interval at which to sync with the weather, in milliseconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;


    public NeedleSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "onPerformSync Called.");

        String zipCode = extras.getString(ZIP_CODE_BUNDLE_KEY);
        String countryCode = extras.getString(COUNTRY_CODE_BUNDLE_KEY);

        Needle needleApi = CloudEndPointBuilderHelper.getEndpoints();
        AdvertisementCollection adCollection = null;
        List<ContentValues> cVVector = new ArrayList<>();

        try {
            provider.delete(NeedleContract.AdvertisementEntry.CONTENT_URI, null, null);
            adCollection = needleApi.ads().getNeighborhoodAds(countryCode, zipCode).execute();

            for(Advertisement ad: adCollection.getItems()) {
                ContentValues adValues = new ContentValues();
                adValues.put(NeedleContract.AdvertisementEntry.COLUMN_USER_EMAIL,
                        ad.getUserEmail());
                adValues.put(NeedleContract.AdvertisementEntry.COLUMN_DATE,
                        ad.getAdvertisementDate().getValue());
                adValues.put(NeedleContract.AdvertisementEntry.COLUMN_COUNTRY_CODE,
                        ad.getCountryCode());
                adValues.put(NeedleContract.AdvertisementEntry.COLUMN_ZIP_CODE,
                        ad.getZipCode());
                adValues.put(NeedleContract.AdvertisementEntry.COLUMN_DESC,
                        ad.getDescription());
                adValues.put(NeedleContract.AdvertisementEntry.COLUMN_NAME,
                        ad.getName());
                if(ad.getPhoneNumber() != null) {
                    adValues.put(NeedleContract.AdvertisementEntry.COLUMN_PHONE_NUMBER,
                            ad.getPhoneNumber());
                }

                cVVector.add(adValues);
            }

            if(cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                provider.bulkInsert(NeedleContract.AdvertisementEntry.CONTENT_URI, cvArray);
            }
        } catch(IOException e) {
            Log.d(LOG_TAG, e.getMessage());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Log.d(LOG_TAG, "Needle Service Complete. " + cVVector.size() + " Inserted");


    }


    public static void syncImmediately(Context context, String countryCode, String zipCode) {
        Bundle bundle = new Bundle();

        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);

        bundle.putString(COUNTRY_CODE_BUNDLE_KEY, countryCode);
        bundle.putString(ZIP_CODE_BUNDLE_KEY, zipCode);


        ContentResolver.requestSync(getSyncAccount(context, countryCode, zipCode),
                context.getString(R.string.content_authority), bundle);
    }

    private static Account getSyncAccount(Context context, String countryCode, String zipCode) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            onAccountCreated(newAccount, context, countryCode, zipCode);
        }
        return newAccount;

    }

    private static void onAccountCreated(Account newAccount, Context context,
                                         String countryCode, String zipCode) {
        /*
         * Since we've created an account
         */
        NeedleSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME,
                countryCode, zipCode);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context, countryCode, zipCode);
    }

    public static void initializeSyncAdapter(Context context, String countryCode, String zipCode) {
        getSyncAccount(context, countryCode, zipCode);
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime,
                                             String countryCode, String zipCode) {
        Account account = getSyncAccount(context, countryCode, zipCode);
        Bundle bundle = new Bundle();
        bundle.putString(COUNTRY_CODE_BUNDLE_KEY, countryCode);
        bundle.putString(ZIP_CODE_BUNDLE_KEY, zipCode);

        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(bundle).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, bundle, syncInterval);
        }
    }
}