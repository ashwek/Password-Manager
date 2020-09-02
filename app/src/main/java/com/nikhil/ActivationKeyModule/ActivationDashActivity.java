package com.nikhil.ActivationKeyModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nikhil.FirebaseFunction.Activationstore;
import com.nikhil.Functions.AES;
import com.nikhil.EnPass.PassActivity;
import com.nikhil.EnPass.R;

import java.util.Objects;


public class ActivationDashActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Activationstore, activiewholder> adapter;
    FloatingActionButton add;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation_dash);

        add = findViewById(R.id.Activationaddbutton);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Activationstore").child(user.getUid());

        recyclerView = findViewById(R.id.ActivationrecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        showlist();


        add.setOnClickListener(v -> {
            Intent in = new Intent(ActivationDashActivity.this,ActivationAddActivity.class);
            startActivity(in);
            finish();
        });

    }

    private void showlist() {
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Activationstore>()
                .setQuery(reference, snapshot -> new Activationstore(
                        Objects.requireNonNull(snapshot.child("softname").getValue()).toString(),
                        Objects.requireNonNull(snapshot.child("key").getValue()).toString(),
                        Objects.requireNonNull(snapshot.child("date").getValue()).toString(),
                        Objects.requireNonNull(snapshot.child("note").getValue()).toString()))
                .build();

        adapter = new FirebaseRecyclerAdapter<Activationstore, activiewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final activiewholder activiewholder, final int i, @NonNull final Activationstore activationstore) {
                //decryption of raw string
               String decsoftname = null;
               String deckey = null;
               String decdate = null;
               String decnote = null;
                try {
                    decsoftname = AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,activationstore.getSoftname());
                    deckey = AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,activationstore.getKey());
                    decdate = AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,activationstore.getDate());
                    decnote = AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,activationstore.getNote());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                activiewholder.softname.setText(decsoftname);

                String finalDecsoftname = decsoftname;
                String finalDeckey = deckey;
                String finalDecdate = decdate;
                String finalDecnote = decnote;
                activiewholder.root.setOnClickListener(view -> {
                    Bundle bundle= new Bundle();
                    bundle.putString("Softname", finalDecsoftname);
                    bundle.putString("Key", finalDeckey);
                    bundle.putString("date", finalDecdate);
                    bundle.putString("Notes", finalDecnote);
                    bundle.putString("id",String.valueOf((activiewholder.getLayoutPosition()+1)));
                    Intent i1 = new Intent(ActivationDashActivity.this,ActivationUpdateDeleteActivity.class);
                    i1.putExtras(bundle);
                    startActivity(i1);
                    finish();

                });
            }

            @NonNull
            @Override
            public activiewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.actilistlayout, parent, false);
                return new activiewholder(view);
            }
        };


        adapter.startListening();
        adapter.notifyDataSetChanged();

        try {
            recyclerView.setAdapter(adapter);
        }catch(Exception ignored
        ){}
    }
}
