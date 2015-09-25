package com.example.android.needle.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by jonfisk on 23/09/15.
 */
public class NeedleSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private NeedleSyncAdapter sNeedleSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("NeedleSyncService", "onCreate - NeedleSyncService");
        synchronized (sSyncAdapterLock) {
            if(sNeedleSyncAdapter == null) {
                sNeedleSyncAdapter = new NeedleSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sNeedleSyncAdapter.getSyncAdapterBinder();
    }
}
