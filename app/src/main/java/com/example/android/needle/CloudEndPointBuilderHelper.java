package com.example.android.needle;

import com.example.android.needle.backend.needle.Needle;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;

/**
 * Created by jonfisk on 11/09/15.
 */
public final class CloudEndPointBuilderHelper {


    static Needle getEndpoints() {
        Needle.Builder builder = new Needle.Builder(
                AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), getRequestInitializer())
                .setRootUrl(Constants.ROOT_URL);


        return builder.build();

    }


    /**
     * Returns appropriate HttpRequestInitializer depending whether the
     * application is configured to require users to be signed in or not.
     * @return an appropriate HttpRequestInitializer.
     */
    static HttpRequestInitializer getRequestInitializer() {

        return new HttpRequestInitializer() {
            @Override
            public void initialize(final HttpRequest arg0) {
            }
        };

    }
}