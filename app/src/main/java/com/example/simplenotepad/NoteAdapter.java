package com.example.simplenotepad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<String> notesList;

    public NoteAdapter(List<String> notesList) {
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemnote, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        holder.noteTextView.setText(notesList.get(position));
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteTextView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            noteTextView = itemView.findViewById(R.id.textNote); // Make sure this matches your layout
        }
    }
}
