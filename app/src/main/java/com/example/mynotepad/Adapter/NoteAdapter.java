package com.example.mynotepad.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mynotepad.Models.Note;
import com.example.mynotepad.R;
import com.example.mynotepad.Utility.Timestamp;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private static final String TAG = "NoteAdapter";

    private ArrayList<Note> mNotes = new ArrayList<>();
    // Reference to the Interface that handles clicks.
    private NoteListener mNoteListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title, timestamp;
        // Interface
        NoteListener onNoteListener;


        public ViewHolder(@NonNull View itemView, NoteListener onNoteListener) {
            super(itemView);
            title = itemView.findViewById(R.id.note_list_title);
            timestamp = itemView.findViewById(R.id.note_list_time_stamp);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        /**
         * This method will Run when an item is selected in RecyclerView.
         * @param v
         */
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: ");
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public NoteAdapter(ArrayList<Note> notes, NoteListener onNoteListener) {
        this.mNotes = notes;
        this.mNoteListener = onNoteListener;
    }

    /**
     * Assign XML recycler Item file. creates a view object for each entry loaded in the recyclerview.
     *
     * @param parent The ViewGroup to witch the new view will be added to after it is bond.
     * @param viewType The ViewType of the new view.
     * @return A new ViewHolder that holds a view of the given view type.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_note_list_item, parent, false);
        return new ViewHolder(view, mNoteListener);
    }

    /**
     * Displays the data at the specified position in the list.
     *
     * @param holder updates to represent the contents of the item at the given position.
     * @param position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try{
            String month = mNotes.get(position).getTimeStamp().substring(0, 2);
            month = Timestamp.getMonthFromNumber(month);

            String day = mNotes.get(position).getTimeStamp().substring(0, 2);

            String year = mNotes.get(position).getTimeStamp().substring(3);
            String timeStamp = month + " " + day + " " + year;

            holder.timestamp.setText(timeStamp);
            holder.title.setText(mNotes.get(position).getTitle());

        } catch (NullPointerException e) {
            Log.e(TAG, "onBindViewHolder: ",e);
        }
    }

    /**
     * Size of the recyclerview.
     */
    @Override
    public int getItemCount() {
        return mNotes.size();
    }


    /**
     * This interface will handle item clicks in the recycler view.
     */
    public interface NoteListener {
        void onNoteClick(int position);
    }
}
