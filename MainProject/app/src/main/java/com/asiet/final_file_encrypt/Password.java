package com.asiet.final_file_encrypt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Password extends AppCompatActivity implements View.OnClickListener {

    Button go;
    EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        go = (Button)findViewById(R.id.btn_check);
        pass = (EditText)findViewById(R.id.et_pass);
        go.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        PrefManager obj = new PrefManager(getApplicationContext());

        if(pass.getText().toString().equals(obj.get_key().toString()))
        {
            Intent i = new Intent(Password.this,WelcomeActivity.class);
            startActivity(i);

        }
    }
}
