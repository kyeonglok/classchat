package com.example.myapplication.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.ClassAdapter;
import com.example.myapplication.R;
import com.example.myapplication.Usermodel.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ChatFragment extends Fragment {
    private View view;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private RecyclerView recyclerView;
    private ClassAdapter classAdapter;
    private List<Users> mUsers;
    private FirebaseUser fUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container,false);
        recyclerView = view.findViewById(R.id.class_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        mUsers = new ArrayList<>();
        ReadUsers();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;

    }

    private void ReadUsers() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //DocumentReference doc_ref = db.collection("users").document("FbDChvuWe3PJnMrLGjJU");
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot q_snap = task.getResult();
                    Map<String, String> classes = new HashMap<>();
                    String cur_user = fUser.getEmail();
                    for (QueryDocumentSnapshot q_doc : q_snap) {
                        if (q_doc.get("email").equals(fUser.getEmail())) {
                            cur_user = fUser.getEmail();
                            classes = (Map<String, String>) q_doc.get("classes");
                            System.out.println("classes: "+classes);
                        }
                    }
                    Iterator it = classes.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        Users user = new Users(cur_user, pair.getValue().toString(), pair.getKey().toString());
                        mUsers.add(user);
                        System.out.println(pair.getKey() + "-------" + pair.getValue());
                        it.remove();
                    }
                    classAdapter = new ClassAdapter(getContext(), mUsers);
                    recyclerView.setAdapter(classAdapter);

                }
            }
        });
    }

}
