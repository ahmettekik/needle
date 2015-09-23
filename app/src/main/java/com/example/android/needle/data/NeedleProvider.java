package com.example.android.needle.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.example.android.needle.data.NeedleContract.AdvertisementEntry;

/**
 * Created by jonfisk on 22/09/15.
 */
public class NeedleProvider extends ContentProvider {

    private NeedleDbHelper mOpenHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();


    static final int ADVERTISEMENT = 100;
    static final int ADVERTISEMENT_WITH_EMAIL = 101;
    static final int ADVERTISEMENT_WITH_LOCATION = 102;

    private static final String sortOrder = AdvertisementEntry.COLUMN_DATE + " DESC";

    private static final String sAdvertisementEmailSelection =
            AdvertisementEntry.TABLE_NAME + "." +
                    AdvertisementEntry.COLUMN_USER_EMAIL + " = ? ";

    private static final String sAdvertisementLocationSelection =
            AdvertisementEntry.TABLE_NAME + "." +
                    AdvertisementEntry.COLUMN_COUNTRY_CODE + " = ? AND " +
                    AdvertisementEntry.COLUMN_ZIP_CODE + " = ? ";

    private static final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

    static {
        queryBuilder.setTables(AdvertisementEntry.TABLE_NAME);
    }

    private Cursor getAdvertisementsByEmail(Uri uri, String[] projection) {
        String email = AdvertisementEntry.getUserEmailFromUri(uri);

        String selection = sAdvertisementEmailSelection;
        String[] selectionArgs = new String[]{email};

        return queryBuilder.query(
                mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getAdvertisementsByLocation(Uri uri, String[] projection) {
        String[] selectionArguments = AdvertisementEntry.getUserLocationFromUri(uri);

        String selection = sAdvertisementLocationSelection;


        return queryBuilder.query(
                mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArguments,
                null,
                null,
                sortOrder
        );
    }


    static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        //content://com.example.android.needle/advertisement
        matcher.addURI(NeedleContract.CONTENT_AUTHORITY,
                NeedleContract.PATH_ADVERTISEMENT, ADVERTISEMENT);

        //content://com.example.android.needle/advertisement/[email_query]
        matcher.addURI(NeedleContract.CONTENT_AUTHORITY,
                NeedleContract.PATH_ADVERTISEMENT + "/*",
                ADVERTISEMENT_WITH_EMAIL);

        //content://com.example.android.sunshine.app/weather/[country_code]/[zip_code]
        matcher.addURI(NeedleContract.CONTENT_AUTHORITY,
                NeedleContract.PATH_ADVERTISEMENT + "/*/*",
                ADVERTISEMENT_WITH_LOCATION);


        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new NeedleDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor = null;

        switch(sUriMatcher.match(uri)) {
            case ADVERTISEMENT: {
                retCursor = queryBuilder.query(
                        mOpenHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case ADVERTISEMENT_WITH_EMAIL: {
                retCursor = getAdvertisementsByEmail(uri, projection);
                break;
            }
            case ADVERTISEMENT_WITH_LOCATION: {
                retCursor = getAdvertisementsByLocation(uri, projection);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        if(match == ADVERTISEMENT || match == ADVERTISEMENT_WITH_EMAIL ||
                match == ADVERTISEMENT_WITH_LOCATION) {
            return AdvertisementEntry.CONTENT_TYPE;
        }
        else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        if(match == ADVERTISEMENT) {
            long _id = db.insert(AdvertisementEntry.TABLE_NAME, null, values);
            if(_id > 0)
                returnUri = AdvertisementEntry.buildAdvertisementUri(_id);
            else
                throw new android.database.SQLException("Failed to insert row into " + uri);
        }
        else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int deletedRows;

        final int match = sUriMatcher.match(uri);

        // this makes delete all rows return the number of rows deleted
        if(null == selection) selection = "1";


        if(match == ADVERTISEMENT) {
            deletedRows = db.delete(AdvertisementEntry.TABLE_NAME, selection, selectionArgs);
        }
        else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(deletedRows != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsUpdated = 0;

        final int match = sUriMatcher.match(uri);


        if(match == ADVERTISEMENT) {
            rowsUpdated = db.update(
                    AdvertisementEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs
            );
        }
        else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }


        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        if(match == ADVERTISEMENT) {
            db.beginTransaction();
            int returnCount = 0;
            try {
                for(ContentValues value: values) {
                    long _id = db.insert(AdvertisementEntry.TABLE_NAME, null, value);
                    if(_id != -1) returnCount++;
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return returnCount;
        }
        else {
            return super.bulkInsert(uri, values);
        }
    }

    @Override
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
