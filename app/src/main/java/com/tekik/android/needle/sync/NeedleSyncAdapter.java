package com.tekik.android.needle.sync;

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

import com.tekik.android.needle.CloudEndPointBuilderHelper;
import com.tekik.android.needle.R;
import com.tekik.android.needle.Utility;
import com.tekik.android.needle.backend.needle.Needle;
import com.tekik.android.needle.backend.needle.model.Advertisement;
import com.tekik.android.needle.backend.needle.model.AdvertisementCollection;
import com.tekik.android.needle.data.NeedleContract;
import com.google.api.client.util.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonfisk on 23/09/15.
 */
public class NeedleSyncAdapter extends AbstractThreadedSyncAdapter{



    // Interval at which to sync with the app, in milliseconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 360;
    public static final long HOUR_IN_MILLIS = 60 * 60 * 1000;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;


    public NeedleSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    // This function is where backend database and SQLite database are connected.
    // Announcements are fetched from the google datastore and kept in the local database.
    // any announcement older than an hour is not fetched.
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        String[] location = Utility.getLocation(getContext());

        String zipCode = location[1];
        String countryCode = location[0];

        Needle needleApi = CloudEndPointBuilderHelper.getEndpoints();
        AdvertisementCollection adCollection = null;
        List<ContentValues> cVVector = new ArrayList<>();

        try {
            long timeInMillis = System.currentTimeMillis();
            timeInMillis = timeInMillis - HOUR_IN_MILLIS;

            provider.delete(NeedleContract.AdvertisementEntry.CONTENT_URI, null, null);
            adCollection = needleApi.ads().
                    getNeighborhoodAds(countryCode, zipCode, new DateTime(timeInMillis)).execute();
            if(adCollection.size() == 2) return;
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
                adValues.put(NeedleContract.AdvertisementEntry.COLUMN_DATABASE_ID,
                        ad.getKey());
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
        } catch(IOException err) {
            err.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }


    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();

        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);




        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    private static Account getSyncAccount(Context context) {
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
            onAccountCreated(newAccount, context);
        }
        return newAccount;

    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        NeedleSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        Bundle bundle = new Bundle();


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
