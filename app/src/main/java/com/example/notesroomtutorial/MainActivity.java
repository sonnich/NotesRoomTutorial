package com.example.notesroomtutorial;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.LinearLayout;

import com.example.notesroomtutorial.Adapters.NotesListAdapter;
import com.example.notesroomtutorial.Database.RoomDB;
import com.example.notesroomtutorial.Models.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //TODO try and use dedicated thread for database call.
    private static final String TAG = "MainActivity";
    public static final int REQUEST_CODE_CREATE = 101;
    public static final int REQUEST_CODE_CLICKED = 102;
    public static final String INTENT_NOTE = "note";
    public static final String CLICKED_NOTE = "clicked_note";
    RecyclerView recyclerView;
    NotesListAdapter notesListAdapter;
    List<Notes> notes = new ArrayList<>();
    RoomDB database;
    FloatingActionButton fab_add;
    SearchView searchView_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recView_home);
        fab_add = findViewById(R.id.fab_add);
        searchView_home = findViewById(R.id.searchView_home);

        database = RoomDB.getInstance(this);
        notes = database.mainDao().getAll();

        updateRecycler(notes);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO use registerForActivityResult instead of deprecated startActivityForResult
                Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
                startActivityForResult(intent, REQUEST_CODE_CREATE);

            }
        });

        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;

            }

            @Override
            public boolean onQueryTextChange(String query) {
                filterQuery(query);
                return true;
            }
        });
    }

    //method for updating the query.
    public void filterQuery(String query){
        ArrayList<Notes> filteredList = new ArrayList<>();
        String search = query.toLowerCase();
        for (Notes candidate: notes){
            if(candidate.getTitle().toLowerCase().contains(search)
            ||candidate.getNotes().toLowerCase().contains(search)){
                filteredList.add(candidate);
            }
        }
       notesListAdapter.filterList(filteredList);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== REQUEST_CODE_CREATE){
            if(resultCode == RESULT_OK){
                Notes new_note = (Notes) data.getSerializableExtra(INTENT_NOTE);
                database.mainDao().insert(new_note);

                notes.clear();
                notes.addAll(database.mainDao().getAll());
                notesListAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == REQUEST_CODE_CLICKED) {
            if(resultCode==RESULT_OK){
                Notes new_notes = (Notes) data.getSerializableExtra(INTENT_NOTE);
                database.mainDao().upDate(new_notes.getID(), new_notes.getTitle(), new_notes.getNotes());
                notes.clear();
                notes.addAll(database.mainDao().getAll());
                notesListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycler(List<Notes> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));
        notesListAdapter = new NotesListAdapter(MainActivity.this, notes, notesClickListener);
        recyclerView.setAdapter(notesListAdapter);


    }

    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {
            Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
            intent.putExtra(CLICKED_NOTE, notes);

            startActivityForResult(intent, REQUEST_CODE_CLICKED);


        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {

        }
    };
}