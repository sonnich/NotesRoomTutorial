package com.example.notesroomtutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.notesroomtutorial.Models.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateNoteActivity extends AppCompatActivity {
    private static final String TAG = "CreateNoteActivity";
    public static final String INTENT_NOTE = "note";
    public static final String CLICKED_NOTE = "clicked_note";


    private EditText edit_Title, edit_notes;
    private ImageView img_save;
    private Notes notes;
    private String title, body;
    boolean oldNote = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        initView();

        notes = new Notes();
        try {
            notes = (Notes) getIntent().getSerializableExtra(CLICKED_NOTE);
            edit_Title.setText(notes.getTitle());
            edit_notes.setText(notes.getNotes());
            oldNote = true;
        } catch (Exception e){
            e.printStackTrace();
        }




        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = edit_Title.getText().toString();
                body = edit_notes.getText().toString();

                if(!oldNote){
                    notes = new Notes();
                }

                if(fieldCheck()){
                    SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
                    Date date = new Date();
                    //notes = new Notes(title, body, formatter.format(date));

                    notes.setTitle(title);
                    notes.setNotes(body);
                    notes.setDate(formatter.format(date));

                    Intent intent = new Intent();
                    intent.putExtra(INTENT_NOTE, notes);
                    setResult(RESULT_OK, intent);
                    finish();

                }

            }
        });

    }

    private void initView() {
        edit_Title = findViewById(R.id.edit_Title);
        edit_notes = findViewById(R.id.edit_notes);
        img_save = findViewById(R.id.img_save);
    }

    private boolean fieldCheck(){
        if(TextUtils.isEmpty(title)){
            edit_Title.setError(getString(R.string.Title_error));
            return false;
        }
        if(TextUtils.isEmpty(body)){
            edit_notes.setError(getString(R.string.body_error));
            return false;
        }

        return true;
    }
}