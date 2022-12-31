package com.maxgarcia.organizzeclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.maxgarcia.organizzeclone.R;
import com.maxgarcia.organizzeclone.config.FirebaseConfiguration;
import com.maxgarcia.organizzeclone.model.User;

public class LoginActivity extends AppCompatActivity {
    private EditText editEmail, editPassword;
    private Button btnSignIn;
    private FirebaseAuth auth;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editEmail = findViewById(R.id.editLoginEmail);
        editPassword = findViewById(R.id.editLoginPassword);
        btnSignIn = findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail = editEmail.getText().toString();
                String textPassword = editPassword.getText().toString();

                if(textEmail.isEmpty() ){
                    Toast.makeText(LoginActivity.this, getString(R.string.validation_email) , Toast.LENGTH_SHORT).show();
                    return;
                }

                if(textPassword.isEmpty() ){
                    Toast.makeText(LoginActivity.this, getString(R.string.validation_password), Toast.LENGTH_SHORT).show();
                    return;
                }

                user = new User(textEmail,textPassword);
                handleSignIn();
            }
        });
    }

    public void handleSignIn() {
        auth = FirebaseConfiguration.getFireBaseAuth();
        auth.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    goToHome();
                }
                else{
                    String exception = "";
                    try {
                        throw  task.getException();
                    } catch ( FirebaseAuthInvalidUserException e){
                        exception = getString(R.string.invalid_user);
                    }  catch (FirebaseAuthInvalidCredentialsException e){
                        exception =  getString(R.string.invalid_login);
                    } catch (Exception e) {
                        exception =  getString(R.string.error_sign_in)+ ":" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, exception , Toast.LENGTH_LONG).show();
                }
            }
        });;
    }

    public void goToHome() {
            startActivity(new Intent(this,HomeActivity.class));
            finish();
    }
}