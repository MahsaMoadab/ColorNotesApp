package com.moadab.colornotesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moadab.colornotesapp.Model.Note;
import com.moadab.colornotesapp.Utilities.Utility;

public class EditNoteActivity extends AppCompatActivity {

    /* Initialize variables */
    private ImageView favorite,backToHome,delete;
    private String flagStar;
    private Button save;
    private EditText noteTitle,noteDesc;
    private String modifiedDate;
    private TextView modified,created;
    ProgressDialog progressDialog;
    Note mNote;
    Boolean flag = false;
    Dialog deleteDialog;
    Dialog saveDialog;
    TextView btnDelete,btnCancel,btnCancelSave,btnDiscard,btnSave;

    /* Initialize variables of Firebase */
    private DatabaseReference nRootRef;
    private FirebaseAuth nAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        /* Access to activity elements */
        flagStar = "false";
        favorite = findViewById(R.id.e_star);
        delete = findViewById(R.id.delete);
        backToHome =  findViewById(R.id.e_back_home);
        save = findViewById(R.id.save);
        noteDesc = findViewById(R.id.e_note_desc);
        noteTitle = findViewById(R.id.e_note_title);
        modified = findViewById(R.id.data_modified);
        created = findViewById(R.id.data_created);

        /* Access to activity elements */
        deleteDialog = new Dialog(EditNoteActivity.this);
        deleteDialog.setContentView(R.layout.delete_dialog);
        deleteDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        deleteDialog.setCancelable(false);
        btnDelete = deleteDialog.findViewById(R.id.btn_delete);
        btnCancel = deleteDialog.findViewById(R.id.btn_cancel);

        btnDelete.setOnClickListener(v -> {
            flag = true;
            deleteNote(mNote);
        });
        btnCancel.setOnClickListener(v -> deleteDialog.dismiss());


        /* Show Customize Save Dioalog after click button Save */
        saveDialog = new Dialog(EditNoteActivity.this);
        saveDialog.setContentView(R.layout.save_dialog);
        saveDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        saveDialog.setCancelable(false);
        btnSave = saveDialog.findViewById(R.id.btn_save);
        btnDiscard = saveDialog.findViewById(R.id.btn_discard);
        btnCancelSave = saveDialog.findViewById(R.id.btn_cansell);

        btnCancelSave.setOnClickListener(v -> saveDialog.dismiss());
        btnDiscard.setOnClickListener(v -> {
            flag = true;
            discardNote();
        });
        btnSave.setOnClickListener(v -> {
            flag = true;
            updateNote(mNote);
        });

        /* Show current time in modified Date */
        Utility util = new Utility();
        modifiedDate = util.getCurrentShamsidate();


        Intent intent = getIntent();

        /* get data in home fragment */
        String id = intent.getExtras().getString("id");
        String title = intent.getExtras().getString("title");
        String note = intent.getExtras().getString("note");
        String data = intent.getExtras().getString("date");
        String dateModified = intent.getExtras().getString("modified");
        String star = intent.getExtras().getString("star");

        mNote = new Note(id ,title,note ,data,dateModified,star);

        noteTitle.setText(title);
        noteDesc.setText(note);
        modified.setText(dateModified);
        created.setText(data);

        switch (star){
            case "true":
                flagStar = "true";
                favorite.setImageResource(R.drawable.ic_star);
                break;

            case "false":
                flagStar = "false";
                favorite.setImageResource(R.drawable.ic_outline_star);
                break;
        }


        /* Access to Instance  Firebase Database*/
        nRootRef = FirebaseDatabase.getInstance().getReference();
        nAuth = FirebaseAuth.getInstance();

        /* Create Progress Dialog for */
        progressDialog = new ProgressDialog(EditNoteActivity.this);

        delete.setOnClickListener(v -> {
            deleteDialog.show();
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNote(mNote);
            }
        });

        favorite.setOnClickListener(v -> {
            if(flagStar == "false"){
                favorite.setImageResource(R.drawable.ic_star);
                flagStar = "true";
            }
            else {
                favorite.setImageResource(R.drawable.ic_outline_star);
                flagStar = "false";
            }
        });

        backToHome.setOnClickListener(v -> saveDialog.show());
    }

    private void discardNote() {
        startActivity(new Intent(EditNoteActivity.this , MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }


    private void updateNote(Note note) {

        /* Show Customize Progress Dioalog after click save update note */
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        String txt_noteTitle = noteTitle.getText().toString().trim();
        String txt_noteDesc = noteDesc.getText().toString().trim();
        String txt_modified = modifiedDate;
        String favorite = flagStar;

        nRootRef.child("Notes").child(nAuth.getCurrentUser().getUid()).child(note.getNoteId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /* set new data in Realtime database in firebase */
                dataSnapshot.getRef().child("title").setValue(txt_noteTitle);
                dataSnapshot.getRef().child("description").setValue(txt_noteDesc);
                dataSnapshot.getRef().child("modified").setValue(txt_modified);
                dataSnapshot.getRef().child("favorite").setValue(favorite);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        progressDialog.dismiss();
        discardNote();

    }

    private void deleteNote(Note note) {

        /* detete this note in Realtime database in firebase */
        FirebaseDatabase.getInstance().getReference().child("Notes").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(note.getNoteId()).removeValue();

        startActivity(new Intent(EditNoteActivity.this , MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
        Toast.makeText(EditNoteActivity.this, "Delete Note" , Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {

        if(!flag){
            saveDialog.show();
        }
        else {
            super.onBackPressed();
            discardNote();
        }
    }
}