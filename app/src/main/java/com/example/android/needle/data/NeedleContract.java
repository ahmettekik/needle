package com.example.android.needle.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jonfisk on 21/09/15.
 */
public class NeedleContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.example.android.needle";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_ADVERTISEMENT = "advertisement";

    public static final class AdvertisementEntry implements BaseColumns {

        public static final String TABLE_NAME = "advertisement";

        public static final String COLUMN_USER_EMAIL = "user_email";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_COUNTRY_CODE = "country_code";

        public static final String COLUMN_ZIP_CODE = "zip_code";

        public static final String COLUMN_DESC = "description";

        public static final String COLUMN_PHONE_NUMBER = "phone_number";

        public static final String COLUMN_NAME = "name";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ADVERTISEMENT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                        + CONTENT_AUTHORITY + "/"
                        + PATH_ADVERTISEMENT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                        + CONTENT_AUTHORITY + "/"
                        + PATH_ADVERTISEMENT;

        public static Uri buildAdvertisementUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildAdvertisementUser(String userEmail) {
            return CONTENT_URI.buildUpon().appendPath(userEmail).build();
        }

        public static Uri buildAdvertisementwithLocation(String countryCode, String zipCode) {
            return CONTENT_URI
                    .buildUpon()
                    .appendQueryParameter(COLUMN_COUNTRY_CODE, countryCode)
                    .appendQueryParameter(COLUMN_ZIP_CODE, zipCode)
                    .build();
        }

        public static String getUserEmailFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String[] getUserLocationFromUri(Uri uri) {
            String[] location = new String[]{uri.getPathSegments().get(1),
                                                uri.getPathSegments().get(2)};

            return location;
        }


    }
}
