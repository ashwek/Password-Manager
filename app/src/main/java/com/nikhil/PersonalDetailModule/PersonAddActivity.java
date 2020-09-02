package com.nikhil.PersonalDetailModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import com.nikhil.FirebaseFunction.Personal;
import com.nikhil.Functions.AES;
import com.nikhil.EnPass.PassActivity;
import com.nikhil.EnPass.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class PersonAddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {
    final Calendar myCalendar = Calendar.getInstance();
    TextInputLayout First_name,Last_name,Nickname,Email,Website,Address,Mobile_num;
    TextInputEditText Birth_date;
    Button Add;
    EditText extranote;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reff;
    int maxid;
    Personal personal;
    String title;
    private static final String[] paths = {"Select","Mr","Mrs.","Miss","Ms"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_add);
        First_name = findViewById(R.id.First_Name);
        Last_name =findViewById(R.id.Last_Name);
        Nickname = findViewById(R.id.Nickname);
        Email =findViewById(R.id.Email);
        Website =findViewById(R.id.Website);
        Address =findViewById(R.id.Address);
        extranote= findViewById(R.id.extranote);
        Birth_date=findViewById(R.id.Birth_dateet);
        Mobile_num=findViewById(R.id.Mobile_num);
        Add= findViewById(R.id.AddKey);
        personal=new Personal();

        Spinner spinner = findViewById(R.id.titleselect);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(PersonAddActivity.this,
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reff= FirebaseDatabase.getInstance().getReference().child("Personal");
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
        Add.setOnClickListener(v -> {
                    try {
                        add();
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


        Birth_date.setOnClickListener(v -> new DatePickerDialog(PersonAddActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());
        }


    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Birth_date.setText(sdf.format(myCalendar.getTime()));
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        switch (position) {
            case 0:
                title = "";
                break;
            case 1:
                title = paths[1];
                break;
            case 2:
                title = paths[2];
                break;

            case 3:
                title = paths[3];
                break;

            case 4:
                title = paths[4];
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void add() throws Exception {
        //raw string
        String  rawtitle = title.trim();
        String rawfname = First_name.getEditText().getText().toString().trim();
        String rawlname = Last_name.getEditText().getText().toString().trim();
        String rawnickname = Nickname.getEditText().getText().toString().trim();
        String rawemail = Email.getEditText().getText().toString().trim();
        String rawwebsite = Website.getEditText().getText().toString().trim();
        String rawaddr = Address.getEditText().getText().toString().trim();
        String rawmobnum = Mobile_num.getEditText().getText().toString().trim();
        String rawbdate = Birth_date.getText().toString().trim();
        String rawextranote = extranote.getText().toString().trim();

        //encryption
        String enctitle = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawtitle);
        String encfname = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawfname);
        String enclname = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawlname);
        String encnickname = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawnickname);
        String encemail = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawemail);
        String encwebsite = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawwebsite);
        String encaddr = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawaddr);
        String encmobnum = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawmobnum);
        String encdbate = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawbdate);
        String encnote = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawextranote);

        if(rawfname.equals("")){
            First_name.setError("Please check firstname");
        }
        else {
            personal.setTitle(enctitle);
            personal.setFirst_name(encfname);
            personal.setLast_name(enclname);
            personal.setNickname(encnickname);
            personal.setEmail(encemail);
            personal.setWebsite(encwebsite);
            personal.setAddress(encaddr);
            personal.setMobile_num(encmobnum);
            personal.setBirth_date(encdbate);
            personal.setExtranote(encnote);
            reff.child(user.getUid()).child(String.valueOf(maxid + 1)).setValue(personal);
            Toast.makeText(PersonAddActivity.this, "data inserted successfully", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, PersonDashActivity.class);
            startActivity(i);
            finish();
        }
    }

}

