package com.asiet.final_file_encrypt;


import android.app.Activity;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends Activity implements View.OnClickListener {

    byte[] data;
    Button decryptbtn,add;
    byte[] decrypteddata;
    ProgressDialog dialog;
    byte[] encrypted_data;
    public  byte[] k;

    Button read;
    File root;
    Button unread,manual;
    File yourFile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ComponentName cn=new ComponentName(this, AdminReceiver.class);
        DevicePolicyManager mgr=
                (DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);
        registerReceiver(new PhoneUnlockedReceiver(), new IntentFilter("android.intent.action.USER_PRESENT"));
        registerReceiver(new PhonelockedReceiver(), new IntentFilter(Intent.ACTION_SCREEN_OFF));
check = true;
        if (!mgr.isAdminActive(cn))
        {
            Intent intent=
                    new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cn);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                getString(R.string.device_admin_explanation));
        startActivity(intent);
    }


        PrefManager obj = new PrefManager(getApplicationContext());

        decryptbtn = (Button) findViewById(R.id.dbtn);
        decryptbtn.setOnClickListener(this);
        read = (Button) findViewById(R.id.btnencrypt);
        read.setOnClickListener(this);
        unread = (Button) findViewById(R.id.btnnon);
        unread.setOnClickListener(this);
        manual = (Button) findViewById(R.id.btnmanual);
        manual.setOnClickListener(this);
        add = (Button) findViewById(R.id.btn_add);
        add.setOnClickListener(this);

        k= Base64.decode(obj.get_enckey(),Base64.DEFAULT);
        int a =  obj.get_key();

      //  Toast.makeText(MainActivity.this,String.valueOf(a),Toast.LENGTH_LONG).show();

        //Toast.makeText(MainActivity.this,obj.get_enckey(),Toast.LENGTH_LONG).show();
        //Toast.makeText(MainActivity.this,String.valueOf(obj.get_phone()),Toast.LENGTH_LONG).show();
        //Toast.makeText(MainActivity.this,String.valueOf(obj.getincorrectpass()),Toast.LENGTH_LONG).show();

      /*  try {
            k = keyGen();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }*/

       // root = new File("/storage/");
        root = new File("/storage/");
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.dbtn) {
            dialog = new ProgressDialog(v.getContext(), 2);
            dialog.setProgressStyle(0);
            dialog.setTitle("Please wait");
            dialog.setMessage("Decrypting...");
            dialog.show();
            BackGround_decrypt b = new BackGround_decrypt();
            b.execute();

        } else if (v.getId() == R.id.btnencrypt) {
            dialog = new ProgressDialog(v.getContext(), 2);
            dialog.setProgressStyle(0);
            dialog.setTitle("Please wait");
            dialog.setMessage("Encrypting...");
            dialog.show();
            BackGround b = new BackGround();
            b.execute();

        } else if (v.getId() == R.id.btnnon) {
           // Toast.makeText(MainActivity.this, Environment.getExternalStorageDirectory().getPath(), Toast.LENGTH_LONG).show();

            for(int i=0;i<list.size();i++)
            {
                Toast.makeText(MainActivity.this,String.valueOf(Constants.list.get(i)),Toast.LENGTH_LONG).show();
            }
           /* String targetExtension = ".xml";

            File a = new File("/storage/emulated/0/abc.pdf");
            String name= a.getName();
            if (name.length() >= 1 )
            {
                int extIndex = name.lastIndexOf(".");
                if (extIndex != -1)
                {
                    String ext = name.substring(extIndex);
                    System.out.println(ext);
                    if (ext.equalsIgnoreCase(".pdf"))
                    {
                            name = name.substring(0, extIndex) + targetExtension;
                            System.out.println(" " + name);
                            File change = new File("/storage/emulated/0/"+name);
                            a.renameTo(change);

                    }
                }
            }*/
          
        }
        else if(v.getId()==R.id.btnmanual)
        {
            Intent i= new Intent(MainActivity.this,Manual.class);
            startActivity(i);
        }
        else if (v.getId()==R.id.btn_add)
        {
            if(counter<=2) {
                showFileChooser();
            }
            else
            Toast.makeText(MainActivity.this,"Only 3 files are permitted currently",Toast.LENGTH_LONG).show();
            counter++;
        }

    }
    int counter=0;
    private static final int FILE_SELECT_CODE = 0;

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        flag=1;
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
    static ArrayList list=new ArrayList();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();

                    path = data.getData().getPath();

                    list.add(path);
                    flag=0;



                    Constants.list = list;

                    Toast.makeText(MainActivity.this,"File Added Successfully",Toast.LENGTH_LONG).show();


                    //  decrypt_single(new File(path));


                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
      class BackGround extends AsyncTask<String, String, String> {
        BackGround() {
        }

        protected String doInBackground(String... params) {
            walkdir_encrypt(root);

            return null;
        }

        protected void onPostExecute(String s) {
            dialog.hide();
        }
    }
    class BackGround_decrypt extends AsyncTask<String, String, String> {

        protected String doInBackground(String... params) {
            walkdir_decrypt(root);

            return null;
        }

        protected void onPostExecute(String s) {
            dialog.hide();
        }
    }


    public void walkdir_encrypt(File dir) {
        String pdfPattern = ".pdf";
        File[] listFile = dir.listFiles();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) {
                    walkdir_encrypt(listFile[i]);
                } else if (listFile[i].getName().endsWith(pdfPattern) ){//||listFile[i].getName().endsWith(".jpg")) {
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

    public class PhoneUnlockedReceiver extends DeviceAdminReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {

            KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if (keyguardManager.isKeyguardSecure()) {

                    //phone was unlocked, do stuff here
                   // Toast.makeText(context,"Unlocked",Toast.LENGTH_LONG).show();
                    context.stopService(new Intent(context, MyService2.class));

                }
            }
        }
    }

    public ArrayList return_list()
    {
        return list;
    }


   public class PhonelockedReceiver extends DeviceAdminReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {

            context.startService(new Intent(context, MyService2.class));

        }
    }
    boolean check;
    @Override
    public void onResume()
    {
        super.onResume();

        if(check==false) {

            check=true;
            Intent i = new Intent(MainActivity.this, Password.class);
            //startActivity(i);
        }
    }
    int flag=0;
    @Override
    public void onPause()
    {
        super.onPause();

        if(flag!=1)
      check = false;
    }
}

