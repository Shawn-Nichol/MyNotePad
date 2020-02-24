package com.example.mynotepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mynotepad.Models.Note;
import com.example.mynotepad.Room.NoteRepository;
import com.example.mynotepad.Utility.LineEditText;
import com.example.mynotepad.Utility.Timestamp;


public class NoteActivity extends AppCompatActivity
        implements View.OnTouchListener, // Interface definition for a callback to be invoked when a touch event is dispatched to this view.
        GestureDetector.OnGestureListener, // Used to notify when Gestures occur.
        GestureDetector.OnDoubleTapListener, // Used for double tap action.
        View.OnClickListener,  // Interface for a callback to be invoked when a view is clicked.
        TextWatcher { // When an object of this type is attached to an Editable, its methods will be called when the text changes.

    private static final String TAG = "NoteActivity";
    private static final int EDIT_MODE_ENABLED = 1;
    private static final int EDIT_MODE_DISABLED = 0;

    // UI components
    private LineEditText mLineEditText;
    private EditText mEditTitle;
    private TextView mViewTitle;
    private RelativeLayout mCheckContainer, mBackArrowContainer;
    private ImageButton mCheck, mBackArrow;

    // vars
    private boolean mIsNewNote;
    private Note mInitialNote, mFinalNote;
    private GestureDetector mGestureDetector;
    private int mMode;
    private NoteRepository mNoteRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mLineEditText = findViewById(R.id.note_text);
        mEditTitle = findViewById(R.id.note_edit_title);
        mViewTitle = findViewById(R.id.note_text_title);
        mCheckContainer = findViewById(R.id.notes_check_container);
        mBackArrowContainer = findViewById(R.id.notes_back_arrow_container);
        mCheck = findViewById(R.id.notes_check);
        mBackArrow = findViewById(R.id.notes_back_arrow);

        mNoteRepository = new NoteRepository(this);

        if(getIncomingIntent()) {
            Log.d(TAG, "onCreate: new Note");

            // set details for existing note.
            mViewTitle.setText("NoteTitle");
            mEditTitle.setText("NoteTitle");

            mInitialNote = new Note();
            mFinalNote = new Note();
            mInitialNote.setTitle("NoteTitle");
            mFinalNote.setTitle("NoteTitle");

            enableEditMode();
        } else {
            Log.d(TAG, "onCreate: existing Note");

            // set details for new note.
            mViewTitle.setText(mInitialNote.getTitle());
            mEditTitle.setText(mInitialNote.getTitle());
            mLineEditText.setText(mInitialNote.getContent());

            disableContentInteraction();
        }

        setListeners();
    }

    /**
     * Changes the view ov the BackArrowContainer and the CheckContainer so the note can be edited.
     */
    private void enableEditMode() {
        Log.d(TAG, "enableEditMode: ");

        mBackArrowContainer.setVisibility(View.GONE);
        mCheckContainer.setVisibility(View.VISIBLE);

        mViewTitle.setVisibility(View.GONE);
        mEditTitle.setVisibility(View.VISIBLE);

        mMode = EDIT_MODE_ENABLED;

        enableContentInteraction();
    }

    /**
     * Changes the view of the BackArrowContainer and the CheckContainer so note can't be edited.
     */
    private void disableEditMode() {
        Log.d(TAG, "disableEditMode: ");

        mBackArrowContainer.setVisibility(View.VISIBLE);
        mCheckContainer.setVisibility(View.GONE);

        mViewTitle.setVisibility(View.VISIBLE);
        mEditTitle.setVisibility(View.GONE);

        mMode = EDIT_MODE_DISABLED;

        disableContentInteraction();

        String temp = mLineEditText.getText().toString();
        temp = temp.replace("\n", "");
        temp.replace(" ", "");
        if(temp.length() > 0) {
            mFinalNote.setTitle(mEditTitle.getText().toString());
            mFinalNote.setContent(mLineEditText.getText().toString());
            String timestamp = Timestamp.getTimestamp();
            mFinalNote.setTimeStamp(timestamp);

            if(!mFinalNote.getContent().equals(mInitialNote.getContent()) ||
                    !mFinalNote.getTitle().equals(mInitialNote.getTitle())){
                saveChanges();
            }
        }

    }

    /**
     * disable line interaction with edit text.
     */
    private void disableContentInteraction() {
        Log.d(TAG, "disableContentInteraction: ");

        // Set the Key listener to null.
        mLineEditText.setKeyListener(null);
        // This View is no longer focusable.
        mLineEditText.setFocusable(false);
        // This view not Focusable.
        mLineEditText.setFocusableInTouchMode(false);
        // Cursor is not visible.
        mLineEditText.setCursorVisible(false);
        // Remove focus from the view.
        mLineEditText.clearFocus();
    }

    /**
     * Enable line interaction with edit text.
     */
    private void enableContentInteraction() {
        // Set the Key listener to interact with the EditText.
        mLineEditText.setKeyListener(new EditText(this).getKeyListener());
        // This view is now focusable.
        mLineEditText.setFocusable(true);
        // This view can receive focus well in touch mode.
        mLineEditText.setFocusableInTouchMode(true);
        // The Cursor is not visible.
        mLineEditText.setCursorVisible(true);
        // Call this to try and give focus to a specific view or to one of its descendants.
        mLineEditText.requestFocus();
    }

    /**
     * Hide soft keyboard after exiting edit mode.
     */
    private void hideSoftKeyBoard() {
        Log.d(TAG, "hideSoftKeyBoard: ");

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if(view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * getIncomingIntent, determines if it is a new note or an existing one.
     *
     * @return existing note or new note.
     */
    private boolean getIncomingIntent() {
        if(getIntent().hasExtra("selected_note")) {
            mInitialNote = getIntent().getParcelableExtra("selected_note");
            Log.d(TAG, "getIncomingIntent: mInitialNote " + mInitialNote);

            mFinalNote = new Note();
            mFinalNote.setTitle(mInitialNote.getTitle());
            mFinalNote.setContent(mInitialNote.getContent());
            mFinalNote.setTimeStamp(mInitialNote.getTimeStamp());
            mFinalNote.setId(mInitialNote.getId());

            mMode = EDIT_MODE_DISABLED;
            mIsNewNote = false;
            return false;
        }
        mMode = EDIT_MODE_ENABLED;
        mIsNewNote = true;
        return true;
    }

    private void saveChanges() {
        if(mIsNewNote) {
            Log.d(TAG, "saveChanges: new note");
            saveNewNote();
        } else {
            Log.d(TAG, "saveChanges: updateNote");
            updateNote();
        }
    }

    private void updateNote() {
        mNoteRepository.updateNote(mFinalNote);
    }

    private void saveNewNote() {
        Log.d(TAG, "saveNewNote: ");
        mNoteRepository.insertNoteTask(mFinalNote);
    }

    /**
     * Set the Listeners so the user can interact with the app.
     */
    private void setListeners() {
        Log.d(TAG, "setListeners: ");

        mLineEditText.setOnTouchListener(this);
        mGestureDetector = new GestureDetector(this, this);
        mViewTitle.setOnClickListener(this); // Will run the onClick method
        mCheck.setOnClickListener(this); // Will run the onClick method
        mBackArrow.setOnClickListener(this);
        mEditTitle.addTextChangedListener(this); // Watch text listener of title.
    }

    /**
     * Called when a touch event is dispatched o a view. This allows listeners to get a chance to respond
     * before the target view.
     *
     * @param v The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    /**
     * Not used in this app
     */
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    /**
     * Not used in this app
     */
    @Override
    public void onShowPress(MotionEvent e) {

    }

    /**
     * Not used in this app
     */
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    /**
     * Not used in this app
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    /**
     * Not used in this app
     */
    @Override
    public void onLongPress(MotionEvent e) {

    }

    /**
     * Not used in this app
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    /**
     * When a double tap occurs start edit mode.
     *
     * @param e The motion event that occurred during the double-tap gesture.
     * @return true if the event is consumed, else false.
     */
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(TAG, "onDoubleTap: ");
        enableEditMode();
        return false;
    }

    /**
     * Not used in this app
     */
    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    /**
     * Call When a view is clicked
     * @param v the view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.notes_check:
                Log.d(TAG, "onClick: notes_check clicked");
                hideSoftKeyBoard();
                disableEditMode();
                break;
            case R.id.note_text_title:
                Log.d(TAG, "onClick: note_text_title clicked");
                mEditTitle.requestFocus();
                mEditTitle.setSelection(mEditTitle.length());
                break;
            case R.id.notes_back_arrow:
                finish();
                break;
        }
    }

    /**
     * When the back button is pressed in EditMode, EditMode will come to an end but the Activity will
     * remain the same. If the back button is pressed outside of EditMode onBackPressed() will run
     * as orignally intended.
     */
    @Override
    public void onBackPressed() {
        if(mMode == EDIT_MODE_ENABLED) {
            Log.d(TAG, "onBackPressed: in Edit mode");
            onClick(mCheck);
        } else {
            Log.d(TAG, "onBackPressed: Edit mode disabled");
            super.onBackPressed();
        }

    }

    /**
     * Called to retrieve per-instance state from an activity before being killed so the state can be
     * restored in onRestoreInstanceState
     *
     * @param outState Bundle in which to place the saved state.
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outState);
        outState.putInt("mode", mMode);
    }

    /**
     * This method is called after onStart() when the activity is being re-initialized form a previously
     * saved state, give from onSavedInstanceState().
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState: ");
        super.onRestoreInstanceState(savedInstanceState);
        mMode = savedInstanceState.getInt("mode");
        if(mMode == EDIT_MODE_ENABLED) {
            enableEditMode();
        }
    }

    /**
     * This method is called to notify you that , within s, the count characters beginning at start
     * are about to be replaced by new text with length after. It is an error to attempt to make
     * changes to s from this callback.
     *
     * @param s CharSequence
     * @param start int
     * @param count int
     * @param after int
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.d(TAG, "beforeTextChanged: s: " + s.toString() + " start: " + start + " count: " + count + " after: " + after);
    }

    /**
     * This method is called to notify you that within s, the count characters beginning at start have
     * just replaced old text that had length before. It is an error to attempt to make changes to s from this callback.
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d(TAG, "onTextChanged: s: " + s.toString() + " start: " + start + " before " + before + " count: " + count);
        mViewTitle.setText(s.toString());
    }

    /**
     * This method is called to notify you that, somewhere within s, the text has changed.
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {
        Log.d(TAG, "afterTextChanged: s: " + s);
    }
}
