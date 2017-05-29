package com.asiet.final_file_encrypt;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Decrypt extends AppCompatActivity implements View.OnClickListener {

    Button decrypt;
    EditText key;
    byte[] data,decrypteddata,k;
    File yourFile;
    File root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);

        decrypt = (Button)findViewById(R.id.btn_decrypt_all);
        decrypt.setOnClickListener(this);
        key = (EditText)findViewById(R.id.et_key);

        PrefManager obj = new PrefManager(getApplicationContext());
        k= Base64.decode(obj.get_enckey(), Base64.DEFAULT);
        root = new File("/storage/emulated/0/");
    }
    public void walkdir_decrypt(File dir) {
        String pdfPattern = ".pdf";
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
    @Override
    public void onClick(View v) {

        String ekey;
        Context c= getApplicationContext();

        ekey = key.getText().toString();
        prefManager = new PrefManager(getApplicationContext());

       // walkdir_decrypt(root);
        if(ekey.equals(prefManager.get_enckey()))
        {
            walkdir_decrypt(root);
            int k=0;
            prefManager.setincorrectpass(k);

            startActivity(new Intent(Decrypt.this, MainActivity.class));

        }


    }
    private PrefManager prefManager;

}
