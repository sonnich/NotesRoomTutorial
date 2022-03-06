package com.example.notesroomtutorial.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesroomtutorial.Models.Notes;
import com.example.notesroomtutorial.NotesClickListener;
import com.example.notesroomtutorial.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesListAdapter extends RecyclerView.Adapter<NotesViewHolder> {
    Context context;
    List<Notes> list;
    //made the interface.
    NotesClickListener listener;

    public NotesListAdapter(Context context, List<Notes> list, NotesClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Notes notes = list.get(position);

        holder.txt_title.setText(notes.getTitle());
        holder.txt_title.setSelected(true);

        holder.txt_noteBody.setText(notes.getNotes());

        holder.txt_noteDate.setText(notes.getDate());
        holder.txt_noteDate.setSelected(true);



        if(notes.isPinned()){
            holder.img_pin.setImageResource(R.drawable.pin);
        }
        else{
            holder.img_pin.setImageResource(0);
        }

        int color = getRandomColor();
        holder.notes_container.setCardBackgroundColor(holder.itemView.getResources().getColor(color, null));

        holder.notes_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });

        holder.notes_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(list.get(holder.getAdapterPosition()),  holder.notes_container);
                return true;
            }
        });

    }

    private int getRandomColor(){
        ArrayList<Integer> colorCodes = new ArrayList<>();
        colorCodes.add(R.color.color1);
        colorCodes.add(R.color.color2);
        colorCodes.add(R.color.color3);
        colorCodes.add(R.color.color4);
        colorCodes.add(R.color.color5);
        Random random = new Random();
        int color = random.nextInt(colorCodes.size());
        return colorCodes.get(color);


    }

    @Override
    public int getItemCount() {
        return list.size();
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
