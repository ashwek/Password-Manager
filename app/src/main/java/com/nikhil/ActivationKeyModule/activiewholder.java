package com.nikhil.ActivationKeyModule;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nikhil.EnPass.R;

public class activiewholder extends RecyclerView.ViewHolder {
    public TextView softname;
    public RelativeLayout root;
    public activiewholder(@NonNull View itemView) {
        super(itemView);
        root = itemView.findViewById(R.id.root);
        softname = itemView.findViewById(R.id.textViewSoftName);
    }
}
