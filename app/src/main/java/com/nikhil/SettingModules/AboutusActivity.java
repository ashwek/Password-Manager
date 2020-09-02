package com.nikhil.SettingModules;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.nikhil.EnPass.R;

public class AboutusActivity extends AppCompatActivity {
 WebView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        view = findViewById(R.id.aboutustv);
        String text;
        text = "<HTML><body><p align=\"justify\">";
        text+= "Password Protect is a data saving app created with the motive of providing a complete data security to its users. We are providing a free and open-source solution for your daily data management problems. We firmly believe that the entire control over the information is solely the right of their respective authenticated owner; henceforth we have introduced a unique solution for storing and retrieving your sensitive data. In case of any further assistance or issues, feel free to contact us. Thank you."+
                "<br><br><b>-Team Password Protect</b>";
        text+= "</p></body></html>";
        view.loadData(text, "text/html", "utf-8");
    }
}
