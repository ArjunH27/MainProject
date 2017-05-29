package com.asiet.final_file_encrypt;

/**
 * Created by Akash Bhaskaran on 10-07-2016.
 */
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class MyService2 extends Service {

    private static String TAG = "MyService";
    private Handler handler;
    private Runnable runnable;
    private final int runTime = 5000;
    byte[] data,decrypteddata,encrypted_data,k;
    File yourFile;
    File root;
    ArrayList list=new ArrayList();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        // root = new File("/storage/emulated/0");

list = Constants.list;
        PrefManager obj = new PrefManager(getApplicationContext());
        k= Base64.decode(obj.get_enckey(), Base64.DEFAULT);
        /*try {
            k=keyGen();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }*/

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                handler.postDelayed(runnable, runTime);
            }
        };
        handler.post(runnable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        Log.i(TAG, "onDestroy");

        for(int i=0;i<list.size();i++) {
            String path = String.valueOf(list.get(i));
            decrypt_single(new File(path));
        }
        Log.i(TAG, "Decrypted");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");


        for(int i=0;i<list.size();i++) {
            String path = String.valueOf(list.get(i));
            encrypt_single(new File(path));
        }
        Log.i(TAG, "Encrypted2");
        return START_STICKY;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i(TAG, "onStart");
    }
    public void encrypt_single(File selectedfile) {
        yourFile = selectedfile;


        try {
            data = FileUtils.readFileToByteArray(selectedfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            encrypted_data = encript(data, k);
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
        } catch (NoSuchPaddingException e3) {
            e3.printStackTrace();
        } catch (InvalidKeyException e4) {
            e4.printStackTrace();
        } catch (IllegalBlockSizeException e5) {
            e5.printStackTrace();
        } catch (BadPaddingException e6) {
            e6.printStackTrace();
        }
        saveFile(encrypted_data, selectedfile.getName());

    }

    public void decrypt_single(File selectedFile)
    {
        yourFile = selectedFile;
        try {
            data = FileUtils.readFileToByteArray(yourFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            decrypteddata = decript(data, k);
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
        } catch (NoSuchPaddingException e3) {
            e3.printStackTrace();
        } catch (InvalidKeyException e4) {
            e4.printStackTrace();
        } catch (IllegalBlockSizeException e5) {
            e5.printStackTrace();
        } catch (BadPaddingException e6) {
            e6.printStackTrace();
        }
        saveFile(decrypteddata, selectedFile.getName());
    }
    public byte[] keyGen() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(192);
        return keyGenerator.generateKey().getEncoded();
    }

    public byte[] encript(byte[] dataToEncrypt, byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher c = Cipher.getInstance("AES");
        c.init(1, new SecretKeySpec(key, "AES"));
        return c.doFinal(dataToEncrypt);
    }

    public byte[] decript(byte[] encryptedData, byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher c = Cipher.getInstance("AES");
        c.init(2, new SecretKeySpec(key, "AES"));
        return c.doFinal(encryptedData);
    }
    public void saveFile(byte[] data, String outFileName){

        FileOutputStream fos=null;
        try {

            fos=new FileOutputStream(yourFile);
            // fos=new FileOutputStream(Environment.getExternalStorageDirectory()+File.separator+outFileName);
            fos.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}