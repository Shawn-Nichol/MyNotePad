package com.example.mynotepad.Async;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mynotepad.Models.Note;
import com.example.mynotepad.Room.NoteDao;

public class UpdateAsyncTask extends AsyncTask<Note, Void, Void> {

    private static final String TAG = "UpdateAsyncTask";

    private NoteDao mNoteDao;

    public UpdateAsyncTask(NoteDao mNoteDao) {
        this.mNoteDao = mNoteDao;
    }

    @Override
    protected Void doInBackground(Note... note) {
        Log.d(TAG, "doInBackground: ");
        mNoteDao.update(note);
        return null;
    }
}