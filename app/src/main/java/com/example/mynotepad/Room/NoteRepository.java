package com.example.mynotepad.Room;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.mynotepad.Async.DeleteAsyncTask;
import com.example.mynotepad.Async.InsertAsyncTask;
import com.example.mynotepad.Async.UpdateAsyncTask;
import com.example.mynotepad.Models.Note;

import java.util.List;

public class NoteRepository {
    private static final String TAG = "NoteRepository";
    private NoteDatabase mNoteDatabase;

    public NoteRepository(Context context) {
        mNoteDatabase = NoteDatabase.getInstance(context);
    }

    public void insertNoteTask(Note note) {
        Log.d(TAG, "insertNoteTask: ");
        new InsertAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }

    public void updateNote(Note note) {
        Log.d(TAG, "updateNote: ");
        new UpdateAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }

    public LiveData<List<Note>> retrieveNotesTask() {
        return mNoteDatabase.getNoteDao().getNotes();
    }

    public void deleteNote(Note note) {
        Log.d(TAG, "deleteNote: note " + note);
        new DeleteAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }
}
