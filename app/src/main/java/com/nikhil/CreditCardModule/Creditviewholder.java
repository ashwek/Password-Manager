package com.nikhil.CreditCardModule;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nikhil.EnPass.R;

public class Creditviewholder extends RecyclerView.ViewHolder {
    TextView bankname,accnum;
    public RelativeLayout root;
    public Creditviewholder(@NonNull View itemView) {
        super(itemView);
        bankname = itemView.findViewById(R.id.textViewbankname);
        accnum = itemView.findViewById(R.id.textViewaccnum);
        root = itemView.findViewById(R.id.credit_root);
    }
}
