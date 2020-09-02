package com.nikhil.CreditCardModule;

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
import com.nikhil.FirebaseFunction.Creditstore;
import com.nikhil.Functions.AES;
import com.nikhil.EnPass.PassActivity;
import com.nikhil.EnPass.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class CreditAddActivity extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    TextInputLayout accNumber,bankName,cardNumber,cvv;
    TextInputEditText expiryDate;
    Button add;
    EditText extranote;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reff;
    int maxid;
    Creditstore creditstore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_add);
        accNumber = findViewById(R.id.AccNumber);
        bankName =findViewById(R.id.BankName);
        cardNumber = findViewById(R.id.CardNumber);
        extranote= findViewById(R.id.extranote);
        expiryDate=findViewById(R.id.ExpiryDateet);
        cvv=findViewById(R.id.Cvv);
        add = findViewById(R.id.AddKey);
        creditstore =new Creditstore();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reff= FirebaseDatabase.getInstance().getReference().child("Creditstore");
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
        add.setOnClickListener(v -> {
            try {
                AddActivationKey();
            } catch (Exception ignored) {
            }
        });

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        expiryDate.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(CreditAddActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

    }


    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        expiryDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void AddActivationKey() throws Exception {
        //raw string
        String rawaccnum = accNumber.getEditText().getText().toString().trim();
        String rawbankname = bankName.getEditText().getText().toString().trim();
        String rawcardnum = cardNumber.getEditText().getText().toString().trim() ;
        String rawexpirydate = expiryDate.getText().toString().trim();
        String rawcvv = cvv.getEditText().getText().toString().trim();
        String rawextranote = extranote.getText().toString().trim();

        //encryption
        String encaccnum = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawaccnum);
        String encbankname = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawbankname);
        String enccardnum = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawcardnum);
        String encexpdate = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawexpirydate);
        String enccvv = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawcvv);
        String encnote = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,rawextranote);

        if(rawaccnum.equals("")){
            accNumber.setError("Please Enter Account number!");
        }
        else if(rawbankname.equals("")){
            bankName.setError("Bank name required!");
        }
        else if(rawcardnum.equals("")){
            cardNumber.setError("Enter card number!");
        }
        else if(rawexpirydate.equals("")){
            expiryDate.setError("Enter expiry date");
        }
        else if(rawcvv.length() != 3 ){
            cvv.setError("Enter 3 digit cvv number");
        }
        else {
            creditstore.setAccNumber(encaccnum);
            creditstore.setBankName(encbankname);
            creditstore.setCardNumber(enccardnum);
            creditstore.setExpiryDate(encexpdate);
            creditstore.setCvv(enccvv);
            creditstore.setExtranote(encnote);
            reff.child(user.getUid()).child(String.valueOf(maxid + 1)).setValue(creditstore);
            Toast.makeText(CreditAddActivity.this, "data inserted successfully", Toast.LENGTH_LONG).show();
            Intent i = new Intent(CreditAddActivity.this, CreditDashActivity.class);
            startActivity(i);
            finish();
        }
    }
}
