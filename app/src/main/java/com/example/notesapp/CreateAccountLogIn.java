package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

import java.util.regex.Pattern;

public class CreateAccountLogIn extends AppCompatActivity {

    EditText emailEditText,passwordEditText,confirmPasswordEditText;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_log_in);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        createAccountBtn = findViewById(R.id.create_account_btn);
        progressBar = findViewById(R.id.progress_bar);
        loginBtnTextView = findViewById(R.id.login_text_view_btn);
        createAccountBtn.setOnClickListener(v -> createAccount());

    }

     void createAccount() {
         String email = emailEditText.getText().toString();
         String password = passwordEditText.getText().toString();
         String confirmPassword = confirmPasswordEditText.getText().toString();

         boolean isValid = validateData(email, password, confirmPassword);
         if (!isValid) {
             return;
         }
         createAccountInFirebase(email, password);
         
     }

     void createAccountInFirebase(String email, String password) {
         changeInProgress(true);
         //create account in firebase

         FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(CreateAccountLogIn.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(CreateAccountLogIn.this,
                                                "Account created successfully",
                                                Toast.LENGTH_SHORT).show();
                                        firebaseAuth.getCurrentUser().sendEmailVerification();
                                        firebaseAuth.signOut();
                                        finish();
                                    }
                                    else {
                                        //Failed to create account
                                        Toast.makeText(CreateAccountLogIn.this,
                                                task.getException().getLocalizedMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
    }

    void changeInProgress(boolean isInProgress){
        if (isInProgress){
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }
    }

    boolean validateData(String email, String Password, String ConfirmPassword){
        //validate the data that are input by user.
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        emailEditText.setError("Email is invalid");
        return false;
    }
    if (Password.length()<6){
        emailEditText.setError("Password length is invalid");
        return false;
    }
    if (!Password.equals(ConfirmPassword)){
        confirmPasswordEditText.setError("Password does not match");
        return false;
    }
    return true;
}


}