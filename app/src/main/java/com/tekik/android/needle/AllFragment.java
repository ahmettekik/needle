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

    // key for saving the email state
    private static final String KEY_EMAIL = "email";

    // intent key for id
    public static final String ID_EXTRA = "id";

    // loader id
    private static final int ADVERTISEMENT_LOADER = 0;


    // to sort the announcements wrt to descending date.
    private String mSortOrder = NeedleContract.AdvertisementEntry.COLUMN_DATE + " DESC";

    // extra to save the state of which member of the list was clicked the latest.
    private static final String POSITION_BUNDLE_KEY = "PositionBundleKey";
    // member field to keep track of where the scrolling of list view has ended
    private int mPosition = ListView.INVALID_POSITION;


    // member field for the backend api
    private Needle mNeedleApi;

    private  final String TAG = getClass().getSimpleName();


    private String mCountryCode;
    private String mZipCode;
    private String mEmail;

    // in order to close the cursor after it's done, introduced a member variable.
    private Cursor mCursor;

    // member field to declare the swipe refresh layout
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

    // this function is for starting the activity to add a new announcement.
    private void addNewAd() {
        Intent i = new Intent(getActivity(), NewAdvertisementActivity.class);
        if (mEmail != null) {
            i.putExtra(LoginActivity.EMAILEXTRA, mEmail);
            startActivity(i);
        }
    }
    // this function is to refresh the content after swiping down the screen.
    // There is .5 seconds wait time with every swipe to give time to server, content provider,
    // and the loader. you have to call setRefreshing(false) because, the rotating arrow will
    // never stop otherwise.
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
        // build uri with country code and zip code. this will yield the announcements
        // in a neighborhood.
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

        // If the zip code or country code changes, the onUpdate func will be called.
        if(!location[0].equals(mCountryCode) || !location[1].equals(mZipCode)) {
            mCountryCode = location[0];
            mZipCode = location[1];
            onUpdate();
        }



    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // here if the item position changed, that position is retained after loading the
        // announcements.
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

    // a dialog is called to sort the announcements. Right now, it is either date ascending
    // or date descending, the default behavior is descending.
    public void afterDialog(Boolean isAscending) {
        if(isAscending) {
            mSortOrder = NeedleContract.AdvertisementEntry.COLUMN_DATE + " ASC";
        } else {
            mSortOrder = NeedleContract.AdvertisementEntry.COLUMN_DATE + " DESC";
        }

        getLoaderManager().restartLoader(ADVERTISEMENT_LOADER, null, this);
    }


    // This is called in case any of the location values changes. It resyncs the Sync adapter
    // restarts the loader.
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
            // Settings activity starts
            case R.id.action_settings: {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            // New announcement screen will start. This case is a floating button for devices
            // after api 21.
            case R.id.action_plus: {
                addNewAd();
                return true;
            }
            // Sort by dialog will appear.
            case R.id.action_sort: {
                DialogFragment newFragment = SortbyDialog.newInstance();
                newFragment.show(getFragmentManager(), "SortbyDialog");
                return true;
            }
            // Implicit intent is initiated to send a mail to needle email.
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
            // implicit intent for rating Needle on Google Play.
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

    // This asynctask will send the user email to backend.
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