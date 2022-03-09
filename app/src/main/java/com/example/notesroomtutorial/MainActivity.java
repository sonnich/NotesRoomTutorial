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
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.notesroomtutorial.Adapters.NotesListAdapter;
import com.example.notesroomtutorial.Database.RoomDB;
import com.example.notesroomtutorial.Models.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    //TODO try and use dedicated thread for database call.
    private static final String TAG = "MainActivity";
    public static final int REQUEST_CODE_CREATE = 101;
    public static final int REQUEST_CODE_CLICKED = 102;
    public static final String INTENT_NOTE = "note";
    public static final String CLICKED_NOTE = "clicked_note";
    private RecyclerView recyclerView;
    private NotesListAdapter notesListAdapter;
    private List<Notes> notes = new ArrayList<>();
    private RoomDB database;
    private FloatingActionButton fab_add;
    private SearchView searchView_home;
    private View root_view;
    private Notes selected_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recView_home);
        fab_add = findViewById(R.id.fab_add);
        searchView_home = findViewById(R.id.searchView_home);
        searchView_home.setQuery("", false);
        root_view = findViewById(R.id.root_view_main);
        root_view.requestFocus();

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
                updateUI();
            }
        } else if (requestCode == REQUEST_CODE_CLICKED) {
            if(resultCode==RESULT_OK){
                Notes new_notes = (Notes) data.getSerializableExtra(INTENT_NOTE);
                database.mainDao().upDate(new_notes.getID(), new_notes.getTitle(), new_notes.getNotes());
                updateUI();
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
            //TODO test if it's necessary to use constructor first.
            selected_note = new Notes();
            selected_note = notes;
            showPopup(cardView);


        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();

    }

    @Override
    protected void onResume(){
        super.onResume();
        root_view.requestFocus();
        //searchView_home.setQuery("", false);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.pin:
                if(selected_note.isPinned()){
                    database.mainDao().pin(selected_note.getID(), false);
                    Toast.makeText(this, "Unpinned", Toast.LENGTH_SHORT).show();
                } else {
                    database.mainDao().pin(selected_note.getID(), true);
                    Toast.makeText(this, "Pinned", Toast.LENGTH_SHORT).show();
                }
                updateUI();
                return true;
            case R.id.delete:
                database.mainDao().delete(selected_note);
                Toast.makeText(this, "Note Deleted", Toast.LENGTH_SHORT).show();
                notes.remove(selected_note);
                notesListAdapter.notifyDataSetChanged();
                return true;

        }
        return false;
    }

    private void updateUI(){
        notes.clear();
        notes.addAll(database.mainDao().getAll());
        notesListAdapter.notifyDataSetChanged();
    }
}