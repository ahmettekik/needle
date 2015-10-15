package com.tekik.android.needle;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.tekik.android.needle.backend.needle.Needle;
import com.tekik.android.needle.backend.needle.model.UserAccount;
import com.tekik.android.needle.data.NeedleContract;
import com.tekik.android.needle.sync.NeedleSyncAdapter;

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

    private static final String POSITION_BUNDLE_KEY = "PositionBundleKey";
    private int mPosition = ListView.INVALID_POSITION;



    private Needle mNeedleApi;
    private  final String TAG = getClass().getSimpleName();

    private String mCountryCode;
    private String mZipCode;
    private String mEmail;
    private Cursor mCursor;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;


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

        if(savedInstanceState != null)
            mPosition = savedInstanceState.getInt(POSITION_BUNDLE_KEY);

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

        mListView = (ListView) rootView.findViewById(R.id.listView_all_ads);
        mListView.setAdapter(mAnnouncementAdapter);

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


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPosition = position;
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
        if( mPosition != ListView.INVALID_POSITION ) {
            outState.putInt(POSITION_BUNDLE_KEY, mPosition);
        }
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
        if( mPosition != ListView.INVALID_POSITION ) {
            mListView.smoothScrollToPosition(mPosition);
        }
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
                return true;
            }
            case R.id.send_feedback: {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"needleapp@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Needle - Feedback");
                emailIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        Html.fromHtml("<i><font color=#757575>"
                                + "Thank you for sending your feedback..."
                                + "</font></i>")
                );
                startActivity(emailIntent);
                return true;
            }
            case R.id.action_rate_needle: {
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id="
                                        + getActivity().getPackageName()))
                    );
                }

                return true;
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
                Toast.makeText(getActivity(),
                        "An error occurred while performing the task",
                        Toast.LENGTH_SHORT
                ).show();
            }

            return null;
        }
    }
}