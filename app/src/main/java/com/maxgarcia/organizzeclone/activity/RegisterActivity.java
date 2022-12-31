package com.maxgarcia.organizzeclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.maxgarcia.organizzeclone.R;
import com.maxgarcia.organizzeclone.config.FirebaseConfiguration;
import com.maxgarcia.organizzeclone.helper.Base64Custom;
import com.maxgarcia.organizzeclone.model.User;

public class RegisterActivity extends AppCompatActivity {
    private EditText editName,  editEmail, editPassword;
    private Button btnRegister;
    private FirebaseAuth auth;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editName = findViewById(R.id.editRegisterName);
        editEmail = findViewById(R.id.editRegisterEmail);
        editPassword = findViewById(R.id.EditRegisterPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textName = editName.getText().toString();
                String textEmail = editEmail.getText().toString();
                String textPassword = editPassword.getText().toString();

                if(textName.isEmpty() ){
                    Toast.makeText(RegisterActivity.this, getString(R.string.validation_name), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(textEmail.isEmpty() ){
                    Toast.makeText(RegisterActivity.this, getString(R.string.validation_email) , Toast.LENGTH_SHORT).show();
                    return;
                }

                if(textPassword.isEmpty() ){
                    Toast.makeText(RegisterActivity.this, getString(R.string.validation_password), Toast.LENGTH_SHORT).show();
                    return;
                }

                user = new User(textName,textEmail,textPassword);
                registerUser();

            }
        });
    }

    public void registerUser(){
        auth = FirebaseConfiguration.getFireBaseAuth();
        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String idUser = Base64Custom.codifyBase64(user.getEmail());
                    user.setIdUser(idUser);
                    user.save();
                    finish();
                }
                else{
                    String exception = "";
                    try {
                        throw  task.getException();
                    } catch ( FirebaseAuthWeakPasswordException e){
                            exception = getString(R.string.weak_password);
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        exception =  getString(R.string.invalid_credentials);
                    } catch (FirebaseAuthUserCollisionException e){
                        exception =  getString(R.string.user_collision);
                    } catch (Exception e) {
                        exception =  getString(R.string.error_register)+ ":" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(RegisterActivity.this, exception , Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}