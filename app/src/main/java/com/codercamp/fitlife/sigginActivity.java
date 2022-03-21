package com.codercamp.fitlife;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import codercamp.fitlife.R;


public class sigginActivity extends AppCompatActivity {
    private Button login;
    private Button button;
    private FirebaseAuth mAuth;
    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siggin);

        editTextFirstName = findViewById(R.id.etFirstName);
        editTextLastName = findViewById(R.id.etLastName);
        editTextEmail = findViewById(R.id.etEmail);
        editTextPassword = findViewById(R.id.etPassword);

        button = findViewById(R.id.btnNext);
        login = findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(),qeustionaireActivity.class));
            finish();
        }

        login = findViewById(R.id.btnLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString().trim();
                String first = editTextFirstName.getText().toString().trim();
                String last = editTextLastName.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();



                //Checking if Data is Valid
                if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError("Email Is Required.");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Password is Required");
                    return;
                }
                if(TextUtils.isEmpty(first)){
                    editTextFirstName.setError("First Name Required");
                    return;
                }
                if(TextUtils.isEmpty(last)){
                    editTextLastName.setError("Last Name Required");
                    return;
                }
                if (password.length() < 6) {
                    editTextPassword.setError("Password Must Have At Least 6 Characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //Register User To FireBase
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Storing Info In DataBase
                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

                            Map newPost = new HashMap();
                            newPost.put("First Name", first);
                            newPost.put("Last Name", last);
                            newPost.put("Email", email);
                            newPost.put("Password", password);
                            current_user_db.setValue(newPost);


                            Toast.makeText(sigginActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),qeustionaireActivity.class));
                        } else {
                            Toast.makeText(sigginActivity.this, "Error Occured" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

        });

    }




}