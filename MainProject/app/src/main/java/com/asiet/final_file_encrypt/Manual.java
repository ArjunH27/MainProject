package com.asiet.final_file_encrypt;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
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

import static android.app.Activity.RESULT_OK;

public class Manual extends Activity implements View.OnClickListener {

    Button filedecrpyt,filepick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        filepick = (Button) findViewById(R.id.btnfile);
        filepick.setOnClickListener(this);
        filedecrpyt = (Button) findViewById(R.id.btnfiledecrypt);
        filedecrpyt.setOnClickListener(this);
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

                        encrypt_single(new File(path));


                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    File yourFile=null;
    byte[] data=null,k;
    byte[] encrypted_data=null,decrypteddata=null;

    public byte[] encript(byte[] dataToEncrypt, byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher c = Cipher.getInstance("AES");
        c.init(1, new SecretKeySpec(key, "AES"));
        return c.doFinal(dataToEncrypt);
    }

    public void encrypt_single(File selectedfile) {
                    yourFile = selectedfile;


            try {

              /*  Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/abcd.JPG");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
                data=baos.toByteArray();*/

                data = FileUtils.readFileToByteArray(selectedfile);
            }
            catch (IOException e) {
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
    public void saveFile(byte[] data, String outFileName){

        FileOutputStream fos=null;
        try {

            //fos = new FileOutputStream("/storage/emulated/0/abcd.JPG");
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
        if(v.getId()==R.id.btnfile)
        {
            showFileChooser();

        }
        else
            if(v.getId()==R.id.btnfiledecrypt)
            {
               Intent i = new Intent(Manual.this,Manual_Decrypt.class);
                startActivity(i);


            }

    }
}
