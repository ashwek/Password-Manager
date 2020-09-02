package com.nikhil.PasswordStoreModule;

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
import com.nikhil.FirebaseFunction.Passwordstore;
import com.nikhil.Functions.AES;
import com.nikhil.EnPass.PassActivity;
import com.nikhil.EnPass.R;
public class PasswordAddActivity extends AppCompatActivity {
    TextInputLayout sname, uname, pass,cpass;
    EditText note;
    Button submit;
    DatabaseReference reff;
    Passwordstore passwordstore;
    FirebaseAuth auth;
    FirebaseUser user;
    int maxid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_add);
        sname = findViewById(R.id.Sitename);
        uname = findViewById(R.id.Username);
        pass = findViewById(R.id.Pass);
        cpass = findViewById(R.id.cPass);
        submit = findViewById(R.id.AddPass);
        note = findViewById(R.id.extranote);
        passwordstore =new Passwordstore();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reff= FirebaseDatabase.getInstance().getReference().child("Passwordstore");
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
                add();
            } catch (Exception ignored) {
            }
        });
    }

    private boolean validsname() {
        String name = sname.getEditText().getText().toString().trim();
        if (name.length() == 0) {
            sname.setError("Please enter WebSite name");
            return false;
        } else {
            sname.setError(null);
            return true;
        }
    }

    private boolean validuname() {
        String name = uname.getEditText().getText().toString().trim();
        if (name.length() == 0) {
            uname.setError("Please enter Username name");
            return false;
        } else {
            uname.setError(null);
            return true;
        }
    }

    private boolean validpass() {
        String pass1 = pass.getEditText().getText().toString();
        String pass2 = cpass.getEditText().getText().toString();
        if(pass1.length() <= 0){
            pass.setError("Password field can't be empty!");
            return false;
        }
        else if(pass2.length() <= 0){
            cpass.setError("Password field can't be empty!");
            return false;
        }
        else if (!(pass1.equals(pass2))){
            cpass.setError("both password not matched!!");
            return false;
        }
        else {
            pass.setError(null);
            cpass.setError(null);
            return true;
        }

    }

    private boolean add() throws Exception {
        if (validsname() && validuname() && validpass()){
            Toast.makeText(PasswordAddActivity.this,"data inserted successfully",Toast.LENGTH_LONG).show();
            //raw string
            String rawsname = sname.getEditText().getText().toString().trim();
            String rawuname = uname.getEditText().getText().toString().trim();
            String rawpass = pass.getEditText().getText().toString().trim();
            String rawnote = note.getText().toString().trim();

            //encryption
            String encsname = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawsname);
            String encuname = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawuname);
            String encpass = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawpass);
            String encnote = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawnote);

            passwordstore.setSname(encsname);
            passwordstore.setUname(encuname);
            passwordstore.setPass(encpass);
            passwordstore.setNote(encnote);
            reff.child(user.getUid()).child(String.valueOf(maxid+1)).setValue(passwordstore);
            Toast.makeText(PasswordAddActivity.this,"data inserted successfully",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(PasswordAddActivity.this, PasswordDashActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return false;
    }


}
