package com.portal_of_happy.managedconfigtesting;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    public MyService() {
    }

    // get the android id
    private static String getGsfAndroidId(Context context) {
        Uri URI = Uri.parse("content://com.google.android.gsf.gservices");
        String ID_KEY = "android_id";
        String params[] = {ID_KEY};
        Cursor c = context.getContentResolver().query(URI, null, null,
                params, null);
        if (!c.moveToFirst() || c.getColumnCount() < 2)
            return null;
        try {
            return Long.toHexString(Long.parseLong(c.getString(1)));
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Play Service", "Device id: " + getGsfAndroidId(this));
        return START_NOT_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
