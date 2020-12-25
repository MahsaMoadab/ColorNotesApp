package com.moadab.colornotesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moadab.colornotesapp.Utilities.Utility;

import java.util.HashMap;

public class AddNotesActivity extends AppCompatActivity {

    /* Initialize variables */
    private ImageView star,backToHome;
    private String flagStar;
    private FloatingActionButton addNote;
    private EditText noteTitle,noteDesc;
    private String createDate;
    ProgressDialog progressDialog;

    /* Initialize variables of Firebase */
    private DatabaseReference nRootRef;
    private FirebaseAuth nAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        /* Access to activity elements */
        flagStar = "false";
        star = findViewById(R.id.star);
        backToHome = findViewById(R.id.back_home);
        addNote = findViewById(R.id.add_note);
        noteTitle = findViewById(R.id.note_title);
        noteDesc = findViewById(R.id.note_desc);

        /* Show current time in create Date */
        Utility util = new Utility();
        createDate = util.getCurrentShamsidate();


        /* Access to Instance  Firebase Database*/
        nRootRef = FirebaseDatabase.getInstance().getReference();
        nAuth = FirebaseAuth.getInstance();

        /* Create Progress Dialog for login user*/
        progressDialog=new ProgressDialog(AddNotesActivity.this);

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagStar == "false"){
                    star.setImageResource(R.drawable.ic_star);
                    flagStar = "true";
                }
                else {
                    star.setImageResource(R.drawable.ic_outline_star);
                    flagStar = "false";
                }
            }
        });

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddNotesActivity.this , MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }
        });

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewNote();
            }
        });
    }


    private void addNewNote() {

        String txt_noteTitle = noteTitle.getText().toString().trim();
        String txt_noteDesc = noteDesc.getText().toString().trim();
        String txt_date = createDate;
        String favorite = flagStar;

        if (TextUtils.isEmpty(txt_noteTitle) || TextUtils.isEmpty(txt_noteDesc)) {
            Toast.makeText(AddNotesActivity.this ,"Please enter text in title or notes", Toast.LENGTH_SHORT).show();
        } else {
            /* Show Customize Progress Dioalog after click sing up */
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

            nRootRef = nRootRef.child("Notes");
            String noteId = nRootRef.push().getKey();
            nRootRef = FirebaseDatabase.getInstance().getReference("Notes");

            HashMap<String, Object> noteMap = new HashMap<>();

            /* get data and set in database */
            noteMap.put("id" , noteId);
            noteMap.put("title", txt_noteTitle);
            noteMap.put("description", txt_noteDesc);
            noteMap.put("craeted", txt_date);
            noteMap.put("modified", txt_date);
            noteMap.put("favorite", favorite);

            nRootRef.child(nAuth.getCurrentUser().getUid()).child(noteId).setValue(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(AddNotesActivity.this, "New Note Created", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddNotesActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AddNotesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }
}