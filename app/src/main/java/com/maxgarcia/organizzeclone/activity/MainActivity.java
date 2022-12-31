package com.maxgarcia.organizzeclone.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.maxgarcia.organizzeclone.R;
import com.maxgarcia.organizzeclone.activity.LoginActivity;
import com.maxgarcia.organizzeclone.activity.RegisterActivity;
import com.maxgarcia.organizzeclone.config.FirebaseConfiguration;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userIsAuthenticated();
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        userIsAuthenticated();
    }

    public void handleRegister(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }

    public void handleHaveRegister(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
    public void goToHome() {
        startActivity(new Intent(this,HomeActivity.class));
        finish();
    }

    public void userIsAuthenticated(){
        auth = FirebaseConfiguration.getFireBaseAuth();
        if( auth.getCurrentUser() != null){
            goToHome();
        }
    }
}