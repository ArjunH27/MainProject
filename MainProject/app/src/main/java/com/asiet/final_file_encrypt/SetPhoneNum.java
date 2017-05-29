package com.asiet.final_file_encrypt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class SetPhoneNum extends AppCompatActivity implements View.OnClickListener {

    EditText phone;
    Button go;
    private PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_phone_num);
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            startActivity(new Intent(SetPhoneNum.this, SetPassword.class));

            finish();
        }
        phone = (EditText)findViewById(R.id.et_phone);
        go = (Button)findViewById(R.id.btn_go);

        go.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        String num = phone.getText().toString();

        if(num.length()!=10){
            Toast.makeText(getApplicationContext(),"Enter a valid number",Toast.LENGTH_LONG);

        }
        else
        {
            prefManager.setPhone(Long.parseLong(num));
            startActivity(new Intent(SetPhoneNum.this, SetPassword.class));
        }
    }
}
