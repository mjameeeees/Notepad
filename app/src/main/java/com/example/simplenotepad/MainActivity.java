package com.example.simplenotepad;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private EditText editTextNote;
    private Button buttonSave;
    private TextView textViewMessage;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private List<String> noteList;
    private NoteAdapter adapter;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // EditText and Button for saving new notes
        editTextNote = findViewById(R.id.editTextNote);
        buttonSave = findViewById(R.id.buttonSave);

        // Load existing notes from the database
        noteList = loadAllNotes();

        // Set adapter for RecyclerView
        adapter = new NoteAdapter(noteList);
        recyclerView.setAdapter(adapter);

        // Save new note when the button is clicked
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNote = editTextNote.getText().toString();
                if (!newNote.isEmpty()) {
                    saveNoteToDatabase(newNote);
                    noteList.add(newNote);
                    adapter.notifyItemInserted(noteList.size() - 1); // Add the new note to the list
                    editTextNote.setText(""); // Clear the input field
                }
            }
        });

        // Load the saved note from the database (if any)

    }

    // Method to save the note to the SQLite database
    private void saveNoteToDatabase(String noteText) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NOTE, noteText);

        // Insert the note into the database
        db.insert(DatabaseHelper.TABLE_NOTES, null, values);
    }

    // Method to load the most recent note from the database
    private List<String> loadAllNotes() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<String> notesList = new ArrayList<>();

        Cursor cursor = db.query(DatabaseHelper.TABLE_NOTES,
                new String[]{DatabaseHelper.COLUMN_NOTE},
                null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String note = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NOTE));
                notesList.add(note);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return notesList;
    }



}