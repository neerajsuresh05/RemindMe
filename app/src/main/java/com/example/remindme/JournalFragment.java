package com.example.remindme;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.*;
import android.widget.*;
import android.content.Intent;
import java.util.List;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class JournalFragment extends Fragment {

    private JournalDatabaseHelper db;
    private NoteAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_journal, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.note_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = new JournalDatabaseHelper(getContext());
        List<Note> notes = db.getAllNotes();

        adapter = new NoteAdapter(notes);
        recyclerView.setAdapter(adapter);

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
