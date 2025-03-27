package com.example.simplenotepad;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<String> notesList;
    private OnItemClickListener listener;


    public NoteAdapter(List<String> notesList, OnItemClickListener listener) {
        this.notesList = notesList;
        this.listener = listener;
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

        // Change the background color alternately
        if (position % 2 == 0) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#00fff9")); // Yellow for even positions
        } else {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#00b8ff")); // Blue for odd positions
        }

        holder.deleteButton.setOnClickListener(v -> {
            listener.onItemDelete(position);  // Trigger the delete action
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }


    public void deleteItem(int position) {
        // Remove item from the list
        notesList.remove(position);
        // Notify the adapter that the item is removed
        notifyItemRemoved(position);
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteTextView;
        Button deleteButton;
        LinearLayout linearLayout;

        public NoteViewHolder(View itemView) {
            super(itemView);
            noteTextView = itemView.findViewById(R.id.textNote); // Make sure this matches your layout
            linearLayout = itemView.findViewById(R.id.linearLayout);
            deleteButton = itemView.findViewById(R.id.deleteButton);  // Find the delete button

        }
    }
    public interface OnItemClickListener {
        void onItemDelete(int position);
    }
}
