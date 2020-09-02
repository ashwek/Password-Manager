package com.nikhil.PasswordStoreModule;
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
import com.nikhil.FirebaseFunction.Passwordstore;
import com.nikhil.EnPass.PassActivity;
import com.nikhil.EnPass.R;

public class PasswordUpdateDeleteActivity extends AppCompatActivity {
    TextView Uname,Sname,Pass,Note;
    String id;
    FirebaseAuth auth;
    FirebaseUser user;
    int maxid;
    DatabaseReference reff;
    private DatabaseReference reference;
    private String newsname ,newuname,newpass,newnote;
    Passwordstore passwordstore;
    ImageView delete;
    boolean deleteRecord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_update_delete);
        Uname = findViewById(R.id.UsernameText);
        Sname = findViewById(R.id.SitenameText);
        Pass = findViewById(R.id.PasswordText);
        Note = findViewById(R.id.extranote);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        delete = findViewById(R.id.delete);
        Note.setMovementMethod(new ScrollingMovementMethod());

        delete.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(PasswordUpdateDeleteActivity.this);
            builder.setCancelable(true);
            builder.setTitle("Delete Password?");
            builder.setMessage("Data once deleted can't be retrieved");
            builder.setPositiveButton("Confirm",
                    (dialog, which) -> delete());
            builder.setNegativeButton("Cancel", (dialog, which) -> {
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        });
        
        showCheckDialog();


        Bundle bundle = getIntent().getExtras();
        String sname = bundle.getString("Sitename");
        Sname.setText(sname);
        String uname = bundle.getString("Username");
        Uname.setText(uname);
        String password = bundle.getString("Password");
        Pass.setText(password);
        String notes = bundle.getString("Notes");
        Note.setText(notes);
        id = bundle.getString("id");
        Toast.makeText(this,id,Toast.LENGTH_LONG).show();

    }

    private void delete() {
          deleteRecord = true;
                reff= FirebaseDatabase.getInstance().getReference().child("Passwordstore");
                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists() && deleteRecord) {
                            maxid = (int) dataSnapshot.child(user.getUid()).getChildrenCount();
                            Replacelast(maxid);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void Replacelast(int maxid) {
        final DatabaseReference DBcur = FirebaseDatabase.getInstance().getReference("Passwordstore").child(user.getUid()).child(String.valueOf(id));
        final DatabaseReference DBlast = FirebaseDatabase.getInstance().getReference("Passwordstore").child(user.getUid()).child(String.valueOf(maxid));
        reference = FirebaseDatabase.getInstance().getReference().child("Passwordstore").child(user.getUid()).child(String.valueOf(maxid));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if( !deleteRecord || dataSnapshot.getValue() == null )
                    return;

                String newsname1 = dataSnapshot.child("sname").getValue().toString();
                newsname = newsname1;
                String newuname1 = dataSnapshot.child("uname").getValue().toString();
                newuname = newuname1;
                String newpass1 = dataSnapshot.child("pass").getValue().toString();
                newpass = newpass1;
                String newnote1 = dataSnapshot.child("note").getValue().toString();
                newnote = newnote1;
                passwordstore =new Passwordstore(newsname,newuname,newpass,newnote);
                DBcur.setValue(passwordstore);
                DBlast.removeValue();
                deleteRecord = false;
                Toast.makeText(PasswordUpdateDeleteActivity.this,"Deleted successfully",Toast.LENGTH_LONG).show();
                Intent i = new Intent(PasswordUpdateDeleteActivity.this,PasswordDashActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    private void showCheckDialog() {
        final Dialog dialog = new Dialog(PasswordUpdateDeleteActivity.this,R.style.DialogTheme);
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
            Intent i = new Intent(PasswordUpdateDeleteActivity.this,PasswordDashActivity.class);
            startActivity(i);
            finish();
        });

    }

    public void backfunction(View view) {
        Intent i = new Intent(PasswordUpdateDeleteActivity.this,PasswordDashActivity.class);
        startActivity(i);
        finish();
    }

    public void update(View view) {
        Intent i =  new Intent(this,PasswordUpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Sitename", String.valueOf(Sname.getText()));
        bundle.putString("Username", String.valueOf(Uname.getText()));
        bundle.putString("Password", String.valueOf(Pass.getText()));
        bundle.putString("Notes", String.valueOf(Note.getText()));
        bundle.putString("id",id);
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }
    }
