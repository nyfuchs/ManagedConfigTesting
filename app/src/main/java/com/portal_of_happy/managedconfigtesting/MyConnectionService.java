package com.portal_of_happy.managedconfigtesting;

import android.content.ComponentName;
import android.content.Context;
import android.os.UserHandle;
import android.telecom.Connection;
import android.telecom.ConnectionRequest;
import android.telecom.ConnectionService;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;

/**
 * Created by nyfuchs on 10/14/16.
 */
public class MyConnectionService extends ConnectionService {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
