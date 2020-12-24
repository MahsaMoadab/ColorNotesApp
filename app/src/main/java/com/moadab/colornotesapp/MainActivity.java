package com.moadab.colornotesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.moadab.colornotesapp.Fragment.HomeFragment;
import com.moadab.colornotesapp.Fragment.UserFragment;

public class MainActivity extends AppCompatActivity {


    /* Initialize variables */
    private SpaceNavigationView space;
    private Fragment selectorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Access to activity elements */
        space = (SpaceNavigationView) findViewById(R.id.space);
        space.initWithSaveInstanceState(savedInstanceState);
        space.addSpaceItem(new SpaceItem("", R.drawable.ic_navigation));
        space.addSpaceItem(new SpaceItem("", R.drawable.ic_user));
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();


        /* Method setSpaceOnClickListener */
        space.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                startActivity(new Intent(MainActivity.this, AddNotesActivity.class));
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                switch (itemIndex) {
                    case 0:
                        selectorFragment = new HomeFragment();
                        break;
                    case 1:
                        selectorFragment = new UserFragment();
                        break;
                }
                if (selectorFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectorFragment).commit();
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        space.onSaveInstanceState(outState);
    }
}