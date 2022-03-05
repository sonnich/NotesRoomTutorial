package com.example.notesroomtutorial.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesroomtutorial.Models.Notes;
import com.example.notesroomtutorial.R;

import java.util.List;

public class NotesListAdapter extends RecyclerView.Adapter<NotesViewHolder> {
    Context context;
    List<Notes> list;


    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

class NotesViewHolder extends RecyclerView.ViewHolder {


    CardView notes_container;
    TextView txt_title, txt_noteBody, txt_noteDate;
    ImageView img_pin;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        initViews();


    }


    private void initViews(){
        notes_container = itemView.findViewById(R.id.notes_container);
        txt_title = itemView.findViewById(R.id.txt_title);
        txt_noteBody = itemView.findViewById(R.id.txt_noteBody);
        txt_noteDate = itemView.findViewById(R.id.txt_noteDate);
        img_pin = itemView.findViewById(R.id.img_pin);
    }
}
