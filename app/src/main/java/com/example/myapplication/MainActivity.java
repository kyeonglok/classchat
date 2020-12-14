package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.myapplication.navigation.AlarmFragment;
import com.example.myapplication.navigation.board.BoardFragment;
import com.example.myapplication.navigation.ChatFragment;
import com.example.myapplication.navigation.MyPageFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Fragment boardFragment;
    private Fragment chatFragment;
    private Fragment alarmFragment;
    private Fragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarmFragment = new AlarmFragment();
        chatFragment = new ChatFragment();
        boardFragment = new BoardFragment();
        settingFragment = new MyPageFragment();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.navigation_board:
                        setFragment(0);
                        break;
                    case R.id.navigation_chat:
                        setFragment(1);
                        break;
                    case R.id.navigation_alarm:
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
        registerPushToken();
    }


    private void registerPushToken(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                String token = task.getResult().getToken();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("pushToken",token);

                FirebaseFirestore.getInstance().collection("pushtokens").document(uid).set(map);
            }
        });
    }
    private void setFragment(int n){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch(n){
            case 0:
                ft.replace(R.id.frameLayout,boardFragment);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.frameLayout,chatFragment);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.frameLayout,alarmFragment);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.frameLayout,settingFragment);
                ft.commit();
                break;
        }
    }
}