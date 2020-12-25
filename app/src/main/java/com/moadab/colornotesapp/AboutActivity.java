package com.moadab.colornotesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AboutActivity extends AppCompatActivity {

    /* Initialize variables */
    TextView yourFeedback,cancel,send;
    EditText txtFeedbak;
    String txtfeed;
    ImageView back;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        /* Access to activity elements */
        yourFeedback = findViewById(R.id.your_feedback);
        back = findViewById(R.id.back_accounts);

        /* Access to Instance  Firebase Database*/
        reference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        /* Create Progress Dialog for sing up user*/
        progressDialog = new ProgressDialog(AboutActivity.this);

        yourFeedback.setOnClickListener(v -> feedbackDialog());

        back.setOnClickListener(v -> finish());
    }

    /* Show Customize feedback Dioalog */
    private void feedbackDialog() {

        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(this,R.style.Theme_MaterialComponents_Dialog_Alert);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.feedback_dialog,null);

        txtFeedbak = view.findViewById(R.id.feedbacks);
        cancel =  view.findViewById(R.id.f_cancel);
        send = view.findViewById(R.id.send);

        alert.setView(view);
        alert.setCancelable(false);

        AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        dialog.show();

        send.setOnClickListener(v -> {
            txtfeed = txtFeedbak.getText().toString().trim();
            if(TextUtils.isEmpty(txtfeed)){
                Toast.makeText(AboutActivity.this, "Feedback is empty" , Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            else {
                sendFeedback(txtfeed);
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(v -> dialog.dismiss());
    }

    /* method send feedback user in database */
    private void sendFeedback(String txtfeed) {

        /* Show Customize Progress Dioalog */
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        HashMap<String, Object> mapFeedback = new HashMap<>();
        mapFeedback.put("feedback" , txtfeed);

        reference.child("Feedback").child(auth.getCurrentUser().getUid()).setValue(mapFeedback).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(AboutActivity.this,"Your feedback was received, thank you",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AboutActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}