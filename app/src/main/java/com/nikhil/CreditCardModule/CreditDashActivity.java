package com.nikhil.CreditCardModule;

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
import com.nikhil.FirebaseFunction.Creditstore;
import com.nikhil.Functions.AES;
import com.nikhil.EnPass.PassActivity;
import com.nikhil.EnPass.R;

import java.util.Objects;

public class CreditDashActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Creditstore, Creditviewholder> adapter;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    FloatingActionButton Addcard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_dash);
        Addcard = findViewById(R.id.AddCredit);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Creditstore").child(user.getUid());

        recyclerView = findViewById(R.id.CreditrecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        showlist();

        Addcard.setOnClickListener(v -> {
            Intent in = new Intent(CreditDashActivity.this,CreditAddActivity.class);
            startActivity(in);
            finish();
        });
    }

    private void showlist() {

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Creditstore>()
                .setQuery(reference, snapshot -> new Creditstore(
                        Objects.requireNonNull(snapshot.child("accNumber").getValue()).toString(),
                        Objects.requireNonNull(snapshot.child("bankName").getValue()).toString(),
                        Objects.requireNonNull(snapshot.child("cardNumber").getValue()).toString(),
                        Objects.requireNonNull(snapshot.child("expiryDate").getValue()).toString(),
                        Objects.requireNonNull(snapshot.child("cvv").getValue()).toString(),
                        Objects.requireNonNull(snapshot.child("extranote").getValue()).toString()))
                .build();
        adapter = new FirebaseRecyclerAdapter<Creditstore, Creditviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final Creditviewholder creditviewholder, final int i, @NonNull final Creditstore creditstore) {
                String decaccnum = null;
                String decbankname = null;
                String deccarnum = null;
                String decexpdate = null;
                String deccvv = null ;
                String decnote = null;

                try {
                    decaccnum =  AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,creditstore.getAccNumber());
                    decbankname =  AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,creditstore.getBankName());
                    deccarnum =  AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,creditstore.getCardNumber());
                    decexpdate =  AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,creditstore.getExpiryDate());
                    deccvv =  AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,creditstore.getCvv());
                    decnote = AES.decryptStrAndFromBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,creditstore.getExtranote());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                creditviewholder.bankname.setText(decbankname);
                creditviewholder.accnum.setText(decaccnum);

                String finalDecaccnum = decaccnum;
                String finalDecbankname = decbankname;
                String finalDeccarnum = deccarnum;
                String finalDecexpdate = decexpdate;
                String finalDecnote = decnote;
                String finalDeccvv = deccvv;
                creditviewholder.root.setOnClickListener(view -> {
                    Bundle bundle= new Bundle();
                    bundle.putString("accNumber", finalDecaccnum);
                    bundle.putString("bankName", finalDecbankname);
                    bundle.putString("cardNumber", finalDeccarnum);
                    bundle.putString("expiryDate", finalDecexpdate);
                    bundle.putString("cvv", finalDeccvv);
                    bundle.putString("extranote", finalDecnote);
                    bundle.putString("id",String.valueOf((creditviewholder.getLayoutPosition()+1)));
                    Intent i1 = new Intent(CreditDashActivity.this,CreditUpdateDeleteActivity.class);
                    i1.putExtras(bundle);
                    startActivity(i1);
                    finish();
                });
            }

            @NonNull
            @Override
            public Creditviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.credlistlayout, parent, false);
                return new Creditviewholder(view);
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
