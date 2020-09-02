package com.nikhil.PasswordStoreModule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nikhil.FirebaseFunction.Passwordstore;
import com.nikhil.Functions.AES;
import com.nikhil.EnPass.PassActivity;
import com.nikhil.EnPass.R;

public class PasswordUpdateActivity extends AppCompatActivity {
    TextInputEditText sitename,Username,pass,cpass;
    EditText extranote;
    Button update;
    String id;
    FirebaseAuth auth;
    Passwordstore passwordstore;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_update);
        sitename = findViewById(R.id.Sitename);
        Username = findViewById(R.id.Username);
        pass = findViewById(R.id.Pass);
        cpass = findViewById(R.id.cPass);
        extranote = findViewById(R.id.extranote);
        update = findViewById(R.id.updatepass);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newSname = String.valueOf(sitename.getText());
                String newUsername = String.valueOf(Username.getText());
                String newPass = String.valueOf(pass.getText());
                String newcPasss = String.valueOf(cpass.getText());
                String newextranote = String.valueOf(extranote.getText());
                if(!newPass.equals(newcPasss)){
                    cpass.setError("Password doesn't match");
                }
                    else{
                    try {
                        update(id,newSname,newUsername,newPass,newextranote);
                    } catch (Exception ignored) {
                    }
                }
                }
        });

        Bundle bundle = getIntent().getExtras();
        String sname = bundle.getString("Sitename");
        sitename.setText(sname);
        String uname = bundle.getString("Username");
        Username.setText(uname);
        String password = bundle.getString("Password");
        pass.setText(password);
        cpass.setText(password);
        String note = bundle.getString("Notes");
        extranote.setText(note);
        id = bundle.getString("id");
        Toast.makeText(this,id,Toast.LENGTH_LONG).show();
    }

    private void update(String id,String sname,String uname,String pass,String note) throws Exception {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Passwordstore").child(user.getUid()).child(id);
        String encsname = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,sname);
        String encuname = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,uname);
        String encpass = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,pass);
        String encnote = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,note);

        passwordstore =new Passwordstore(encsname,encuname,encpass,encnote);
        databaseReference.setValue(passwordstore);
        Toast.makeText(this,"updated successfully",Toast.LENGTH_LONG).show();
        Intent i = new Intent(PasswordUpdateActivity.this,PasswordDashActivity.class);
        startActivity(i);
        finish();
    }

}
