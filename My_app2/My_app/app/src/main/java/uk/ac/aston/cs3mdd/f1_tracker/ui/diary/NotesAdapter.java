package uk.ac.aston.cs3mdd.f1_tracker.ui.diary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uk.ac.aston.cs3mdd.f1_tracker.R; // Adjust this based on your package structure

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private static List<NotesDatabaseModel> notes;
    private static NotesDatabase notesDatabase;

    public NotesAdapter(List<NotesDatabaseModel> notes, NotesDatabase notesDatabase) {
        this.notes = notes;
        this.notesDatabase = notesDatabase;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item_layout, parent, false);
        return new NotesViewHolder(view,parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        NotesDatabaseModel note = notes.get(position);
        holder.bind(note);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void updateData(List<NotesDatabaseModel> updatedNotes) {
        this.notes = updatedNotes;
        notifyDataSetChanged();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {

        private final TextView raceName;
        private final TextView raceDescription;
        private Button deleteButton;
        private Button updateButton;
        private Button showButton;
        private Context context;

        public NotesViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;

            raceName = itemView.findViewById(R.id.raceNameTextView);
            raceDescription = itemView.findViewById(R.id.raceDescriptionTextView);

            deleteButton = itemView.findViewById(R.id.delete_button);
            updateButton = itemView.findViewById(R.id.edit_button);
            showButton = itemView.findViewById(R.id.show_button);

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        showEditPopup(notes.get(position));
                    }
                }
            });

            showButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        showNotePopup(notes.get(position));
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        NotesDatabaseModel note = notes.get(position);
                        String noteId = note.getId();

                        AlertDialog.Builder confirmDialogBuilder = new AlertDialog.Builder(context);
                        confirmDialogBuilder.setTitle("Delete");
                        confirmDialogBuilder.setMessage("Delete this note?");
                        confirmDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notesDatabase.delete(noteId);
                                notes.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, notes.size());
                                Log.d("NotesAdapter", "Note deleted successfully.");
                                Toast.makeText(context, "Note deleted successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                        confirmDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });

                        confirmDialogBuilder.create().show();
                    }
                }
            });
        }

        private void showEditPopup(NotesDatabaseModel note) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Edit Note");

            View editView = LayoutInflater.from(context).inflate(R.layout.note_dialog_popup, null);
            builder.setView(editView);

            EditText editRaceName = editView.findViewById(R.id.raceNameText);
            EditText editRaceDescription = editView.findViewById(R.id.raceDescriptionText);

            editRaceName.setText(note.getRaceName());
            editRaceDescription.setText(note.getRaceDescription());

            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String editedRaceName = editRaceName.getText().toString();
                    String editedRaceDescription = editRaceDescription.getText().toString();

                    note.setRaceName(editedRaceName);
                    note.setRaceDescription(editedRaceDescription);
                    notesDatabase.update(note);
                    notifyItemChanged(getAdapterPosition());
                    Toast.makeText(context, "Note edited successfully", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(context, "Note was not edited", Toast.LENGTH_SHORT).show();
                }
            });

            AlertDialog editDialog = builder.create();
            editDialog.show();
        }

        private void showNotePopup(NotesDatabaseModel note) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Show Note");

            View editView = LayoutInflater.from(context).inflate(R.layout.note_dialog_popup, null);
            builder.setView(editView);

            EditText showRaceName = editView.findViewById(R.id.raceNameText);
            EditText showRaceDescription = editView.findViewById(R.id.raceDescriptionText);

            showRaceName.setText(note.getRaceName());
            showRaceDescription.setText(note.getRaceDescription());

            showRaceName.setFocusable(false);
            showRaceName.setClickable(false);

            showRaceDescription.setFocusable(false);
            showRaceDescription.setClickable(false);

            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog editDialog = builder.create();
            editDialog.show();
        }

        public void bind(NotesDatabaseModel note) {
            raceName.setText(note.getRaceName());
            raceDescription.setText(note.getRaceDescription());
        }
    }
}