package com.asiet.final_file_encrypt;

/**
 * Created by Akash Bhaskaran on 08-07-2016.
 */
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AdminReceiver extends DeviceAdminReceiver {
    @Override
    public void onEnabled(Context ctxt, Intent intent) {
        ComponentName cn=new ComponentName(ctxt, AdminReceiver.class);
        DevicePolicyManager mgr=
                (DevicePolicyManager)ctxt.getSystemService(Context.DEVICE_POLICY_SERVICE);

        mgr.setPasswordQuality(cn,
                DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC);

        onPasswordChanged(ctxt, intent);

    }

    @Override
    public void onPasswordChanged(Context ctxt, Intent intent) {
        DevicePolicyManager mgr=
                (DevicePolicyManager)ctxt.getSystemService(Context.DEVICE_POLICY_SERVICE);
        int msgId;

        if (mgr.isActivePasswordSufficient()) {
            msgId=R.string.compliant;
        }
        else {
            msgId=R.string.not_compliant;
        }

        //Toast.makeText(ctxt, msgId, Toast.LENGTH_LONG).show();
    }

    int i=0;

    @Override
    public void onPasswordFailed(Context ctxt, Intent intent) {
        prefManager = new PrefManager(ctxt);
        //  Toast.makeText(ctxt, R.string.password_failed, Toast.LENGTH_LONG).show();

        if(prefManager.getincorrectpass()==4)
        {
            ctxt.startService(new Intent(ctxt, MyService.class));
          // ctxt.startService(new Intent(ctxt,PhotoTakingService.class));
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(String.valueOf(prefManager.get_phone()), null, String.valueOf(prefManager.get_enckey()), null, null);

        }
            prefManager.setincorrectpass(prefManager.getincorrectpass() + 1);


    }
    private PrefManager prefManager;

    @Override
    public void onPasswordSucceeded(Context ctxt, Intent intent) {

        Toast.makeText(ctxt, R.string.password_success, Toast.LENGTH_LONG).show();
        //ctxt.stopService(new Intent(ctxt, MyService.class));



    }
}


