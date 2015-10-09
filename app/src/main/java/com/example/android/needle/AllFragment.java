package com.example.android.needle;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Outline;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.AdapterView;
import android.widget.ListView;

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

    private String mSortOrder = NeedleContract.AdvertisementEntry.COLUMN_DATE + " DESC";




    private Needle mNeedleApi;
    private  final String TAG = getClass().getSimpleName();

    private String mCountryCode;
    private String mZipCode;
    private String mEmail;
    private Cursor mCursor;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    private AnnouncementAdapter mAnnouncementAdapter;

    public static final String[] ADVERTISEMENT_COLUMNS = {
            NeedleContract.AdvertisementEntry.TABLE_NAME + "." + NeedleContract.AdvertisementEntry._ID,
            NeedleContract.AdvertisementEntry.COLUMN_DESC,
            NeedleContract.AdvertisementEntry.COLUMN_DATE
    };

    static final int COL_AD_ID = 0;
    static final int COL_AD_DESC = 1;
    static final int COL_AD_DATE = 2;


    public AllFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

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
                mSortOrder
        );

        mAnnouncementAdapter = new AnnouncementAdapter(
                getActivity(),
                mCursor,
                0
        );

        ListView listView = (ListView) rootView.findViewById(R.id.listView_all_ads);
        listView.setAdapter(mAnnouncementAdapter);

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
                if (cursor != null) {
                    Intent i = new Intent(getActivity(), DetailsActivity.class);
                    long adId = cursor.getLong(0);
                    i.putExtra(ID_EXTRA, adId);
                    i.putExtra(LoginActivity.EMAILEXTRA, mEmail);
                    startActivity(i);
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View addButton = rootView.findViewById(R.id.add_button);
            addButton.setOutlineProvider(new ViewOutlineProvider() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void getOutline(View view, Outline outline) {
                    int diameter = getResources().getDimensionPixelSize(R.dimen.diameter);
                    outline.setOval(0, 0, diameter, diameter);
                }
            });
            addButton.setClipToOutline(true);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addNewAd();
                }
            });
        }
        return rootView;
    }

    private void addNewAd() {
        Intent i = new Intent(getActivity(), NewAdvertisementActivity.class);
        if (mEmail != null) {
            i.putExtra(LoginActivity.EMAILEXTRA, mEmail);
            startActivity(i);
        }
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

        Log.d(TAG, "uri: " + adForLocationUri);

        return new CursorLoader(
                getActivity(),
                adForLocationUri,
                ADVERTISEMENT_COLUMNS,
                null,
                null,
                mSortOrder
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        String[] location = Utility.getLocation(getActivity());


        if(!location[0].equals(mCountryCode) || !location[1].equals(mZipCode)) {
            mCountryCode = location[0];
            mZipCode = location[1];
            onUpdate();
        }



    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAnnouncementAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAnnouncementAdapter.swapCursor(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    public void afterDialog(Boolean isAscending) {
        if(isAscending) {
            mSortOrder = NeedleContract.AdvertisementEntry.COLUMN_DATE + " ASC";
        } else {
            mSortOrder = NeedleContract.AdvertisementEntry.COLUMN_DATE + " DESC";
        }

        getLoaderManager().restartLoader(ADVERTISEMENT_LOADER, null, this);
    }

    public void onUpdate() {
        NeedleSyncAdapter.syncImmediately(getActivity());
        getLoaderManager().restartLoader(ADVERTISEMENT_LOADER, null, this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_settings: {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            case R.id.action_plus: {
                addNewAd();
                return true;
            }
            case R.id.action_sort: {
                DialogFragment newFragment = SortbyDialog.newInstance();
                newFragment.show(getFragmentManager(), "SortbyDialog");
            }

        }

        return super.onOptionsItemSelected(item);
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