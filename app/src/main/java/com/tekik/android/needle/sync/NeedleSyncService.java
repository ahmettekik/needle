package com.tekik.android.needle.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by jonfisk on 23/09/15.
 */
public class NeedleSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private NeedleSyncAdapter sNeedleSyncAdapter = null;

    @Override
    public void onCreate() {
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
