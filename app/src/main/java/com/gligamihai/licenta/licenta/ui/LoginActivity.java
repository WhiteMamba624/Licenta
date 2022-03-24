package com.gligamihai.licenta.licenta.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gligamihai.licenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        EditText email = findViewById(R.id.editTextEmail);
        EditText password = findViewById(R.id.editTextPassword);
        CircularProgressButton loginButton = (CircularProgressButton) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.startAnimation();
                String loginEmail = email.getText().toString().trim();
                String loginPassword = password.getText().toString().trim();
                if (loginEmail.isEmpty() || loginPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please make sure that there are no empty fields", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(loginEmail, loginPassword);
                    // loginButton.doneLoadingAnimation(Color.parseColor("#00ADC1"), BitmapFactory.decodeResource(getResources(),R.drawable.ic_baseline_check_24));
                    loginButton.revertAnimation();
                    email.getText().clear();
                    password.getText().clear();
                }
            }
        });
    }

    public void goToRegisterScreen(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (mAuth.getCurrentUser().isEmailVerified()) {
                                //Toast.makeText(LoginActivity.this,"Authentication succesfully",Toast.LENGTH_SHORT).show();
                                goToMainActivity();
                            } else {
                                Toast.makeText(LoginActivity.this, "Please verify your email address", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void resetPasswordDialog(View view) {
        AlertDialog.Builder alertInputEmail = new AlertDialog.Builder(this);
        alertInputEmail.setTitle("Reset password");
        alertInputEmail.setMessage("Please enter your email so you can reset the password");
        final EditText emailInput = new EditText(this);
        alertInputEmail.setView(emailInput);
        emailInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        alertInputEmail.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = emailInput.getText().toString();
                resetPassword(email);
            }
        });

        alertInputEmail.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertInputEmail.show();
    }

    public void resetPassword(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "An email has been sent to your address", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goToMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }
}