package com.example.android.needle.backend;

/**
 * Created by jonfisk on 04/09/15.
 */
public final class Constants {
     // User: Update keys

     /**
     * Google Cloud Messaging API key.
     */
    public static final String GCM_API_KEY = "AIzaSyDyrKMw7lO6SlO2Rir8EAt3WVTkr4_9i-A";

    /**
     * Android client ID from Google Cloud console.
     */
    public static final String ANDROID_CLIENT_ID =
            "644704306847-queqvv140sn1bn11jf08tehs4lva1qtr.apps.googleusercontent.com";



    /**
     * Web client ID from Google Cloud console.
     */
    public static final String WEB_CLIENT_ID =
            "644704306847-rlnhirpqfl8da2hmd92braiq0kcrcq4c.apps.googleusercontent.com";

    /**
     * Audience ID used to limit access to some client to the API.
     */
    public static final String AUDIENCE_ID = WEB_CLIENT_ID;

    /**
     * API package name.
     */
    public static final String API_OWNER =
            "backend.needle.android.example.com";

    /**
     * API package path.
     */
    public static final String API_PACKAGE_PATH = "";

    /**
     * Default constrictor, never called.
     */
    private Constants() { }
}


