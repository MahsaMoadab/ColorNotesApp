package com.moadab.colornotesapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moadab.colornotesapp.Adapter.NoteAdapter;
import com.moadab.colornotesapp.EditNoteActivity;
import com.moadab.colornotesapp.Model.Note;
import com.moadab.colornotesapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements NoteAdapter.OnNoteListener{

    /* Initialize variables */
    private RecyclerView recyclerViewNotes;
    private NoteAdapter noteAdapter;
    private List<Note> noteslist;
    private FirebaseAuth mAuth;
    private DatabaseReference nRootRef;
    TextView name,email;
    ImageView profile;
    RelativeLayout nothing;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        /* Access to fragment elements */
        name = view.findViewById(R.id.user_name);
        email = view.findViewById(R.id.user_email);
        profile = view.findViewById(R.id.image_profile);
        nothing = view.findViewById(R.id.nothing);
        recyclerViewNotes = view.findViewById(R.id.recyclerview_notes);
        recyclerViewNotes.setHasFixedSize(true);
        recyclerViewNotes.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        mAuth = FirebaseAuth.getInstance();
        readInfoUser();
        noteslist = new ArrayList<>();
        noteAdapter = new NoteAdapter(getContext(), noteslist , this);
        recyclerViewNotes.setAdapter(noteAdapter);

        /* Access to Instance  Firebase Database*/
        nRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        readNotes();


        if (noteslist.size() == 0){
            nothing.setVisibility(View.VISIBLE);
        }
        else if (noteslist.size() > 0){
            nothing.setVisibility(View.GONE);
        }
        return view;
    }


    /* Method read Notes Firebase Database*/
    private void readNotes() {

        nRootRef = FirebaseDatabase.getInstance().getReference().child("Notes")
                .child((mAuth.getCurrentUser()).getUid());
        noteslist.clear();
        nRootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String id = Objects.requireNonNull(snapshot.child("id").getValue()).toString();
                String note = Objects.requireNonNull(snapshot.child("description").getValue()).toString();
                String date = Objects.requireNonNull(snapshot.child("craeted").getValue()).toString();
                String dateM = Objects.requireNonNull(snapshot.child("modified").getValue()).toString();
                String title = Objects.requireNonNull(snapshot.child("title").getValue()).toString();
                String noteFavorite = Objects.requireNonNull(snapshot.child("favorite").getValue()).toString();
                Note myNote = new Note(id,title, note, date, dateM,noteFavorite);

                noteslist.add(myNote);
                nothing.setVisibility(View.GONE);
                noteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override

            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /* get user information in firebase */
    private void readInfoUser() {

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(mAuth.getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String txtName = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                String txtEmail = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                String imgProfile = Objects.requireNonNull(snapshot.child("imageurlProfile").getValue().toString());

                name.setText(txtName);
                email.setText(txtEmail);

                if (!imgProfile.equals("default")) {
                    Picasso.get()
                            .load(imgProfile)
                            .placeholder(R.drawable.profile)
                            .error(R.drawable.profile)
                            .into(profile);

                }else {
                    profile.setImageResource(R.drawable.profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onNoteClick(int position) {

        /* pass data to Edit Note Activity */
        Intent intent = new Intent(getActivity(), EditNoteActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("id", noteslist.get(position).getNoteId());
        intent.putExtra("title", noteslist.get(position).getNoteTitle());
        intent.putExtra("note", noteslist.get(position).getNoteDesc());
        intent.putExtra("date", noteslist.get(position).getNoteCraeted());
        intent.putExtra("modified", noteslist.get(position).getNoteModified());
        intent.putExtra("star", noteslist.get(position).getNoteFavorite());
        startActivity(intent);
    }
}