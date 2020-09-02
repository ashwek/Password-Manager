package com.nikhil.PersonalDetailModule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nikhil.CreditCardModule.CreditDashActivity;
import com.nikhil.FirebaseFunction.Personal;
import com.nikhil.Functions.AES;
import com.nikhil.EnPass.PassActivity;
import com.nikhil.EnPass.R;

public class PersonUpdateActivity extends AppCompatActivity {
    TextInputEditText First_name, Last_name, Nickname, Email, Website, Address, Mobile_num, Birth_date;
    EditText notes;
    Button update;
    String id;
    FirebaseAuth auth;
    Personal personal;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_update);
        First_name = findViewById(R.id.First_Name);
        Last_name = findViewById(R.id.Last_Name);
        Nickname = findViewById(R.id.Nickname);
        Email = findViewById(R.id.Email);
        Website = findViewById(R.id.Website);
        Address = findViewById(R.id.Address);
        Mobile_num = findViewById(R.id.Mobile_num);
        Birth_date = findViewById(R.id.Birth_date);
        notes = findViewById(R.id.extranote);
        update = findViewById(R.id.UpdateKey);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        //bundle extraction
        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("title");
        String firstname = bundle.getString("firstname");
        First_name.setText(firstname);
        String lastname = bundle.getString("lastname");
        Last_name.setText(lastname);
        String nickname = bundle.getString("nickname");
        Nickname.setText(nickname);
        String email = bundle.getString("email");
        Email.setText(email);
        String website = bundle.getString("website");
        Website.setText(website);
        String address = bundle.getString("address");
        Address.setText(address);
        String mobile = bundle.getString("mobile");
        Mobile_num.setText(mobile);
        String birth = bundle.getString("birth");
        Birth_date.setText(birth);
        String Note = bundle.getString("note");
        notes.setText(Note);
        id = bundle.getString("id");


        update.setOnClickListener(v -> {
            String newtitle = title;
            String newFirst_name = String.valueOf(First_name.getText());
            String newLast_name = String.valueOf(Last_name.getText());
            String newNickname = String.valueOf(Nickname.getText());
            String newEmail = String.valueOf(Email.getText());
            String newWebsite = String.valueOf(Website.getText());
            String newAddress = String.valueOf(Address.getText());
            String newMobile_num = String.valueOf(Mobile_num.getText());
            String newBirth_date = String.valueOf(Birth_date.getText());
            String newnotes = String.valueOf(notes.getText());
            try {
                update(id, newtitle,newFirst_name, newLast_name, newNickname, newEmail, newWebsite, newAddress, newMobile_num, newBirth_date, newnotes);
            } catch (Exception ignored) {
            }

        });
    }
    private void update (String id, String newtitle,String newFirst_name, String newLast_name, String
            newNickname, String newEmail, String newWebsite,
                         String newAddress, String newMobile_num, String newBirth_date, String newnotes) throws Exception {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Personal").child(user.getUid()).child(id);

        String enctitle = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,newtitle);
        String encfname = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,newFirst_name);
        String enclname = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,newLast_name);
        String encnickname = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,newNickname);
        String encemail = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,newEmail);
        String encwebsite = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,newWebsite);
        String encaddr = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,newAddress);
        String encmobnum = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,newMobile_num);
        String encbdate = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,newBirth_date);
        String encnote = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,newnotes);

        personal = new Personal(enctitle,encfname, enclname, encnickname, encemail, encwebsite, encaddr, encmobnum, encbdate, encnote);
        databaseReference.setValue(personal);
        Toast.makeText(this, "updated successfully", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, CreditDashActivity.class);
        startActivity(i);
        finish();

    }

}