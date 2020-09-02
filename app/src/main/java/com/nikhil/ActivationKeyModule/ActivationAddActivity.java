package com.nikhil.ActivationKeyModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nikhil.FirebaseFunction.Activationstore;
import com.nikhil.Functions.AES;
import com.nikhil.EnPass.PassActivity;
import com.nikhil.EnPass.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ActivationAddActivity extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    TextInputLayout SoftName,Key;
    TextInputEditText Date;
    EditText Note;
    Button addbtn;
    DatabaseReference reff;
    Activationstore activationstore;
    FirebaseAuth auth;
    FirebaseUser user;
    int maxid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation_add);
        SoftName = findViewById(R.id.Name);
        Key =findViewById(R.id.key);
        Date = findViewById(R.id.ExpiryDateet);
        Note = findViewById(R.id.extranote);
        addbtn = findViewById(R.id.AddKey);
        activationstore =new Activationstore();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reff= FirebaseDatabase.getInstance().getReference().child("Activationstore");
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
        addbtn.setOnClickListener(v -> {
            try {
                AddActivationKey();
            } catch (Exception e) {
                e.printStackTrace();
            }


        });

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
        Date.setOnClickListener(v -> new DatePickerDialog(ActivationAddActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date.setText(sdf.format(myCalendar.getTime()));
    }

    private void AddActivationKey() throws Exception {
        //raw input string
        String rawsoftname = SoftName.getEditText().getText().toString().trim();
        String rawkey = Key.getEditText().getText().toString().trim();
        String rawdate = Date.getText().toString().trim();
        String rawnote = Note.getText().toString().trim();

        //encryption
        String encsoftname = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawsoftname);
        String enckey = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawkey);
        String encdate = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawdate);
        String encnote = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawnote);

        if(rawsoftname.equals("")){
            SoftName.setError("Software name field can't be empty!");
        }
        else if(rawkey.equals("")){
            Key.setError("Please enter your key!");
        }
        else if(rawdate.equals("")){
            Date.setError("You haven't entered date!");
        }
        else{
        //firebase upload
        activationstore.setSoftname(encsoftname);
        activationstore.setKey(enckey);
        activationstore.setDate(encdate);
        activationstore.setNote(encnote);
        reff.child(user.getUid()).child(String.valueOf(maxid+1)).setValue(activationstore);
        Toast.makeText(ActivationAddActivity.this,"data inserted successfully",Toast.LENGTH_LONG).show();
        Intent i = new Intent(ActivationAddActivity.this,ActivationDashActivity.class);
        startActivity(i);
        finish();}
    }
}
