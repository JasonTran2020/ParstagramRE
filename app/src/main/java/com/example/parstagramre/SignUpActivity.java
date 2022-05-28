package com.example.parstagramre;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    EditText etUsername;
    EditText etPassword;
    Button btnNewAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etUsername=findViewById(R.id.etNewUsername);
        etPassword=findViewById(R.id.etNewPassword);
        btnNewAccount=findViewById(R.id.btnNewAccount);

        btnNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=etUsername.getText().toString();
                String password=etPassword.getText().toString();
                if(username==""||password==""){
                    Log.i("SignUpActivity","Please fill in a password and username");
                    Toast.makeText(SignUpActivity.this,"Please fill in a password and username",Toast.LENGTH_SHORT).show();
                }
                else{
                    makeNewAccount(username,password);
                }

            }
        });
    }

    private void makeNewAccount(String username, String password) {
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties

        user.setUsername(username);
        user.setPassword(password);
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    ParseUser.logOut();
                    finish();
                } else
                {

                }
            }
        });


    }
}