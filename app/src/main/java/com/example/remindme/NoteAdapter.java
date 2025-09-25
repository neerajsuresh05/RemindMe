package com.example.remindme;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.*;
import java.util.List;
import java.util.Date;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private List<Note> notes;

    public NoteAdapter(List<Note> notes) {
        this.notes = notes;
    }

    public void updateData(List<Note> newNotes) {
        this.notes = newNotes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.content.setText(note.content);

        Date date = new Date(note.timestamp);
        DateFormat df = DateFormat.getDateTimeInstance();
        holder.timestamp.setText(df.format(date));
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
