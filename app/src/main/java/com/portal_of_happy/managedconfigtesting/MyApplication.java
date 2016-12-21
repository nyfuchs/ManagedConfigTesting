package com.portal_of_happy.managedconfigtesting;

import android.app.ActivityManager;
import android.app.Application;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.UserHandle;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.util.Log;

/**
 * Created by nyfuchs on 10/17/16.
 */
public class MyApplication extends Application {

    private final static String PHONE_ACCOUNT_ID = "PHONE_ACCOUNT_ID";
    private final static String APPLICATION_TAG = "Testing App";

    @Override
    public void onCreate(){

        super.onCreate();
        Log.d(APPLICATION_TAG, "app starting");

        TelecomManager telecomManager = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);

        ActivityManager am = (ActivityManager)
                getSystemService(Context.ACTIVITY_SERVICE);
        DevicePolicyManager dpm = (DevicePolicyManager)
                getSystemService(Context.DEVICE_POLICY_SERVICE);
        String pkg = getApplicationContext().getPackageName();

        boolean shouldStartLockTask = (am.getLockTaskModeState() ==
                ActivityManager.LOCK_TASK_MODE_NONE && dpm.isLockTaskPermitted(pkg));



        ComponentName componentName =
                new ComponentName(getPackageName(), MyConnectionService.class.getCanonicalName());

        int myUid = android.os.Process.myUid();
        UserHandle userHandle = UserHandle.getUserHandleForUid(android.os.Process.myUid());
        PhoneAccountHandle phoneAccountHandle =
                new PhoneAccountHandle(componentName, PHONE_ACCOUNT_ID, userHandle);
        PhoneAccount phoneAccount =
                new PhoneAccount.Builder(phoneAccountHandle, getPackageName())
                        .setCapabilities(PhoneAccount.CAPABILITY_CALL_PROVIDER).build();
        telecomManager.registerPhoneAccount(phoneAccount);
    }
}
