package uk.ac.aston.cs3mdd.f1_tracker.ui.diary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import uk.ac.aston.cs3mdd.f1_tracker.R;
import uk.ac.aston.cs3mdd.f1_tracker.databinding.FragmentDiaryBinding;

public class DiaryFragment extends Fragment {

    private FragmentDiaryBinding binding;
    private NotesDatabase notesDatabase;
    private NotesAdapter notesAdapter;
    private SearchView searchView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDiaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.notesView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        notesDatabase = new NotesDatabase(getContext());
        List<NotesDatabaseModel> notes = notesDatabase.getAllNotes();

        notesAdapter = new NotesAdapter(notes, notesDatabase);
        recyclerView.setAdapter(notesAdapter);

        searchView = binding.searchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            //filtering the notes by title through the searchbar
            @Override
            public boolean onQueryTextChange(String newText) {
                List<NotesDatabaseModel> filteredNotes = notesDatabase.searchNotes(newText);
                if (filteredNotes != null) {
                    notesAdapter.updateData(filteredNotes);
                }
                return true;
            }
        });

        Button addButton = binding.addButton;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddNoteDialog();            }
        });
        return root;
    }
    private void showAddNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add New Note");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.note_dialog_popup, null);
        builder.setView(dialogView);

        TextInputEditText raceNameEditText = dialogView.findViewById(R.id.raceNameText);
        TextInputEditText raceDescriptionEditText = dialogView.findViewById(R.id.raceDescriptionText);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String raceName = raceNameEditText.getText().toString();
                String raceDescription = raceDescriptionEditText.getText().toString();

                NotesDatabaseModel newNote = new NotesDatabaseModel(raceName, raceDescription);
                notesDatabase.add(newNote);

                List<NotesDatabaseModel> updatedNotes = notesDatabase.getAllNotes();
                notesAdapter.updateData(updatedNotes);

                notesAdapter.notifyDataSetChanged();
                Toast.makeText(requireContext(), "New note added at the end", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Toast.makeText(requireContext(), "No note was added", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}