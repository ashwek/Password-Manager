package com.nikhil.EnPass;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nikhil.EnPass.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            final EditTextPreference etp1 = findPreference("UserName");
            final EditTextPreference etp2 = findPreference("Loginpin");
            Preference contact = findPreference("contact");

            contact.setOnPreferenceClickListener(preference -> {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setMessage("For any further query and suggestion Contact us on:\nsemproject08@gmail.com");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "OK",
                        (dialog, id) -> dialog.cancel());

                AlertDialog alert11 = builder1.create();
                alert11.show();
                return true;
            });
            if (etp1 != null) {
                etp1.setOnPreferenceChangeListener((preference, newValue) -> {
                    String newname = newValue.toString().trim();
                    if(newname.length() != 0){
                        updatename(newname);
                        return true;
                    }
                    else
                        return false;
                });
            }

            if (etp2 != null) {
                etp2.setOnPreferenceChangeListener((preference, newValue) -> {
                    String newpin = newValue.toString().trim();
                    if(newpin.length() == 4){
                        updatepin(newpin);
                        return true;
                    }
                    else
                        return false;
                });
            }
        }

        private void updatepin(String newpin) {
            FirebaseAuth auth;
            FirebaseUser user;
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Detail").child(user.getUid());
            databaseReference.child("mpin").setValue(newpin);
        }

        private void updatename(String newname) {
            FirebaseAuth auth;
            FirebaseUser user;
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Detail").child(user.getUid());
            databaseReference.child("fname").setValue(newname);
        }


    }
}