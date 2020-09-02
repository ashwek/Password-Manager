package com.nikhil.SecureNotesModule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nikhil.FirebaseFunction.Notestore;
import com.nikhil.Functions.AES;
import com.nikhil.EnPass.PassActivity;
import com.nikhil.EnPass.R;

public class NoteUpdateActivity extends AppCompatActivity {
    TextInputEditText title;
    EditText note;
    Button update;
    String id;
    FirebaseAuth auth;
    Notestore notestore;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_update);
        title = findViewById(R.id.NoteName);
        note = findViewById(R.id.note);
        update = findViewById(R.id.update);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        update.setOnClickListener(v -> {
            String newtitle = String.valueOf(title.getText());
            String newnote = String.valueOf(note.getText());
            if(newnote.length() == 0){
                note.setError("Secure note can't be empty");
            }
            else{
                try {
                    update(id,newtitle,newnote);
                } catch (Exception ignored) {
                }
            }

        });

        Bundle bundle = getIntent().getExtras();
        String notetitle = bundle.getString("title");
        title.setText(notetitle);
        String securenote = bundle.getString("note");
        note.setText(securenote);
        id = bundle.getString("id");
    }

    private void update(String id, String newtitle, String newnote) throws Exception {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notestore").child(user.getUid()).child(id);
        String enctitle = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,newtitle);
        String encnote = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,newnote);
        notestore = new Notestore(enctitle,encnote);
        databaseReference.setValue(notestore);
        Toast.makeText(this,"updated successfully",Toast.LENGTH_LONG).show();
        Intent i = new Intent(this,NotesDashActivity.class);
        startActivity(i);
        finish();

    }
}
