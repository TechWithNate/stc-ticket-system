package com.nate.stctickets.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nate.stctickets.R;
import com.nate.stctickets.fragments.BookingsFragment;
import com.nate.stctickets.fragments.WalletFragment;
import com.nate.stctickets.fragments.HomeFragment;
import com.nate.stctickets.fragments.ProfileFragment;

public class Home extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        initViews();
        openFragment(new HomeFragment());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int menuId = item.getItemId();
                if (menuId == R.id.home_menu) {
                    openFragment(new HomeFragment());
                } else if (menuId == R.id.booking_menu) {
                    openFragment(new BookingsFragment());
                }else if (menuId == R.id.help_menu){
                    openFragment(new WalletFragment());
                }else if (menuId == R.id.profile_menu){
                    openFragment(new ProfileFragment());
                }
                return false;
            }
        });

    }

    private void initViews(){
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void openFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}