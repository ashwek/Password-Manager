package com.nikhil.SecureNotesModule;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nikhil.EnPass.R;

public class notesviewholder extends RecyclerView.ViewHolder {
    public TextView notetitle;
    public RelativeLayout root;

    public notesviewholder(@NonNull View itemView) {
        super(itemView);
        notetitle = itemView.findViewById(R.id.textViewNoteTitleName);
        root = itemView.findViewById(R.id.note_root);
    }
}
