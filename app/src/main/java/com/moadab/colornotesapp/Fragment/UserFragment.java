package com.moadab.colornotesapp.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moadab.colornotesapp.AboutActivity;
import com.moadab.colornotesapp.LoginActivity;
import com.moadab.colornotesapp.ProfileActivity;
import com.moadab.colornotesapp.R;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserFragment extends Fragment {

    private ImageView logout,account,about;
    private TextView userName,userBio;
    private CircleImageView userImage;
    Dialog singOutDialog;
    TextView btnSingout,cancel;
    FirebaseAuth mAuth;
    TextView profileText,aboutApp,txtSingOut;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_user, container, false);

        userName = view.findViewById(R.id.user);
        userBio = view.findViewById(R.id.bio);
        userImage = view.findViewById(R.id.pro_user);
        mAuth = FirebaseAuth.getInstance();
        profileText = view.findViewById(R.id.txt_pro);
        aboutApp = view.findViewById(R.id.txt_about);

        readInfoUser();

        account = view.findViewById(R.id.account);
        about = view.findViewById(R.id.about);
        txtSingOut = view.findViewById(R.id.txt_logout);

        account.setOnClickListener(v -> userProfile());
        txtSingOut.setOnClickListener(v -> singOutDialog.show());
        about.setOnClickListener(v -> aboutApps());

        singOutDialog = new Dialog(getActivity());
        singOutDialog.setContentView(R.layout.logout_dialog);
        singOutDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        singOutDialog.setCancelable(false);
        btnSingout = singOutDialog.findViewById(R.id.btn_singout);
        cancel = singOutDialog.findViewById(R.id.s_cancel);
        logout = view.findViewById(R.id.logout);

        btnSingout.setOnClickListener(v -> singOut());
        cancel.setOnClickListener(v -> singOutDialog.dismiss());

        logout.setOnClickListener(v -> singOutDialog.show());
        profileText.setOnClickListener(v -> userProfile());
        aboutApp.setOnClickListener(v -> aboutApps());

        return view;


    }




    /* get user information in firebase */
    private void readInfoUser() {

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(mAuth.getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String txtName = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                String imgProfile = Objects.requireNonNull(snapshot.child("imageurlProfile").getValue().toString());
                String txtBio = Objects.requireNonNull(snapshot.child("bio").getValue().toString());

                userName.setText(txtName);
                userBio.setText(txtBio);

                if (!imgProfile.equals("default")) {
                    Picasso.get()
                            .load(imgProfile)
                            .placeholder(R.drawable.profile)
                            .error(R.drawable.profile)
                            .into(userImage);

                }else {
                    userImage.setImageResource(R.drawable.profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void singOut(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity() , LoginActivity.class));
        getActivity().finish();

        Toast.makeText(getActivity(), "Sing out is Successful" , Toast.LENGTH_SHORT).show();
    }

    private void userProfile(){

        startActivity(new Intent(getActivity() , ProfileActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));

    }

    private void aboutApps() {
        startActivity(new Intent(getActivity() , AboutActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));

    }
}