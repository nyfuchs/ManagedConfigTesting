package com.portal_of_happy.managedconfigtesting;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.RestrictionsManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewA;
    private TextView textViewB;
    private Button button;
    private RestrictionsManager restrictionsManager;
    private final static String TAG = "Configuration Testing";
    private final static int REQUEST_PHONE_STATE_PERMISSION_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewA = (TextView) findViewById(R.id.textviewA);
        textViewB = (TextView) findViewById(R.id.textviewB);
        button = (Button) findViewById(R.id.testing_button);
        button.setOnClickListener(this);

        restrictionsManager = (RestrictionsManager) getSystemService(
                Context.RESTRICTIONS_SERVICE);
        applyConfiguration(restrictionsManager, textViewA);
//        experimentalApplyConfig(restrictionsManager, textViewB);

        IntentFilter restrictionsFilter = new IntentFilter(
                Intent.ACTION_APPLICATION_RESTRICTIONS_CHANGED);

        BroadcastReceiver restrictionsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                applyConfiguration(restrictionsManager,textViewA);
//                experimentalApplyConfig(restrictionsManager, textViewB);
            }
        };

        registerReceiver(restrictionsReceiver, restrictionsFilter);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[] {Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE_PERMISSION_CODE);
        }
    }

    public void onRequestPermissionResult(int requestCode,
                                          String permissions[], int[] grantResults){
        switch (requestCode) {
            case REQUEST_PHONE_STATE_PERMISSION_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    break;
                }
            }
        }
    }

    private void applyConfiguration(RestrictionsManager restrictionsManager,
                                    TextView textView) {
        Bundle appRestrictions =
                restrictionsManager.getApplicationRestrictions();

        if (appRestrictions != null) {
            Parcelable[] parcelables =
                    appRestrictions.getParcelableArray("config_list");
            String restrictionsString = "";
            if (parcelables != null && parcelables.length > 0) {
                restrictionsString += "Configuration:\n";
                for (int i = 0; i < parcelables.length; i++) {
                    restrictionsString += "  Config #" + i + ":\n";
                    Bundle configBundle = (Bundle) parcelables[i];
                    restrictionsString += "    server: " +
                            configBundle.getString("server") + "\n";
                    restrictionsString += "    username: " +
                            configBundle.getString("username") + "\n";
                    restrictionsString += "    password: " +
                            configBundle.getString("password") + "\n";
                }
            }
            textView.setText(restrictionsString);
        }
    }

    private void experimentalApplyConfig(
            RestrictionsManager restrictionsManager, TextView textView) {

        Bundle appRestrictions =
                restrictionsManager.getApplicationRestrictions();
        if (appRestrictions == null) {
            Log.d(TAG, "No configurations set");
            return;
        }
        Parcelable bundles[] = appRestrictions.getParcelableArray("config_list");

        String restrictionsString = "";

        int i;
        for (i = 0; i < bundles.length; i++) {
            Bundle config = (Bundle) bundles[i];
            Bundle b = config.getBundle("config");
            if (b == null) {
                b = config.getBundle(Integer.toString(i));
            }
            if (b == null) {
                b = config;
            }
            restrictionsString += "  Config #" + i + ":\n";
            restrictionsString += "    server: " +
                    b.getString("server") + "\n";
            restrictionsString += "    username: " +
                    b.getString("username") + "\n";
            restrictionsString += "    password: " +
                    b.getString("password") + "\n";
        }
        textView.setText(restrictionsString);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.testing_button:
                Log.d(TAG, MyConnectionService.class.getCanonicalName());
                TelecomManager telecomManager =
                        (TelecomManager) getSystemService(Context.TELECOM_SERVICE);

                try{
                    ApplicationInfo info = this.getPackageManager().getApplicationInfo(
                            getApplication().getApplicationContext().getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e){
                    e.printStackTrace();
                }






                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                        == PackageManager.PERMISSION_GRANTED) {
                    List<PhoneAccountHandle> phoneAccountHandleList =
                            telecomManager.getCallCapablePhoneAccounts();

                    for (PhoneAccountHandle phoneAccountHandle : phoneAccountHandleList) {
                        Log.d(TAG, phoneAccountHandle.toString());
                    }
                }



                break;
        }
    }
}
