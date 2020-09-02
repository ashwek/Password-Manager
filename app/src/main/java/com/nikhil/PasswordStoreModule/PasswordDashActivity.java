package com.nikhil.PasswordStoreModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nikhil.FirebaseFunction.Passwordstore;
import com.nikhil.Functions.AES;
import com.nikhil.EnPass.PassActivity;
import com.nikhil.EnPass.R;

import java.util.Objects;


public class PasswordDashActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton add;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Passwordstore, passviewholder> adapter;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_dash);
        add = findViewById(R.id.AddPassButton);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Passwordstore").child(user.getUid());


        recyclerView = findViewById(R.id.PasswordrecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        showlist();


        add.setOnClickListener(v -> {
            Intent intent = new Intent(PasswordDashActivity.this, PasswordAddActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void showlist() {
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Passwordstore>()
                .setQuery(reference, snapshot -> new Passwordstore(
                        Objects.requireNonNull(snapshot.child("sname").getValue()).toString(),
                        Objects.requireNonNull(snapshot.child("uname").getValue()).toString(),
                        Objects.requireNonNull(snapshot.child("pass").getValue()).toString(),
                        Objects.requireNonNull(snapshot.child("note").getValue()).toString()))
                .build();
        adapter = new FirebaseRecyclerAdapter<Passwordstore, passviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final passviewholder passviewholder, final int i, @NonNull final Passwordstore passwordstore) {
                String decsname = null;
                String decuname = null;
                String decpass = null;
                String decnote = null;
                try {
                    decsname = AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,passwordstore.getSname());
                    decuname =  AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,passwordstore.getUname());;
                    decpass = AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,passwordstore.getPass());
                    decnote =  AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,passwordstore.getNote());
                } catch (Exception e) {
                    e.printStackTrace();
                }


                passviewholder.sitename.setText(decsname);
                passviewholder.username.setText(decuname);

                String finalDecsname = decsname;
                String finalDecuname = decuname;
                String finalDecpass = decpass;
                String finalDecnote = decnote;
                passviewholder.root.setOnClickListener(view -> {
                    Bundle bundle= new Bundle();
                    bundle.putString("Sitename", finalDecsname);
                    bundle.putString("Username", finalDecuname);
                    bundle.putString("Password", finalDecpass);
                    bundle.putString("Notes", finalDecnote);
                    bundle.putString("id", String.valueOf((passviewholder.getLayoutPosition()+1)));
                    Intent i1 = new Intent(PasswordDashActivity.this,PasswordUpdateDeleteActivity.class);
                    i1.putExtras(bundle);
                    startActivity(i1);
                    finish();
                });
            }

            @NonNull
            @Override
            public passviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.passlistlayout, parent, false);
                return new passviewholder(view);
            }
        };


        adapter.startListening();
        adapter.notifyDataSetChanged();

        try {
            recyclerView.setAdapter(adapter);
        }catch(Exception e
        ){}
    }

}