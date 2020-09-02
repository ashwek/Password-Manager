package com.nikhil.PersonalDetailModule;

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
import com.nikhil.FirebaseFunction.Personal;
import com.nikhil.Functions.AES;
import com.nikhil.EnPass.PassActivity;
import com.nikhil.EnPass.R;

import java.util.Objects;

public class PersonDashActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton adddetail;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Personal,personviewholder> adapter;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_dash);
        adddetail = findViewById(R.id.Addpersondetail);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Personal").child(user.getUid());


        recyclerView = findViewById(R.id.PersonalrecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        showlist();

        adddetail.setOnClickListener(v -> {
            Intent i = new Intent(PersonDashActivity.this,PersonAddActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void showlist() {
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Personal>()
                .setQuery(reference, snapshot -> new Personal(
                        Objects.requireNonNull(snapshot.child("title").getValue()).toString(),
                        Objects.requireNonNull(snapshot.child("first_name").getValue()).toString(),
                        Objects.requireNonNull(snapshot.child("last_name").getValue()).toString(),
                        Objects.requireNonNull(snapshot.child("nickname").getValue()).toString(),
                        Objects.requireNonNull(snapshot.child("email").getValue()).toString(),
                        Objects.requireNonNull(snapshot.child("website").getValue()).toString(),
                        Objects.requireNonNull(snapshot.child("address").getValue()).toString(),
                        Objects.requireNonNull(snapshot.child("mobile_num").getValue()).toString(),
                        Objects.requireNonNull(snapshot.child("birth_date").getValue()).toString(),
                        Objects.requireNonNull(snapshot.child("extranote").getValue()).toString()
                ))
                .build();
        adapter=new FirebaseRecyclerAdapter<Personal, personviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final personviewholder personviewholder,final int i, @NonNull final Personal personal) {
                String dectitle = null;
                String decfname = null;
                String declname = null;
                String decnickname = null;
                String decemail = null;
                String decwebsite =null;
                String decaddr = null;
                String decmobnum = null;
                String decbdate = null ;
                String decnote = null;

                try {
                    dectitle = AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,personal.getTitle()).trim();
                    decfname =  AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,personal.getFirst_name()).trim();
                    declname =  AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,personal.getLast_name()).trim();
                    decnickname =  AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,personal.getNickname()).trim();
                    decemail =  AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,personal.getEmail()).trim();
                    decwebsite =  AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,personal.getWebsite()).trim();
                    decaddr =  AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,personal.getAddress()).trim();
                    decmobnum =  AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,personal.getMobile_num()).trim();
                    decbdate =  AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,personal.getBirth_date()).trim();
                    decnote = AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,personal.getExtranote()).trim();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String compname = dectitle+" " + Character.toString(decfname.charAt(0)).toUpperCase()+decfname.substring(1) +" "+ Character.toString(declname.charAt(0)).toUpperCase()+declname.substring(1);
                personviewholder.name.setText(compname);
                String finalDectitle = dectitle;
                String finalDecfname = decfname;
                String finalDeclname = declname;
                String finalDecnickname = decnickname;
                String finalDecemail = decemail;
                String finalDecwebsite = decwebsite;
                String finalDecaddr = decaddr;
                String finalDecmobnum = decmobnum;
                String finalDecbdate = decbdate;
                String finalDecnote = decnote;
                personviewholder.root.setOnClickListener(view -> {
                    Bundle bundle=new Bundle();
                    bundle.putString("title", finalDectitle);
                    bundle.putString("firstname", finalDecfname);
                    bundle.putString("lastname", finalDeclname);
                    bundle.putString("nickname", finalDecnickname);
                    bundle.putString("email", finalDecemail);
                    bundle.putString("website", finalDecwebsite);
                    bundle.putString("address", finalDecaddr);
                    bundle.putString("mobile", finalDecmobnum);
                    bundle.putString("birth", finalDecbdate);
                    bundle.putString("note", finalDecnote);
                    bundle.putString("id",String.valueOf((personviewholder.getLayoutPosition()+1)));
                    Intent i1 =new Intent(PersonDashActivity.this,PersonUpdateDeleteActivity.class);
                    i1.putExtras(bundle);
                    startActivity(i1);
                    finish();
                });

            }

            @NonNull
            @Override
            public personviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.personlistlayout, parent, false);
                return new personviewholder(view);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();

        try{
            recyclerView.setAdapter(adapter);
        }catch (Exception ignored)
        {}
    }
}
