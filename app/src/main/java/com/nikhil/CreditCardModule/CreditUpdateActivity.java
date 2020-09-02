package com.nikhil.CreditCardModule;

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
import com.nikhil.FirebaseFunction.Creditstore;
import com.nikhil.Functions.AES;
import com.nikhil.EnPass.PassActivity;
import com.nikhil.EnPass.R;

public class CreditUpdateActivity extends AppCompatActivity {
    TextInputEditText accnum,bankname,cardnum,expdate,cvv;
    EditText notes;
    Button update;
    String id;
    FirebaseAuth auth;
    Creditstore creditstore;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_update);
        accnum = findViewById(R.id.AccNumber);
        bankname = findViewById(R.id.BankName);
        cardnum = findViewById(R.id.CardNumber);
        expdate = findViewById(R.id.ExpiryDate);
        cvv = findViewById(R.id.Cvv);
        notes = findViewById(R.id.extranote);
        update = findViewById(R.id.UpdateKey);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        update.setOnClickListener(v -> {
            String newaccnum = String.valueOf(accnum.getText());
            String newbankname = String.valueOf(bankname.getText());
            String newcardnum = String.valueOf(cardnum.getText());
            String newexpdate = String.valueOf(expdate.getText());
            String newcvv = String.valueOf(cvv.getText());
            String newnote = String.valueOf(notes.getText());
            if(newcardnum.length() == 0){
                cardnum.setError("Not a valid card number");
            }
            else if(newexpdate.length() == 0){
                expdate.setError("Please enter expiry date");
            }
            else if(newcvv.length() == 0){
                cvv.setError("Enter CVV number");
            }
            else{
                try {
                    update(id,newaccnum,newbankname,newcardnum,newexpdate,newcvv,newnote);
                } catch (Exception ignored) {
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        String Accnum = bundle.getString("accnum");
        accnum.setText(Accnum);
        String Bankname = bundle.getString("bankname");
        bankname.setText(Bankname);
        String Cardnum = bundle.getString("cardnum");
        cardnum.setText(Cardnum);
        String Expdate = bundle.getString("expdate");
        expdate.setText(Expdate);
        String Cvv = bundle.getString("cvv");
        cvv.setText(Cvv);
        String Note = bundle.getString("note");
        notes.setText(Note);
        id = bundle.getString("id");
    }

    private void update(String id, String newaccnum, String newbankname, String newcardnum, String newexpdate, String newcvv, String newnote) throws Exception {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Creditstore").child(user.getUid()).child(id);
        String encaccnum = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,newaccnum);
        String encbankname = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,newbankname);
        String enccardnum = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,newcardnum);
        String encexpdate = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,newexpdate);
        String enccvv = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,newcvv);
        String encnote = AES.encryptStrAndToBase64(PassActivity.firebaserandomnumber,PassActivity.repeatedmsterpass,newnote);

        creditstore = new Creditstore(encaccnum,encbankname,enccardnum,encexpdate,enccvv,encnote);
        databaseReference.setValue(creditstore);
        Toast.makeText(this,"updated successfully",Toast.LENGTH_LONG).show();
        Intent i = new Intent(this,CreditDashActivity.class);
        startActivity(i);
        finish();
    }
}
