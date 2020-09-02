package com.nikhil.ActivationKeyModule;

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
import com.nikhil.FirebaseFunction.Activationstore;
import com.nikhil.EnPass.PassActivity;
import com.nikhil.EnPass.R;


public class ActivationUpdateDeleteActivity extends AppCompatActivity {
    TextView Softname,Key,ExpDate,Note;
    String id;
    ImageView delete;
    boolean deleteRecord;
    FirebaseAuth auth;
    FirebaseUser user;
    int maxid;
    DatabaseReference reff;
    private DatabaseReference reference;
    Activationstore activationstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation_update_delete);
        Softname = findViewById(R.id.SoftnameText);
        Key = findViewById(R.id.SoftkeyText);
        ExpDate = findViewById(R.id.ExpdateText);
        Note = findViewById(R.id.extranote);
        delete = findViewById(R.id.delete);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        Note.setMovementMethod(new ScrollingMovementMethod());

        showCheckDialog();

        Bundle bundle = getIntent().getExtras();
        String softname = bundle.getString("Softname");
        Softname.setText(softname);
        String key = bundle.getString("Key");
        Key.setText(key);
        String date = bundle.getString("date");
        ExpDate.setText(date);
        String note = bundle.getString("Notes");
        Note.setText(note);
        id = bundle.getString("id");

        delete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivationUpdateDeleteActivity.this);
            builder.setCancelable(true);
            builder.setTitle("Delete Activation Key?");
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
                reff= FirebaseDatabase.getInstance().getReference().child("Activationstore");
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
        final DatabaseReference DBcur = FirebaseDatabase.getInstance().getReference("Activationstore").child(user.getUid()).child(String.valueOf(id));
        final DatabaseReference DBlast = FirebaseDatabase.getInstance().getReference("Activationstore").child(user.getUid()).child(String.valueOf(maxid));
        reference = FirebaseDatabase.getInstance().getReference().child("Activationstore").child(user.getUid()).child(String.valueOf(maxid));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if( !deleteRecord || dataSnapshot.getValue() == null )
                    return;
                String softname = dataSnapshot.child("softname").getValue().toString();
                String key = dataSnapshot.child("key").getValue().toString();
                String date = dataSnapshot.child("date").getValue().toString();
                String note = dataSnapshot.child("note").getValue().toString();
                activationstore = new Activationstore(softname,key,date,note);
                DBcur.setValue(activationstore);
                DBlast.removeValue();
                deleteRecord = false;
                Toast.makeText(ActivationUpdateDeleteActivity.this,"Deleted successfully",Toast.LENGTH_LONG).show();
                Intent i = new Intent(ActivationUpdateDeleteActivity.this,ActivationDashActivity.class);
                startActivity(i);
                finish();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showCheckDialog() {

        final Dialog dialog = new Dialog(ActivationUpdateDeleteActivity.this,R.style.DialogTheme);
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
            Intent i = new Intent(ActivationUpdateDeleteActivity.this,ActivationDashActivity.class);
            startActivity(i);
            finish();
        });
    }

    public void backfunction(View view) {
        Intent i = new Intent(ActivationUpdateDeleteActivity.this,ActivationDashActivity.class);
        startActivity(i);
        finish();
    }

    public void update(View view) {
        Intent i = new Intent(ActivationUpdateDeleteActivity.this,ActivationUpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("sname", String.valueOf(Softname.getText()));
        bundle.putString("Key", String.valueOf(Key.getText()));
        bundle.putString("expdate", String.valueOf(ExpDate.getText()));
        bundle.putString("note", String.valueOf(Note.getText()));
        bundle.putString("id",id);
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }
}
