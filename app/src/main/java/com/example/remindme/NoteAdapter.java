package com.example.remindme;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.*;
import java.util.*;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private List<Note> notes;
    private OnItemClickListener listener;

    public NoteAdapter(List<Note> notes) {
        this.notes = notes;
    }

    public void updateData(List<Note> newNotes) {
        this.notes = newNotes;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Note note);
        void onItemLongClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.content.setText(note.content);
        holder.timestamp.setText(DateFormat.getDateTimeInstance().format(new Date(note.timestamp)));

        holder.itemView.setOnClickListener(v -> {
            if(listener != null) listener.onItemClick(note);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if(listener != null) listener.onItemLongClick(note);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView content, timestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.note_content);
            timestamp = itemView.findViewById(R.id.note_timestamp);
        }
    }
}
