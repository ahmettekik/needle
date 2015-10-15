package com.tekik.android.needle.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by jonfisk on 22/09/15.
 */
public class NeedleProvider extends ContentProvider {

    private NeedleDbHelper mOpenHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();


    static final int ADVERTISEMENT = 100;
    static final int ADVERTISEMENT_WITH_EMAIL = 101;
    static final int ADVERTISEMENT_WITH_LOCATION = 102;
    static final int ADVERTISEMENT_WITH_LOCATION_AND_DATE = 103;




    private static final String sAdvertisementEmailSelection =
            NeedleContract.AdvertisementEntry.TABLE_NAME + "." +
                    NeedleContract.AdvertisementEntry.COLUMN_USER_EMAIL + " = ? ";

    private static final String sAdvertisementLocationSelection =
            NeedleContract.AdvertisementEntry.TABLE_NAME + "." +
                    NeedleContract.AdvertisementEntry.COLUMN_COUNTRY_CODE + " = ? AND " +
                    NeedleContract.AdvertisementEntry.COLUMN_ZIP_CODE + " = ? ";

    private static final String sAdvertisementLocationTimeSelection =
            NeedleContract.AdvertisementEntry.TABLE_NAME + "." +
                    NeedleContract.AdvertisementEntry.COLUMN_COUNTRY_CODE + " = ? AND " +
                    NeedleContract.AdvertisementEntry.COLUMN_ZIP_CODE + " = ? AND " +
                    NeedleContract.AdvertisementEntry.COLUMN_DATE + " <= ? ";

    private static final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

    static {
        queryBuilder.setTables(NeedleContract.AdvertisementEntry.TABLE_NAME);
    }

    private Cursor getAdvertisementsByEmail(Uri uri, String[] projection, String sortOrder) {
        String email = NeedleContract.AdvertisementEntry.getUserEmailFromUri(uri);

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

    private Cursor getAdvertisementsByLocation(Uri uri, String[] projection, String sortOrder) {
        String[] selectionArguments = NeedleContract.AdvertisementEntry.getUserLocationFromUri(uri);

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

    private Cursor getAdvertisementsByLocationAndTime(Uri uri, String[] projection, String sortOrderAsc) {
        String[] selectionArguments = NeedleContract.AdvertisementEntry.getLocationAndTimeFromUri(uri);
        String selection = sAdvertisementLocationTimeSelection;

        return queryBuilder.query(
                mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArguments,
                null,
                null,
                sortOrderAsc
        );
    }


    static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(NeedleContract.CONTENT_AUTHORITY,
                NeedleContract.PATH_ADVERTISEMENT, ADVERTISEMENT);

        matcher.addURI(NeedleContract.CONTENT_AUTHORITY,
                NeedleContract.PATH_ADVERTISEMENT + "/*",
                ADVERTISEMENT_WITH_EMAIL);

        matcher.addURI(NeedleContract.CONTENT_AUTHORITY,
                NeedleContract.PATH_ADVERTISEMENT + "/*/*",
                ADVERTISEMENT_WITH_LOCATION);

        matcher.addURI(NeedleContract.CONTENT_AUTHORITY,
                NeedleContract.PATH_ADVERTISEMENT + "/*/*/#",
                ADVERTISEMENT_WITH_LOCATION_AND_DATE);

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
            case ADVERTISEMENT_WITH_LOCATION_AND_DATE: {
                retCursor = getAdvertisementsByLocationAndTime(uri, projection, sortOrder);
                break;
            }
            case ADVERTISEMENT_WITH_LOCATION: {
                retCursor = getAdvertisementsByLocation(uri, projection, sortOrder);
                break;
            }
            case ADVERTISEMENT_WITH_EMAIL: {
                retCursor = getAdvertisementsByEmail(uri, projection, sortOrder);
                break;
            }
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

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        if(match == ADVERTISEMENT || match == ADVERTISEMENT_WITH_EMAIL ||
                match == ADVERTISEMENT_WITH_LOCATION ||
                match == ADVERTISEMENT_WITH_LOCATION_AND_DATE) {
            return NeedleContract.AdvertisementEntry.CONTENT_TYPE;
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
            long _id = db.insert(NeedleContract.AdvertisementEntry.TABLE_NAME, null, values);
            if(_id > 0)
                returnUri = NeedleContract.AdvertisementEntry.buildAdvertisementUri(_id);
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
            deletedRows = db.delete(NeedleContract.AdvertisementEntry.TABLE_NAME, selection, selectionArgs);
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
                    NeedleContract.AdvertisementEntry.TABLE_NAME,
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
                    long _id = db.insert(NeedleContract.AdvertisementEntry.TABLE_NAME, null, value);
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
