package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.myapplication.Navigation.BoardFragment;
import com.example.myapplication.Navigation.ChatFragment;
import com.example.myapplication.Navigation.HomeFragment;
import com.example.myapplication.Navigation.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Fragment boardFragment;
    private Fragment chatFragment;
    private Fragment homeFragment;
    private Fragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homeFragment = new HomeFragment();
        chatFragment = new ChatFragment();
        boardFragment = new BoardFragment();
        settingFragment = new SettingFragment();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.navigation_home:
                        setFragment(0);
                        break;
                    case R.id.navigation_chat:
                        setFragment(1);
                        break;
                    case R.id.navigation_board:
                        setFragment(2);
                        break;
                    case R.id.navigation_setting:
                        setFragment(3);
                        break;
                }
                return true;
            }
        });

        setFragment(0);

    }
    private void setFragment(int n){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch(n){
            case 0:
                ft.replace(R.id.frameLayout,homeFragment);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.frameLayout,chatFragment);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.frameLayout,boardFragment);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.frameLayout,settingFragment);
                ft.commit();
                break;
        }
    }
}