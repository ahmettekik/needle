package com.example.android.needle;

import com.example.android.needle.backend.needle.Needle;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

/**
 * Created by jonfisk on 11/09/15.
 */
public final class CloudEndPointBuilderHelper {


    public static Needle getEndpoints() {
        Needle.Builder builder = new Needle.Builder(
                AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null)
                .setApplicationName("com.example.android.needle")
                .setRootUrl(BuildConfig.ROOT_URL);


        return builder.build();

    }


}