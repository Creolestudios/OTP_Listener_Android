package com.otpdemo; //Change your package name

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class SmsRetrieveBroadcastReceiver extends BroadcastReceiver {

    public static final int SMS_CONSENT_REQUEST = 1244;

    private final Activity activity;

    public SmsRetrieveBroadcastReceiver(Activity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
            int statusCode = smsRetrieverStatus.getStatusCode();
            switch (statusCode) {
                case CommonStatusCodes.SUCCESS:
                    // Get consent intent
                    Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                    try {
                        ComponentName name = consentIntent.resolveActivity(this.activity.getPackageManager());
                        Log.e("Chado", "onReceive: " + name.getPackageName() + " " + name.getClassName());
                        if (name.getPackageName().equalsIgnoreCase("com.google.android.gms") &&
                                name.getClassName().equalsIgnoreCase("com.google.android.gms.auth.api.phone.ui.UserConsentPromptActivity")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                consentIntent.removeFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                consentIntent.removeFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                consentIntent.removeFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                                consentIntent.removeFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
                            }
                            this.activity.startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);
                        }
                    } catch (ActivityNotFoundException e) {
                        // Handle the exception ...
                    }
                    break;
                case CommonStatusCodes.TIMEOUT:
                    // Time out occurred, handle the error.
                    break;
            }
        }
    }
}
