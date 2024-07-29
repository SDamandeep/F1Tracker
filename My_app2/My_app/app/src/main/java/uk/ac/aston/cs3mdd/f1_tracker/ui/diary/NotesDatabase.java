package uk.ac.aston.cs3mdd.f1_tracker.ui.diary;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NotesDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NOTES = "notes";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_RACE_NAME = "race_name";
    private static final String COLUMN_RACE_DESCRIPTION = "race_description";
    private final Context context;

    public NotesDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void add(NotesDatabaseModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RACE_NAME, model.getRaceName());
        values.put(COLUMN_RACE_DESCRIPTION, model.getRaceDescription());

        db.insert(TABLE_NOTES, null, values);
        db.close();
    }


    @SuppressLint("Range")
    public List<NotesDatabaseModel> getAllNotes() {
        List<NotesDatabaseModel> notesList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NOTES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                NotesDatabaseModel note = new NotesDatabaseModel();
                note.setId(cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                note.setRaceName(cursor.getString(cursor.getColumnIndex(COLUMN_RACE_NAME)));
                note.setRaceDescription(cursor.getString(cursor.getColumnIndex(COLUMN_RACE_DESCRIPTION)));

                notesList.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return notesList;
    }

    public void delete(String noteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, COLUMN_ID + " = ?", new String[]{noteId});
        db.close();
    }

    public void update(NotesDatabaseModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RACE_NAME, model.getRaceName());
        values.put(COLUMN_RACE_DESCRIPTION, model.getRaceDescription());

        db.update(TABLE_NOTES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(model.getId())});
        db.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TABLE_NOTES + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_RACE_NAME + " TEXT, " +
                        COLUMN_RACE_DESCRIPTION + " TEXT);";
        db.execSQL(query);
    }

    @SuppressLint("Range")
    public List<NotesDatabaseModel> searchNotes(String query) {
        List<NotesDatabaseModel> notesList = new ArrayList<>();
        String selection = COLUMN_RACE_NAME + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%"};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NOTES,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                NotesDatabaseModel note = new NotesDatabaseModel();
                note.setId(cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                note.setRaceName(cursor.getString(cursor.getColumnIndex(COLUMN_RACE_NAME)));
                note.setRaceDescription(cursor.getString(cursor.getColumnIndex(COLUMN_RACE_DESCRIPTION)));

                notesList.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return notesList;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

}
