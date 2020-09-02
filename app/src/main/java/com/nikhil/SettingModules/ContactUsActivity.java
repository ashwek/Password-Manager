package com.nikhil.SettingModules;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nikhil.EnPass.R;

public class ContactUsActivity extends AppCompatActivity {
    EditText to,subject,msg;
    Button send;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        send = findViewById(R.id.btnOK);
        to = findViewById(R.id.txtTo);
        to.setText("semproject08@gmail.com");
        subject = findViewById(R.id.txtSubject);
        subject.setText("application feedback");
        msg = findViewById(R.id.txtMessage);
        send.setOnClickListener(v -> {
            String To = to.getText().toString();
            String sub = subject.getText().toString();
            String Msg = msg.getText().toString();
            Uri uri = Uri.parse("mailto:"+To)
                    .buildUpon()
                    .appendQueryParameter("To",To)
                    .appendQueryParameter("subject",sub)
                    .appendQueryParameter("body",Msg)
                    .build();
            Intent mail = new Intent(Intent.ACTION_SENDTO,uri);
            try {
                startActivity(Intent.createChooser(mail, "Send Email"));
                finish();
            } catch (Exception e) {
                Toast.makeText(ContactUsActivity.this,"No email client Found!!",Toast.LENGTH_LONG).show();
            }
        });
    }
}
