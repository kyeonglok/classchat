package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_popup);
        Button okbtn = findViewById(R.id.button_ok);
        Button xbtn = findViewById(R.id.button_cancel);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Intent i = getIntent();
        String time = Calendar.getInstance().getTime().toString();
        String r_msg,r_time,r_id;
        r_id = i.getStringExtra("id");
        r_time = i.getStringExtra("time");
        r_msg = i.getStringExtra("msg");

        Map<String, Object> r_up = new HashMap<>();
        r_up.put("id",r_id);
        r_up.put("msg",r_msg);
        r_up.put("time",r_time);
        Map<String,Object> up = new HashMap<>();
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("reports").document(time).set(r_up)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                System.out.println("Data commit to firestore success");
                                Toast.makeText(ReportActivity.this, "your report submitted", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Database upload failure");
                    }
                });
            }
        });
        xbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
