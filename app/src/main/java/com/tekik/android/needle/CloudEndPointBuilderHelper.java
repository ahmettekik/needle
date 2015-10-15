package com.tekik.android.needle;

import com.tekik.android.needle.backend.needle.Needle;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

/**
 * Created by jonfisk on 11/09/15.
 */
public final class CloudEndPointBuilderHelper {

    // This function simply retrieves the backend api for client application.
    public static Needle getEndpoints() {
        Needle.Builder builder = new Needle.Builder(
                AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null)
                .setApplicationName("com.tekik.android.needle")
                .setRootUrl(BuildConfig.ROOT_URL);


        return builder.build();

    }


}