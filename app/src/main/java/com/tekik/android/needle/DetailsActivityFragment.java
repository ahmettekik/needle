package com.tekik.android.needle;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tekik.android.needle.backend.needle.Needle;
import com.tekik.android.needle.data.NeedleContract;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
                    View.OnClickListener{
    public static final String DATABASE_ID_EXTRA = "database_id";
    private String mEmail;

    private final String TAG = getClass().getSimpleName();
    private static final long MINUTE_IN_MILLIES = 60 * 1000;
    private static final long HOUR_IN_MILLIES = 60 * 60 * 1000;

    private Cursor mCursor;
    private Uri mUri;
    private long mDatabaseId = -1;
    private String mDescription;
    private String mName;
    private String mNumber;

    // id for the loader.
    private static final int DETAIL_LOADER = 0;

    public static final String DESCRIPTION_EXTRA = "description";
    public static final String NAME_EXTRA = "name";
    public static final String NUMBER_EXTRA = "number";

    // this is a selection statement for the query. this selection will help us find the element
    // of the list by its back end database id.
    private static final String sAdvertisementIdSelection =
            NeedleContract.AdvertisementEntry.TABLE_NAME + "." +
                    NeedleContract.AdvertisementEntry._ID + " = ? ";


    private String[] sSelectionArgs = new String[1];

    // This is the projection for the query calls we're gonna execute.
    public static final String[] AD_COLUMNS = {
            NeedleContract.AdvertisementEntry.TABLE_NAME + "." + NeedleContract.AdvertisementEntry._ID,
            NeedleContract.AdvertisementEntry.COLUMN_DATE,
            NeedleContract.AdvertisementEntry.COLUMN_USER_EMAIL,
            NeedleContract.AdvertisementEntry.COLUMN_DESC,
            NeedleContract.AdvertisementEntry.COLUMN_PHONE_NUMBER,
            NeedleContract.AdvertisementEntry.COLUMN_NAME,
            NeedleContract.AdvertisementEntry.COLUMN_DATABASE_ID
    };


    // These are the indices of database entries wrt AD_COLUMNS projection string.
    // We won't have to deal with finding indices by calling another function.
    static final int COL_AD_ID = 0;
    static final int COL_AD_DATE = 1;
    static final int COL_AD_EMAIL = 2;
    static final int COL_AD_DESC = 3;
    static final int COL_AD_NUM = 4;
    static final int COL_AD_NAME = 5;
    static final int COL_AD_DBID = 6;


    public DetailsActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getActivity().getIntent();
        long id = 0;
        if(i != null) {
            mEmail = i.getStringExtra(LoginActivity.EMAILEXTRA);
            id = i.getLongExtra(AllFragment.ID_EXTRA, -1);
        }

        mUri = NeedleContract.AdvertisementEntry.CONTENT_URI;

        //id is retrieved as an extra from the intent.
        sSelectionArgs[0] = Long.toString(id);

        mCursor = getActivity().getContentResolver().query(
                mUri,
                null,
                sAdvertisementIdSelection,
                sSelectionArgs,
                null
        );




        getLoaderManager().initLoader(DETAIL_LOADER, null, this);


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        ViewHolder holder = new ViewHolder(rootView);
        rootView.setTag(holder);


        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(
                getActivity(),
                mUri,
                AD_COLUMNS,
                sAdvertisementIdSelection,
                sSelectionArgs,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(!data.moveToFirst()) return;


        ViewHolder holder = (ViewHolder) getView().getTag();

        mName = data.getString(COL_AD_NAME);
        holder.nameView.setText("Posted by " + mName);

        mDescription = data.getString(COL_AD_DESC);
        holder.descriptionView.setText(mDescription);


        long time = data.getLong(COL_AD_DATE);
        String sTime = formatDate(time);
        long remainingTime = time + HOUR_IN_MILLIES - System.currentTimeMillis();
        remainingTime /= MINUTE_IN_MILLIES;
        holder.timeView.setText("Posted at " + sTime + ", " + remainingTime + " minutes left" );

        mNumber = data.getString(COL_AD_NUM);
        if(mNumber == null) {
            holder.dialView.setVisibility(View.INVISIBLE);
            holder.textView.setVisibility(View.INVISIBLE);
        } else {
            holder.dialView.setOnClickListener(this);
            holder.textView.setOnClickListener(this);
        }

        String email = data.getString(COL_AD_EMAIL);
        mDatabaseId = data.getLong(COL_AD_DBID);
        if(!email.equals(mEmail)) {
            holder.editButton.setVisibility(View.INVISIBLE);
            holder.deleteButton.setVisibility(View.INVISIBLE);
        }
        else {
            holder.editButton.setOnClickListener(this);
            holder.deleteButton.setOnClickListener(this);
        }

        holder.mailView.setOnClickListener(this);

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
        }

        return super.onOptionsItemSelected(item);
    }

    private String formatDate(long time) {

        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        return formatter.format(new Date(time));


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
            // this button starts the new announcement activity in editing mode.
            case R.id.details_edit_button: {
                if(mDescription != null && mEmail != null && mName != null) {
                    Intent intent = new Intent(getActivity(), NewAdvertisementActivity.class);
                    intent.putExtra(LoginActivity.EMAILEXTRA, mEmail);
                    intent.putExtra(NAME_EXTRA, mName);
                    intent.putExtra(DESCRIPTION_EXTRA, mDescription);
                    intent.putExtra(DATABASE_ID_EXTRA, mDatabaseId);
                    if(mNumber != null) {
                        intent.putExtra(NUMBER_EXTRA, mNumber);
                    }
                    startActivity(intent);
                }
                break;
            }
            // this button deletes the announcement and returns the main activity.
            case R.id.details_delete_button: {
                new DeleteAdTask().execute();
                break;
            }
            // this button starts an implicit intent to send an email to announcement owner.
            case R.id.email_imageView: {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mEmail});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Needle - " + mDatabaseId);
                emailIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        Html.fromHtml("<i><font color=#757575>" + mDescription + "</font></i>")
                );
                startActivity(emailIntent);
                break;
            }
            // this button starts implicit intent to view the phone number.
            case R.id.dial_imageView: {
                Uri number = Uri.parse("tel:" + mNumber);
                Intent callIntent = new Intent(Intent.ACTION_VIEW, number);
                startActivity(callIntent);
                break;
            }
            // this button initiates an implicit intent to text an sms.
            case R.id.text_imageView: {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setType("text/plain");
                sendIntent.setData(Uri.parse("sms:" + mNumber));
                sendIntent.putExtra("sms_body", "\"Needle-" + mDatabaseId + "\"");
                startActivity(sendIntent);
            }
        }


    }
    // This is for simplifying the onCreateView function. sub views initiations are handled here.
    public static class ViewHolder {
        public final ImageView mailView;
        public final ImageView dialView;
        public final TextView nameView;
        public final TextView descriptionView;
        public final TextView timeView;
        public final Button editButton;
        public final Button deleteButton;
        public final ImageView textView;

        public ViewHolder(View view) {
            mailView = (ImageView) view.findViewById(R.id.email_imageView);
            dialView = (ImageView) view.findViewById(R.id.dial_imageView);
            textView = (ImageView) view.findViewById(R.id.text_imageView);
            nameView = (TextView) view.findViewById(R.id.user_name_textView);
            descriptionView = (TextView) view.findViewById(R.id.ad_desc_textView);
            timeView = (TextView) view.findViewById(R.id.ad_time_textView);
            editButton = (Button) view.findViewById(R.id.details_edit_button);
            deleteButton = (Button) view.findViewById(R.id.details_delete_button);
        }
    }

    // delete the announcement and go to main activity.
    private class DeleteAdTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Needle mNeedleApi = CloudEndPointBuilderHelper.getEndpoints();

            try {
                mNeedleApi.ads().removeAdvertisement(mDatabaseId).execute();
            } catch(IOException e) {
                Toast.makeText(getActivity(),
                        "An error occurred while performing the task",
                        Toast.LENGTH_SHORT
                ).show();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(LoginActivity.EMAILEXTRA, mEmail);
            startActivity(intent);
        }
    }
}
