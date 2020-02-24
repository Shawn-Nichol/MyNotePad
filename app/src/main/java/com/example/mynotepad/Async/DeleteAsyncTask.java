package com.example.mynotepad.Async;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mynotepad.Models.Note;
import com.example.mynotepad.Room.NoteDao;

public class DeleteAsyncTask extends AsyncTask<Note, Void, Void> {

    private static final String TAG = "DeleteAsyncTask";
    private NoteDao mNoteDao;

    public DeleteAsyncTask(NoteDao dao) {
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... note) {
        Log.d(TAG, "doInBackground: thread " + Thread.currentThread().getName());
        mNoteDao.delete(note);
        return null;
    }
}
