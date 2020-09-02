package com.nikhil.PasswordStoreModule;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nikhil.EnPass.R;

public class passviewholder extends RecyclerView.ViewHolder {

    public TextView username;
    public TextView sitename;
    public RelativeLayout root;
    public passviewholder(@NonNull View itemView) {
        super(itemView);
        root = itemView.findViewById(R.id.list_root);
        username = itemView.findViewById(R.id.textViewUserName);
        sitename = itemView.findViewById(R.id.textViewSite);
    }
}
