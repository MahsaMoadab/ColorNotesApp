package com.moadab.colornotesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SingUpActivity extends AppCompatActivity {

    /* Initialize variables */
    private EditText name;
    private EditText email;
    private EditText password;
    private TextView btnLogin;
    private Button singUp;
    ProgressDialog progressDialog;

    /* Initialize variables of Firebase */
    private DatabaseReference sRootRef;
    private FirebaseAuth sAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        /* Access to activity elements */
        email = findViewById(R.id.s_emailaddress);
        password = findViewById(R.id.s_password);
        name = findViewById(R.id.s_personname);
        singUp = findViewById(R.id.s_btn_singup);
        btnLogin = findViewById(R.id.s_btn_login);

        /* Create Progress Dialog for sing up user*/
        progressDialog = new ProgressDialog(SingUpActivity.this);

        /* Access to Instance  Firebase Database*/
        sRootRef = FirebaseDatabase.getInstance().getReference();
        sAuth = FirebaseAuth.getInstance();

        /* TextView Login */
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SingUpActivity.this , LoginActivity.class));
                finish();
            }
        });

        /* Button Sing Up */
        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtName = name.getText().toString();
                String txtEmail = email.getText().toString();
                String txtPassword = password.getText().toString();
                if (TextUtils.isEmpty(txtName) || TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)) {
                    Toast.makeText(SingUpActivity.this, "!Values are empty!" , Toast.LENGTH_SHORT).show();
                } else if (txtPassword.length() < 6 ) {
                    Toast.makeText(SingUpActivity.this, "!The password is less than 6 characters!" , Toast.LENGTH_SHORT).show();
                } else {
                    signUpUser(txtName , txtEmail , txtPassword);
                }
            }
        });
    }

    /* Method User Sing Up in FireBase */
    private void signUpUser(final String name, final String email, final String password) {

        /* Show Customize Progress Dioalog after click sing up */
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        /* Create User With Email And Password */
        sAuth.createUserWithEmailAndPassword(email , password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String, Object> mapSingUp = new HashMap<>();
                mapSingUp.put("name" , name);
                mapSingUp.put("email" , email);
                mapSingUp.put("id" , sAuth.getCurrentUser().getUid());
                mapSingUp.put("bio" , "");
                mapSingUp.put("imageurlProfile" , "default");

                /* Insert Users information into RealTime Database */
                sRootRef.child("Users").child(sAuth.getCurrentUser().getUid()).setValue(mapSingUp).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(SingUpActivity.this,"Registration was successful",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SingUpActivity.this , MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(SingUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}