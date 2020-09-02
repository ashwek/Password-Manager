package com.nikhil.PersonalDetailModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
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
import com.nikhil.FirebaseFunction.Personal;
import com.nikhil.EnPass.PassActivity;
import com.nikhil.EnPass.R;

public class PersonUpdateDeleteActivity extends AppCompatActivity {
    TextView title,First_name, Last_name, Nickname, Email, Website, Address, Mobile_num, Birth_date, Note;
    String id;
    ImageView delete;
    boolean deleteRecord;
    FirebaseAuth auth;
    FirebaseUser user;
    int maxid;
    DatabaseReference reff;
    Personal personal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_update_delete);
        title = findViewById(R.id.titleText);
        First_name = findViewById(R.id.First_NameText);
        Last_name = findViewById(R.id.Last_NameText);
        Nickname = findViewById(R.id.NicknameText);
        Email = findViewById(R.id.EmailText);
        Website = findViewById(R.id.WebsiteText);
        Address = findViewById(R.id.AddressText);
        Mobile_num = findViewById(R.id.Mobile_numText);
        Birth_date = findViewById(R.id.Birth_dateText);
        Note = findViewById(R.id.extranote);
        delete = findViewById(R.id.delete);
        Note.setMovementMethod(new ScrollingMovementMethod());
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        showCheckDialog();

        Bundle bundle = getIntent().getExtras();
        String Title = bundle.getString("title");
        title.setText(Title);
        String first_name = bundle.getString("firstname");
        First_name.setText(first_name);
        String last_name = bundle.getString("lastname");
        Last_name.setText(last_name);
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
        String note = bundle.getString("note");
        Note.setText(note);
        id = bundle.getString("id");

        delete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(PersonUpdateDeleteActivity.this);
            builder.setCancelable(true);
            builder.setTitle("Delete Personal Details?");
            builder.setMessage("Data once deleted can't be retrieved");
            builder.setPositiveButton("Confirm",
                    (dialogInterface, which) -> delete());
            builder.setNegativeButton("Cancel", (dialogInterface, which) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void delete() {
        deleteRecord = true;
        reff = FirebaseDatabase.getInstance().getReference().child("Personal");
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
        final DatabaseReference DBcur = FirebaseDatabase.getInstance().getReference("Personal").child(user.getUid()).child(String.valueOf(id));
        final DatabaseReference DBlast = FirebaseDatabase.getInstance().getReference("Personal").child(user.getUid()).child(String.valueOf(maxid));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Personal").child(user.getUid()).child(String.valueOf(maxid));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!deleteRecord || dataSnapshot.getValue() == null)
                    return;
                String title = dataSnapshot.child("title").getValue().toString();
                String first_name = dataSnapshot.child("firstname").getValue().toString();
                String lastname = dataSnapshot.child("lastname").getValue().toString();
                String nickname = dataSnapshot.child("nickname").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String website = dataSnapshot.child("website").getValue().toString();
                String address = dataSnapshot.child("address").getValue().toString();
                String mobile = dataSnapshot.child("mobile").getValue().toString();
                String birth = dataSnapshot.child("birth").getValue().toString();
                String exnote = dataSnapshot.child("note").getValue().toString();
                personal = new Personal(title,first_name, lastname, nickname, email, website, address, mobile, birth, exnote);
                DBcur.setValue(personal);
                DBlast.removeValue();
                deleteRecord = false;
                Toast.makeText(PersonUpdateDeleteActivity.this, "Deleted successfully", Toast.LENGTH_LONG).show();
                Intent i = new Intent(PersonUpdateDeleteActivity.this, PersonDashActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showCheckDialog() {
        final Dialog dialog = new Dialog(PersonUpdateDeleteActivity.this, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.show();
        dialog.setContentView(R.layout.mpassdialogbox);
        Button bt1 = dialog.findViewById(R.id.btn1);
        Button bt2 = dialog.findViewById(R.id.btn2);
        final TextInputLayout mpass = dialog.findViewById(R.id.mpassinp);


        bt1.setOnClickListener(v -> {
            final String mpassc = mpass.getEditText().getText().toString().trim();
            final String check = PassActivity.firebasemasterpassword;
            if (mpassc.equals(check)) {
                dialog.dismiss();
            } else {
                mpass.setError("Wrong Master Password");
            }
        });

        bt2.setOnClickListener(v -> {
            Intent i = new Intent(PersonUpdateDeleteActivity.this, PersonDashActivity.class);
            startActivity(i);
            finish();
        });
    }


    public void backfunction(View view) {
        Intent i = new Intent(PersonUpdateDeleteActivity.this, PersonDashActivity.class);
        startActivity(i);
        finish();
    }

    public void update(View view) {
        Intent i = new Intent(PersonUpdateDeleteActivity.this, PersonUpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title",String.valueOf(title.getText()));
        bundle.putString("firstname", String.valueOf(First_name.getText()));
        bundle.putString("lastname", String.valueOf(Last_name.getText()));
        bundle.putString("nickname", String.valueOf(Nickname.getText()));
        bundle.putString("email", String.valueOf(Email.getText()));
        bundle.putString("website", String.valueOf(Website.getText()));
        bundle.putString("address", String.valueOf(Address.getText()));
        bundle.putString("mobile", String.valueOf(Mobile_num.getText()));
        bundle.putString("birth", String.valueOf(Birth_date.getText()));
        bundle.putString("note", String.valueOf(Note.getText()));
        bundle.putString("id", id);
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }
}