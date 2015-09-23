package com.example.android.needle.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.needle.data.NeedleContract.AdvertisementEntry;

/**
 * Created by jonfisk on 22/09/15.
 */
public class NeedleDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "needle.db";

    public NeedleDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_ADVERTISEMENT_TABLE = "CREATE TABLE " +
                AdvertisementEntry.TABLE_NAME + " (" +

                AdvertisementEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                AdvertisementEntry.COLUMN_USER_EMAIL + " TEXT NOT NULL," +
                AdvertisementEntry.COLUMN_DATE + " INTEGER NOT NULL," +
                AdvertisementEntry.COLUMN_COUNTRY_CODE + " TEXT NOT NULL," +
                AdvertisementEntry.COLUMN_ZIP_CODE + " TEXT NOT NULL," +
                AdvertisementEntry.COLUMN_DESC + " TEXT NOT NULL," +
                AdvertisementEntry.COLUMN_PHONE_NUMBER + " TEXT NOT NULL," +
                AdvertisementEntry.COLUMN_NAME + " TEXT NOT NULL );";

        db.execSQL(SQL_CREATE_ADVERTISEMENT_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AdvertisementEntry.TABLE_NAME);
        onCreate(db);
    }
}