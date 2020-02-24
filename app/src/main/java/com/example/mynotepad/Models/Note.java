package com.example.mynotepad.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note
        implements Parcelable { // Parcelable is implemented so Note objects can be passed as extras
    // the new activity.

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "content")
    private String mContent;

    @NonNull
    @ColumnInfo(name = "timestamp")
    private String mTimeStamp;

    public Note(String mTitle, String mContent, String mTimeStamp) {
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.mTimeStamp = mTimeStamp;
    }

    @Ignore
    public Note() {
    }

    protected Note(Parcel in) {
        id = in.readInt();
        mTitle = in.readString();
        mContent = in.readString();
        mTimeStamp = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(String mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }

    /**
     * Describes the kind of special object contained in this Parcelable instances marshaled representation.
     *
     * @return A bitmask indicating the set of special object types marshaled by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten the object in to a Parcel.
     * @param dest The parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(mTitle);
        dest.writeString(mContent);
        dest.writeString(mTimeStamp);
    }

    /**
     *  This object converted to a string
     * @return string.
     */
    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", mTitle='" + mTitle + '\'' +
                ", mContent='" + mContent + '\'' +
                ", mTimeStamp='" + mTimeStamp + '\'' +
                '}';
    }
}
