package com.asiet.final_file_encrypt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
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

public class Manual_Decrypt extends AppCompatActivity implements View.OnClickListener {

    Button decrypt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual__decrypt);


        decrypt = (Button)findViewById(R.id.btn_file_decrypt);
        decrypt.setOnClickListener(this);
        PrefManager obj = new PrefManager(getApplicationContext());

        k= Base64.decode(obj.get_enckey(), Base64.DEFAULT);
    }
    private static final int FILE_SELECT_CODE = 0;

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    String path = null;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();

                    path = data.getData().getPath();

                    decrypt_single(new File(path));
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public byte[] decript(byte[] encryptedData, byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher c = Cipher.getInstance("AES");
        c.init(2, new SecretKeySpec(key, "AES"));
        return c.doFinal(encryptedData);
    }


    public void decrypt_single(File selectedFile)
    {
        yourFile = selectedFile;
        try {

        data = FileUtils.readFileToByteArray(yourFile);
        }


        catch (IOException e) {
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

    File yourFile=null;
    byte[] data=null,k;
    byte[] decrypteddata=null;
    @Override
    public void onClick(View v) {
        showFileChooser();

    }
}
