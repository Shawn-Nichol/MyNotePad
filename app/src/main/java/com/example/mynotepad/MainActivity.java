package com.example.mynotepad;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.example.mynotepad.Adapter.NoteAdapter;
import com.example.mynotepad.Models.Note;
import com.example.mynotepad.Room.NoteRepository;
import com.example.mynotepad.Utility.RecyclerViewItemDecorator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NoteAdapter.NoteListener, // Interface for RecyclerView clicks.
        View.OnClickListener {

    private static final String TAG = "MainActivity";

    // UI
    private RecyclerView mRecyclerView;

    // Var
    private ArrayList<Note> mNotes = new ArrayList<>();
    private NoteAdapter mNotesAdapter;
    private NoteRepository mNoteRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Thread: " + Thread.currentThread().getName());

        mRecyclerView = findViewById(R.id.main_recycler_view);

        findViewById(R.id.fab).setOnClickListener(this);

        mNoteRepository = new NoteRepository(this);
        initRecyclerView();
        retrieveNotes();

        setSupportActionBar((Toolbar) findViewById(R.id.notes_toolbar));
        setTitle("Notes");
    }

    /**
     * Retrieve Notes database, any database calls using live data are asynchronous (runs on background Thread).
     */
    private void retrieveNotes() {
        Log.d(TAG, "retrieveNotes: ");
        mNoteRepository.retrieveNotesTask().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                if(mNotes.size() > 0 ) {
                    mNotes.clear();
                }

                if(notes != null) {
                    mNotes.addAll(notes);
                }
                mNotesAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Implements the RecyclerView.
     */
    private void initRecyclerView() {
        // The type of LayoutManager the RecyclerView will use.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // Link the LayoutManger to the RecyclerView.
        mRecyclerView.setLayoutManager(layoutManager);
        RecyclerViewItemDecorator itemDecorator = new RecyclerViewItemDecorator(10);
        // Add decoration for each item in the RecyclerView.
        mRecyclerView.addItemDecoration(itemDecorator);
        // This method will handle swipe action.
        new ItemTouchHelper (new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            /**
             * Called when ItemTouchHelper wants to move the dragged item from its old position to the
             * new position.
             *
             * @param recyclerView The RecyclerView to which ItemTouchHelper is attached to.
             * @param viewHolder The ViewHolder which is being dragged by the user.
             * @param target Teh ViewHolder over wich the currently active item is being dragged.
             * @return True if the viewHOlder has been moved to the adapter position of target
             */
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            /**
             * onSwiped, is called when a viewHolder is swiped by the user.
             *
             * @param viewHolder, the ViewHolder which has been swiped.
             * @param direction the direction the viewHolder is swiped.
             */
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Note note = mNotes.get(viewHolder.getAdapterPosition());
                Log.d(TAG, "onSwiped: delete note " + note);
                mNotes.remove(note);
                mNotesAdapter.notifyDataSetChanged();
                mNoteRepository.deleteNote(note);
            }
        }).attachToRecyclerView(mRecyclerView);

        // Pass context for interface.
        mNotesAdapter = new NoteAdapter(mNotes, this);
        // Link the adapter to the RecyclerView.
        mRecyclerView.setAdapter(mNotesAdapter);
    }

    /**
     * onNoteClick will create an Intent to start a new activity when an item in the RecyclerView is
     * clicked.
     *
     * @param position The position of the Item clicked in the RecyclerView.
     */
    @Override
    public void onNoteClick(int position) {
        Log.d(TAG, "onNoteClick: position " + position);

        Intent intent = new Intent(this, NoteActivity.class);
        // Extra will pass the Notes object to the new activity, the object must be a parcelable.
        intent.putExtra("selected_note", mNotes.get(position));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent intent = new Intent(this, NoteActivity.class);
                startActivity(intent);
                break;
        }
    }

}
