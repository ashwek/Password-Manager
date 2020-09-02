package com.nikhil.CreditCardModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nikhil.FirebaseFunction.Creditstore;
import com.nikhil.EnPass.PassActivity;
import com.nikhil.EnPass.R;

public class CreditUpdateDeleteActivity extends AppCompatActivity {
    TextView Accnum,Bankname,Cardnum,Expirydate,Cvv,Note;
    String id;
    ImageView delete;
    boolean deleteRecord;
    FirebaseAuth auth;
    FirebaseUser user;
    int maxid;
    DatabaseReference reff;
    private DatabaseReference reference;
    Creditstore creditstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_update_delete);

        Accnum = findViewById(R.id.AccNumText);
        Bankname = findViewById(R.id.BanknameText);
        Cardnum = findViewById(R.id.CardNumberText);
        Expirydate = findViewById(R.id.ExpiryDateText);
        Cvv = findViewById(R.id.CvvText);
        Note = findViewById(R.id.extranote);
        delete = findViewById(R.id.delete);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        Note.setMovementMethod(new ScrollingMovementMethod());

        showCheckDialog();

        Bundle bundle = getIntent().getExtras();
        String accnum = bundle.getString("accNumber");
        Accnum.setText(accnum);
        String bankname = bundle.getString("bankName");
        Bankname.setText(bankname);
        String cardnum = bundle.getString("cardNumber");
        Cardnum.setText(cardnum);
        String expdate = bundle.getString("expiryDate");
        Expirydate.setText(expdate);
        String cvv = bundle.getString("cvv");
        Cvv.setText(cvv);
        String note = bundle.getString("extranote");
        Note.setText(note);
        id = bundle.getString("id");

        delete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreditUpdateDeleteActivity.this);
            builder.setCancelable(true);
            builder.setTitle("Delete Card Details?");
            builder.setMessage("Data once deleted can't be retrieved");
            builder.setPositiveButton("Confirm",
                    (dialog, which) -> delete());
            builder.setNegativeButton("Cancel", (dialog, which) -> {
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        });
    }

    private void delete() {
        deleteRecord = true;
        reff= FirebaseDatabase.getInstance().getReference().child("Creditstore");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                maxid = (int) dataSnapshot.child(user.getUid()).getChildrenCount();
                Replacelast(maxid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Replacelast(int maxid) {
        final DatabaseReference DBcur = FirebaseDatabase.getInstance().getReference("Creditstore").child(user.getUid()).child(String.valueOf(id));
        final DatabaseReference DBlast = FirebaseDatabase.getInstance().getReference("Creditstore").child(user.getUid()).child(String.valueOf(maxid));
        reference = FirebaseDatabase.getInstance().getReference().child("Creditstore").child(user.getUid()).child(String.valueOf(maxid));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if( !deleteRecord || dataSnapshot.getValue() == null )
                    return;
                String accnum = dataSnapshot.child("accNumber").getValue().toString();
                String bankname = dataSnapshot.child("bankName").getValue().toString();
                String cardnum = dataSnapshot.child("cardNumber").getValue().toString();
                String cvv = dataSnapshot.child("cvv").getValue().toString();
                String expdate = dataSnapshot.child("expiryDate").getValue().toString();
                String exnote = dataSnapshot.child("extranote").getValue().toString();
                creditstore = new Creditstore(accnum,bankname,cardnum,expdate,cvv,exnote);
                DBcur.setValue(creditstore);
                DBlast.removeValue();
                deleteRecord = false;
                Toast.makeText(CreditUpdateDeleteActivity.this,"Deleted successfully",Toast.LENGTH_LONG).show();
                Intent i = new Intent(CreditUpdateDeleteActivity.this,CreditDashActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showCheckDialog() {

        final Dialog dialog = new Dialog(CreditUpdateDeleteActivity.this,R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.show();
        dialog.setContentView(R.layout.mpassdialogbox);
        Button bt1 = dialog.findViewById(R.id.btn1);
        Button bt2 =  dialog.findViewById(R.id.btn2);
        final TextInputLayout mpass = dialog.findViewById(R.id.mpassinp);



        bt1.setOnClickListener(v -> {
            final String mpassc = mpass.getEditText().getText().toString().trim();
            final String check = PassActivity.firebasemasterpassword;
            if (mpassc.equals(check)){
                dialog.dismiss();
            }
            else{
                mpass.setError("Wrong Master Password");
            }
        });

        bt2.setOnClickListener(v -> {
            Intent i = new Intent(CreditUpdateDeleteActivity.this,CreditDashActivity.class);
            startActivity(i);
            finish();
        });
    }

    public void backfunction(View view) {
        Intent i = new Intent(CreditUpdateDeleteActivity.this,CreditDashActivity.class);
        startActivity(i);
        finish();
    }

    public void update(View view) {
        Intent i = new Intent(CreditUpdateDeleteActivity.this,CreditUpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("accnum", String.valueOf(Accnum.getText()));
        bundle.putString("bankname", String.valueOf(Bankname.getText()));
        bundle.putString("cardnum", String.valueOf(Cardnum.getText()));
        bundle.putString("expdate", String.valueOf(Expirydate.getText()));
        bundle.putString("cvv", String.valueOf(Cvv.getText()));
        bundle.putString("note", String.valueOf(Note.getText()));
        bundle.putString("id",id);
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }
}
