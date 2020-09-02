package com.nikhil.PersonalDetailModule;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nikhil.EnPass.R;

public class personviewholder extends RecyclerView.ViewHolder {

    public TextView name;
    public RelativeLayout root;
    personviewholder(@NonNull View itemview){
        super(itemview);
        root=itemview.findViewById(R.id.person_root);
        name=itemview.findViewById(R.id.textView_name);
    }

}
