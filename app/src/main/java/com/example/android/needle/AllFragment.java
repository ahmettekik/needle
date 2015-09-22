package com.example.android.needle;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.needle.backend.needle.Needle;
import com.example.android.needle.backend.needle.model.Advertisement;
import com.example.android.needle.backend.needle.model.AdvertisementCollection;
import com.example.android.needle.backend.needle.model.UserAccount;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public  class AllFragment extends android.app.Fragment {

    private static final String KEY_COUNTRY_CODE = "country_code";
    private static final String KEY_ZIP_CODE = "zip_code";
    private static final String KEY_EMAIL = "email";

    private Needle mNeedleApi;
    private  final String TAG = getClass().getSimpleName();

    private String mCountryCode;
    private String mZipCode;
    private String mEmail;


    private ArrayAdapter<String> mDescriptionAdapter;
    private List<String> mDescriptionList;

    public AllFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);





        Intent intent = getActivity().getIntent();
        if(intent != null) {
            mEmail = intent.getStringExtra(LoginActivity.EMAILEXTRA);
            mZipCode = intent.getStringExtra(LoginActivity.ZIPCODEEXTRA);
            mCountryCode = intent.getStringExtra(LoginActivity.COUNTRYCODEEXTRA);
        }

        if(savedInstanceState != null) {
            mEmail = savedInstanceState.getString(KEY_EMAIL);
            mCountryCode = savedInstanceState.getString(KEY_COUNTRY_CODE);
            mZipCode = savedInstanceState.getString(KEY_ZIP_CODE);
        }

        mNeedleApi = CloudEndPointBuilderHelper.getEndpoints();
        if(mEmail != null) {
            new UserTask().execute();
        }

        mDescriptionList = new ArrayList<>();
        mDescriptionAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.list_item_ad,
                R.id.description_textView,
                mDescriptionList
        );
        ListView listView = (ListView) rootView.findViewById(R.id.listView_all_ads);

        listView.setAdapter(mDescriptionAdapter);


        new RetrieveAdvertisementTask().execute();



        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_COUNTRY_CODE, mCountryCode);
        outState.putString(KEY_ZIP_CODE, mZipCode);
        outState.putString(KEY_EMAIL, mEmail);
    }


    private class RetrieveAdvertisementTask extends AsyncTask<Void, Void, AdvertisementCollection> {

        @Override
        protected AdvertisementCollection doInBackground(Void... params) {

            ArrayList<Advertisement> ads = null;
            AdvertisementCollection adCollections = null;

            try {
                adCollections = mNeedleApi.
                                    ads().
                                    getNeighborhoodAds(mCountryCode, mZipCode).
                                    execute();
            } catch (IOException e) {
                Log.d(TAG, e.getMessage());
            }

            Log.d(TAG, "adCollections: " + adCollections);


            return adCollections;
        }

        @Override
        protected void onPostExecute(AdvertisementCollection collection) {

            if(collection == null) {
                Log.d(TAG, "Collection is null");
                return;
            }
            if(collection.getItems() != null) {
                for(Advertisement ad: collection.getItems()) {
                    mDescriptionList.add(ad.getDescription());
                }
            }

            mDescriptionAdapter.notifyDataSetChanged();

        }
    }


    private class UserTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            UserAccount userAccount = new UserAccount();
            userAccount.setEmail(mEmail);



            try {
                mNeedleApi.accounts().insertUserAccount(userAccount).execute();
            } catch(IOException e) {
                Log.d(TAG, e.getMessage());
            }

            return null;
        }
    }
}