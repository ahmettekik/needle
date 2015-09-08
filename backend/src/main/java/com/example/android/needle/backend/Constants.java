package com.example.android.needle.backend;

/**
 * Created by jonfisk on 04/09/15.
 */
public final class Constants {
     // User: Update keys

     /**
     * Google Cloud Messaging API key.
     */
    public static final String GCM_API_KEY = "YOUR-GCM-API-KEY";

    /**
     * Android client ID from Google Cloud console.
     */
    public static final String ANDROID_CLIENT_ID = "YOUR-ANDROID-CLIENT-ID";



    /**
     * Web client ID from Google Cloud console.
     */
    public static final String WEB_CLIENT_ID = "YOUR-WEB-CLIENT-ID";

    /**
     * Audience ID used to limit access to some client to the API.
     */
    public static final String AUDIENCE_ID = WEB_CLIENT_ID;

    /**
     * API package name.
     */
    public static final String API_OWNER =
            "mobileassistantbackend.sample.google.com";

    /**
     * API package path.
     */
    public static final String API_PACKAGE_PATH = "";

    /**
     * Default constrictor, never called.
     */
    private Constants() { }
}


