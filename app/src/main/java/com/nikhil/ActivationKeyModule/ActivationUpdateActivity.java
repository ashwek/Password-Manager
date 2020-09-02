
package com.nikhil.ActivationKeyModule;

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
import com.nikhil.FirebaseFunction.Activationstore;
import com.nikhil.Functions.AES;
import com.nikhil.EnPass.PassActivity;
import com.nikhil.EnPass.R;

public class ActivationUpdateActivity extends AppCompatActivity {
    TextInputEditText softname,key,expdate;
    EditText note;
    Button update;
    String id;
    FirebaseAuth auth;
    Activationstore activationstore;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation_update);
        softname = findViewById(R.id.Name);
        key = findViewById(R.id.Key);
        expdate = findViewById(R.id.ExpiryDate);
        note = findViewById(R.id.extranote);
        update = findViewById(R.id.UpdateKey);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newsoftname = String.valueOf(softname.getText());
                String newkey = String.valueOf(key.getText());
                String newexpdate = String.valueOf(expdate.getText());
                String newnote = String.valueOf(note.getText());
                if(newkey.length()==0){
                    key.setError("Please enter activation key");
                }
                else if(newexpdate.length()==0){
                    expdate.setError("Not a valid expiration date");
                }
                else{
                    try {
                        update(id,newsoftname,newkey,newexpdate,newnote);
                    } catch (Exception ignored) {

                    }
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        String sname = bundle.getString("sname");
        softname.setText(sname);
        String Akey = bundle.getString("Key");
        key.setText(Akey);
        String Expdate = bundle.getString("expdate");
        expdate.setText(Expdate);
        String exnote = bundle.getString("note");
        note.setText(exnote);
        id = bundle.getString("id");
    }

    private void update(String id, String softname, String key, String date, String note) throws Exception {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Activationstore").child(user.getUid()).child(id);
        String encsoftname = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,softname);
        String enckey = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,key);
        String encdate = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,date);
        String encnote = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,note);
        activationstore = new Activationstore(encsoftname,enckey,encdate,encnote);
        databaseReference.setValue(activationstore);
        Toast.makeText(this,"updated successfully",Toast.LENGTH_LONG).show();
        Intent i = new Intent(this,ActivationDashActivity.class);
        startActivity(i);
        finish();
    }
}
