package com.example.parstagramre;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    public final static String TAG="LoginActivity";
    EditText etUserName;
    EditText etPassword;
    Button btnLogin;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(ParseUser.getCurrentUser()!=null)
            goMainActivity();

        etUserName=findViewById(R.id.etUserName);
        etPassword=findViewById(R.id.etPassword);
        btnLogin=findViewById(R.id.btnLogin);
        btnSignUp=findViewById(R.id.btnSignUp);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=etUserName.getText().toString();
                String password=etPassword.getText().toString();
                loginUser(username,password);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(i);
                //finish();
            }
        });

    }

    private void loginUser(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e!=null)
                {
                    Toast.makeText(LoginActivity.this,"Wrong Password or Username",Toast.LENGTH_SHORT).show();
                    Log.e(TAG,"Issue with log in with "+username+" "+password,e);
                    return;
                }
                //Go to main activity
                goMainActivity();
            }
        });
    }

    private void goMainActivity()
    {
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }
}