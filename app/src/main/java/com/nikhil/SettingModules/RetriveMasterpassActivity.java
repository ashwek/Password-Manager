package com.nikhil.SettingModules;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nikhil.SettingModules.javaapi.JavaMailAPI;
import com.nikhil.EnPass.PassActivity;
import com.nikhil.EnPass.R;

public class RetriveMasterpassActivity extends AppCompatActivity {
    TextView useremailtv,resendotp;
    EditText otpet;
    Button submitotp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrive_masterpass);
        otpet = findViewById(R.id.otpet);
        submitotp = findViewById(R.id.otpsubmitbtn);
        useremailtv = findViewById(R.id.useremailtv);
        resendotp = findViewById(R.id.resendotp);
        resendotp.setOnClickListener(v -> {
           sendmail();
        });
        String otp = sendmail();
        submitotp.setOnClickListener(v -> {
            String enteredotp = otpet.getText().toString().trim();
            if(enteredotp.equals(otp)){
                showpass();
            }
            else{
                Toast.makeText(RetriveMasterpassActivity.this,"Sorry pin didn't matched..\nTry again later!!!",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showpass() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(RetriveMasterpassActivity.this);
        builder1.setMessage("Your Master-Password is:\n"+ PassActivity.firebasemasterpassword);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "OK",
                (dialog, id) -> dialog.cancel());
        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    private String sendmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String otp = otpgenerator.getAlphaNumericString();
        String mail = user.getEmail();
        useremailtv.setText(mail);
        String message = "Your OTP for Master-Password Retrieval is:\n"+otp+"\nPlease do not disclose this OTP with anyone else.\n\nIf not requested by you then let us know at our E-mail Id:semproject08@gmail.com";
        String subject = "Password Manager OTP ";

        //Send Mail
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,mail,subject,message);
        javaMailAPI.execute();
       return otp;
    }

    public static class otpgenerator {
        static String getAlphaNumericString() {
            String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
            StringBuilder sb = new StringBuilder(8);
            for (int i = 0; i < 8; i++) {
                int index = (int) (AlphaNumericString.length() * Math.random());
                sb.append(AlphaNumericString.charAt(index));
            }
            return sb.toString();
        }
    }
}
