package com.example.android.needle;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android.needle.backend.needle.Needle;
import com.example.android.needle.backend.needle.model.UserAccount;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public  class AllFragment extends android.app.Fragment {

    private Needle mNeedleApi;
    private  final String TAG = getClass().getSimpleName();


    public AllFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        String email = null;

        Intent intent = getActivity().getIntent();
        if(intent != null) {
            email = intent.getStringExtra(LoginActivity.EMAILEXTRA);
        }

        mNeedleApi = CloudEndPointBuilderHelper.getEndpoints();
        if(email != null) {
            new UserTask().execute(email);
        }

        ListView listView = (ListView) rootView.findViewById(R.id.listView_all_ads);



        return rootView;
    }


    private class UserTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String userEmail = params[0];
            UserAccount userAccount = new UserAccount();
            userAccount.setEmail(userEmail);



            try {
                mNeedleApi.accounts().insertUserAccount(userAccount).execute();
            } catch(IOException e) {
                Log.d(TAG, e.getMessage());
            }

            return null;
        }
    }
}