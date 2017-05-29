package com.asiet.final_file_encrypt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;

public class SetPassword extends AppCompatActivity implements View.OnClickListener {

    EditText pin1,pin2;
    Button go;
    private PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        pin1 = (EditText)findViewById(R.id.et_phone);
        pin2 = (EditText)findViewById(R.id.et_pin2);
        go =  (Button)findViewById(R.id.btngo);
        go.setOnClickListener(this);
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {

            if(prefManager.getincorrectpass()>=3)
            {
                startActivity(new Intent(SetPassword.this, Decrypt.class));
                finish();
            }
            else

                startActivity(new Intent(SetPassword.this, MainActivity.class));


        }
    }

    @Override
    public void onClick(View v) {
        String pass1,pass2;


        pass1 = pin1.getText().toString();
        pass2 = pin2.getText().toString();

        if(pass1.isEmpty()||pass2.isEmpty()||pass1.trim().equals("")||pass2.trim().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please enter a valid Password",Toast.LENGTH_LONG);
        }
        else
            if(!pass1.equals(pass2))
                Toast.makeText(getApplicationContext(),"Pin does not match",Toast.LENGTH_LONG);

        else
            {byte[] k=null;
                prefManager.setFirstTimeLaunch(false);
                PrefManager obj = new PrefManager(getApplicationContext());
                obj.setkey(Integer.parseInt(pass1));
                try {
                    k = keyGen();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                obj.setenckey(k);
                startActivity(new Intent(SetPassword.this, MainActivity.class));
            }

    }
    public byte[] keyGen() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(192);
        return keyGenerator.generateKey().getEncoded();
    }
}
