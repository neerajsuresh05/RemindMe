package com.example.remindme;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import java.util.List;

public class JournalFragment extends Fragment {

    private JournalDatabaseHelper db;
    private NoteAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_journal, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.note_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = new JournalDatabaseHelper(getContext());
        adapter = new NoteAdapter(db.getAllNotes());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(getActivity(), AddNoteActivity.class);
                intent.putExtra("noteId", note.id); // edit mode
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(Note note) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Note")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            db.deleteNote(note.id);
                            adapter.updateData(db.getAllNotes());
                            Toast.makeText(getContext(), "Note deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        FloatingActionButton fab = root.findViewById(R.id.fab_add_note);
        fab.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddNoteActivity.class)));

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.updateData(db.getAllNotes());
    }
}
