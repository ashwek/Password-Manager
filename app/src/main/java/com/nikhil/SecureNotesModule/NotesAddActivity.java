package com.nikhil.SecureNotesModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nikhil.FirebaseFunction.Notestore;
import com.nikhil.EnPass.PassActivity;
import com.nikhil.EnPass.R;
import com.nikhil.Functions.*;

public class NotesAddActivity extends AppCompatActivity {
    TextInputLayout title;
    EditText note;
    Button submit;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reff;
    Notestore notestore;
    int maxid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_add);
        title = findViewById(R.id.NoteName);
        note = findViewById(R.id.note);
        submit = findViewById(R.id.AddKey);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        notestore = new Notestore();
        reff= FirebaseDatabase.getInstance().getReference().child("Notestore");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    maxid = (int) dataSnapshot.child(user.getUid()).getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        submit.setOnClickListener(v -> {
            try {
                Addnote();
            } catch (Exception ignored) {
            }
        });

    }

    private void Addnote() throws Exception {

        String rawtitle =title.getEditText().getText().toString().trim();
        String enctitle = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawtitle);

        String rawnote = note.getText().toString().trim();
        String encnote = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawnote);
        if(rawtitle.length() == 0){
            title.setError("Title can't be empty");
        }
        else if(rawnote.length() == 0){
            note.setError("There is no note..");
        }
        else {
        notestore.setTitle(enctitle);
        notestore.setNote(encnote);
        reff.child(user.getUid()).child(String.valueOf(maxid+1)).setValue(notestore);
        Toast.makeText(NotesAddActivity.this,"data inserted successfully",Toast.LENGTH_LONG).show();
        Intent i = new Intent(NotesAddActivity.this,NotesDashActivity.class);
        startActivity(i);
        finish();}
    }
}
