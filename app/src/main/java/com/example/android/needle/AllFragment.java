package com.example.android.needle;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.android.needle.backend.needle.Needle;
import com.example.android.needle.backend.needle.model.UserAccount;
import com.example.android.needle.data.NeedleContract;
import com.example.android.needle.sync.NeedleSyncAdapter;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public  class AllFragment extends android.app.Fragment
            implements LoaderManager.LoaderCallbacks<Cursor>{


    private static final String KEY_EMAIL = "email";

    public static final String ID_EXTRA = "id";

    private static final int ADVERTISEMENT_LOADER = 0;


    private Needle mNeedleApi;
    private  final String TAG = getClass().getSimpleName();

    private String mCountryCode;
    private String mZipCode;
    private String mEmail;
    private Cursor mCursor;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    private SimpleCursorAdapter mAdvertisementAdapter;

    public static final String[] ADVERTISEMENT_COLUMNS = {
            NeedleContract.AdvertisementEntry.TABLE_NAME + "." + NeedleContract.AdvertisementEntry._ID,
            NeedleContract.AdvertisementEntry.COLUMN_DESC
    };


    public AllFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        if(intent != null) {
            mEmail = intent.getStringExtra(LoginActivity.EMAILEXTRA);
        }


        NeedleSyncAdapter.syncImmediately(getActivity());

        getLoaderManager().initLoader(ADVERTISEMENT_LOADER, null, this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCursor.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        if(savedInstanceState != null) {
            mEmail = savedInstanceState.getString(KEY_EMAIL);
        }
        String[] location = Utility.getLocation(getActivity());
        mCountryCode = location[0];
        mZipCode = location[1];

        mNeedleApi = CloudEndPointBuilderHelper.getEndpoints();
        if(mEmail != null) {
            new UserTask().execute();
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout)
                rootView.findViewById(R.id.swipe_refresh);

        Uri adForLocationUri = NeedleContract.AdvertisementEntry
                .buildAdvertisementwithLocation(mCountryCode, mZipCode);

        mCursor = getActivity().getContentResolver().query(
                adForLocationUri,
                null,
                null,
                null,
                null
        );

        mAdvertisementAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.list_item_ad,
                mCursor,
                new String[]{NeedleContract.AdvertisementEntry.COLUMN_DESC},
                new int[]{R.id.description_textView},
                0
        );


        ListView listView = (ListView) rootView.findViewById(R.id.listView_all_ads);
        listView.setAdapter(mAdvertisementAdapter);

        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.grey,
                R.color.needle_yellow,
                R.color.orange
        );
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if(cursor != null) {
                    Intent i = new Intent(getActivity(), DetailsActivity.class);
                    long adId = cursor.getLong(0);
                    i.putExtra(ID_EXTRA, adId);
                    i.putExtra(LoginActivity.EMAILEXTRA, mEmail);
                    startActivity(i);
                }
            }
        });

        return rootView;
    }

    private void refreshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NeedleSyncAdapter.syncImmediately(getActivity());
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 500);

        getLoaderManager().restartLoader(ADVERTISEMENT_LOADER, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_EMAIL, mEmail);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri adForLocationUri = NeedleContract.AdvertisementEntry
                .buildAdvertisementwithLocation(mCountryCode, mZipCode);

        return new CursorLoader(
                getActivity(),
                adForLocationUri,
                ADVERTISEMENT_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdvertisementAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdvertisementAdapter.swapCursor(null);
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