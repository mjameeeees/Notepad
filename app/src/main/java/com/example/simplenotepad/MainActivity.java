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

        EdgeToEdge.enable(this); // Enable edge-to-edge support (for system bar handling)
        setContentView(R.layout.activity_main);

        // Handle window insets for system bars (navigation bar and status bar)
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

        // Set the adapter for RecyclerView with the OnItemClickListener
        adapter = new NoteAdapter(noteList, new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemDelete(int position) {
                // Get the noteId from the note at the given position
                int noteId = getNoteIdFromPosition(position);

                // Delete from the database
                dbHelper.deleteNoteById(noteId);

                // Delete from the list and notify adapter
                noteList.remove(position);
                adapter.notifyItemRemoved(position);  // Notify adapter to remove item
            }
        });
        recyclerView.setAdapter(adapter);

        // Save new note when the button is clicked
        buttonSave.setOnClickListener(v -> {
            String newNote = editTextNote.getText().toString();
            if (!newNote.isEmpty()) {
                saveNoteToDatabase(newNote);  // Save the new note to the database
                noteList.add(newNote);  // Add the new note to the list
                adapter.notifyItemInserted(noteList.size() - 1);  // Notify the adapter that an item is inserted
                editTextNote.setText("");  // Clear the input field
            }
        });
    }


    private int getNoteIdFromPosition(int position) {
        // Assuming noteList contains notes as Strings, you should modify this if your notes have more data
        // Example: If noteList contains a list of Note objects, you can get the ID from there
        return position;  // This is just an example, modify according to your logic
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