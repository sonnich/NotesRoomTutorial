package com.example.notesroomtutorial;

import androidx.cardview.widget.CardView;

import com.example.notesroomtutorial.Models.Notes;

public interface NotesClickListener {
    void onClick(Notes notes);
    void onLongClick(Notes notes, CardView cardView);
}
