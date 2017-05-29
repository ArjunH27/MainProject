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

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class MyService extends Service {

    private static String TAG = "MyService";
    private Handler handler;
    private Runnable runnable;
    private final int runTime = 5000;
    byte[] data,decrypteddata,encrypted_data,k;
    File yourFile;
    File root;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        // root = new File("/storage/emulated/0");
        root = new File("/storage/emulated/0/");

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
        walkdir_decrypt(root);
       // Log.i(TAG, "Decrypted");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");

        walkdir_encrypt(root);
        Log.i(TAG, "Encrypted");
        return START_STICKY;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i(TAG, "onStart");
    }
    public void walkdir_encrypt(File dir) {
        String pdfPattern = ".pptx";
        File[] listFile = dir.listFiles();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) {
                    walkdir_encrypt(listFile[i]);
                } else if (listFile[i].getName().endsWith(pdfPattern)){//||listFile[i].getName().endsWith(".jpg")) {
                    yourFile = listFile[i];

                    try {
                        data = FileUtils.readFileToByteArray(yourFile);
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
                    saveFile(encrypted_data, listFile[i].getName());
                }
            }
        }
    }

    public void walkdir_decrypt(File dir) {
        String pdfPattern = ".pptx";
        File[] listFile = dir.listFiles();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) {
                    walkdir_decrypt(listFile[i]);
                } else if (listFile[i].getName().endsWith(pdfPattern)){//||listFile[i].getName().endsWith(".jpg")) {
                    yourFile = listFile[i];
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
                    saveFile(decrypteddata, listFile[i].getName());
                }
            }
        }

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