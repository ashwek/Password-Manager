package com.nikhil.SecureNotesModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.nikhil.FirebaseFunction.Notestore;
import com.nikhil.Functions.AES;
import com.nikhil.EnPass.PassActivity;
import com.nikhil.EnPass.R;

import java.util.Objects;

public class NotesDashActivity extends AppCompatActivity {
    FloatingActionButton submit;
    RecyclerView recyclerView;
    FloatingActionButton add;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Notestore, notesviewholder> adapter;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_dash);
        add = findViewById(R.id.Addnewnotes);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Notestore").child(user.getUid());


        recyclerView = findViewById(R.id.NotesrecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        showlist();

        add.setOnClickListener(v -> {
            Intent i = new Intent(NotesDashActivity.this,NotesAddActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void showlist() {
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Notestore>()
                .setQuery(reference, snapshot -> new Notestore(
                        Objects.requireNonNull(snapshot.child("title").getValue()).toString(),
                        Objects.requireNonNull(snapshot.child("note").getValue()).toString()))
                .build();
        adapter = new FirebaseRecyclerAdapter<Notestore, notesviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final notesviewholder notesviewholder, final int i, @NonNull final Notestore notestore) {
                String dectitle = null;
                String decnote = null;
                try {
                    dectitle = AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,notestore.getTitle());
                    decnote =  AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,notestore.getNote());
                    Log.d("dectitle",dectitle);
                    Log.d("decnote",decnote);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                notesviewholder.notetitle.setText(dectitle);

                String finalDectitle = dectitle;
                String finalDecnote = decnote;
                notesviewholder.root.setOnClickListener(view -> {
                    Bundle bundle= new Bundle();
                    bundle.putString("Notetitle", finalDectitle);
                    bundle.putString("note", finalDecnote);
                    bundle.putString("id", String.valueOf((notesviewholder.getLayoutPosition()+1)));
                    Intent i1 = new Intent(NotesDashActivity.this,NoteUpdateDeleteActivity.class);
                    i1.putExtras(bundle);
                    startActivity(i1);
                    finish();
                });
            }

            @NonNull
            @Override
            public notesviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.notelistlayout, parent, false);
                return new notesviewholder(view);
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
