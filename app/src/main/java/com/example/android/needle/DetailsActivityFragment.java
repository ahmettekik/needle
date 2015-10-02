package com.example.android.needle;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.needle.backend.needle.Needle;
import com.example.android.needle.data.NeedleContract.AdvertisementEntry;
import com.google.api.client.util.DateTime;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
                    View.OnClickListener{
    public static final String DATABASE_ID_EXTRA = "database_id";
    private String mEmail;

    private final String TAG = getClass().getSimpleName();

    private Cursor mCursor;
    private Uri mUri;
    private long mDatabaseId = -1;
    private String mDescription;
    private String mName;
    private String mNumber;

    private static final int DETAIL_LOADER = 0;

    public static final String DESCRIPTION_EXTRA = "description";
    public static final String NAME_EXTRA = "name";
    public static final String NUMBER_EXTRA = "number";
    private static final String sAdvertisementIdSelection =
            AdvertisementEntry.TABLE_NAME + "." +
                    AdvertisementEntry._ID + " = ? ";


    private String[] sSelectionArgs = new String[1];

    public static final String[] AD_COLUMNS = {
            AdvertisementEntry.TABLE_NAME + "." + AdvertisementEntry._ID,
            AdvertisementEntry.COLUMN_DATE,
            AdvertisementEntry.COLUMN_USER_EMAIL,
            AdvertisementEntry.COLUMN_DESC,
            AdvertisementEntry.COLUMN_PHONE_NUMBER,
            AdvertisementEntry.COLUMN_NAME,
            AdvertisementEntry.COLUMN_DATABASE_ID
    };

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

        mUri = AdvertisementEntry.CONTENT_URI;

        sSelectionArgs[0] = Long.toString(id);

        Log.d(TAG, "uri is: " + mUri);
        mCursor = getActivity().getContentResolver().query(
                mUri,
                null,
                sAdvertisementIdSelection,
                sSelectionArgs,
                null
        );


        Log.d(TAG, "cursor is: " + mCursor);


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


        Long time = data.getLong(COL_AD_DATE);
        holder.timeView.setText(new DateTime(time).toString());
        //TODO: change this view to show when the ad is posted and when it'll be removed.

        mNumber = data.getString(COL_AD_NUM);
        if(mNumber == null) {
            holder.dialView.setVisibility(View.INVISIBLE);
            holder.dialView.setVisibility(View.INVISIBLE);
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

        //TODO: change the button behaviors




    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
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
            case R.id.details_delete_button: {
                new DeleteAdTask().execute();
                break;
            }
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
            case R.id.dial_imageView: {
                Uri number = Uri.parse("tel:" + mNumber);
                Intent callIntent = new Intent(Intent.ACTION_VIEW, number);
                startActivity(callIntent);
                break;
            }
            case R.id.text_imageView: {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setType("text/plain");
                sendIntent.setData(Uri.parse("sms:" + mNumber));
                sendIntent.putExtra("sms_body", "\"Needle-" + mDatabaseId + "\"");
                startActivity(sendIntent);
            }
        }


    }

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

    private class DeleteAdTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Needle mNeedleApi = CloudEndPointBuilderHelper.getEndpoints();

            try {
                mNeedleApi.ads().removeAdvertisement(mDatabaseId).execute();
            } catch(IOException e) {
                Log.d(TAG, e.getMessage());
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
