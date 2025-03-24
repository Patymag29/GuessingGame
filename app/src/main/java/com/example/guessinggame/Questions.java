package com.example.guessinggame;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Questions implements Parcelable { // parcelzation  (transform class into package)
    public int imageResId; // hold flag image reference
    public String questionAsk;
    public String option1, option2, option3, option4;
    public String correctAnswer;
    public boolean answered = false;
    public Questions(int imageResId, String ask, String op1, String op2, String op3, String op4, String correct) { // constructor method
        this.imageResId    = imageResId; // flag
        this.questionAsk   = ask;
        this.option1       = op1;
        this.option2       = op2;
        this.option3       = op3;
        this.option4       = op4;
        this.correctAnswer = correct;
    }
    protected Questions (Parcel in) { // parcel is a generic object (here is a constructor method using Parcel)
        imageResId    = in.readInt(); // flag
        questionAsk   = in.readString();
        option1       = in.readString();
        option2       = in.readString();
        option3       = in.readString();
        option4       = in.readString();
        correctAnswer = in.readString();
        answered      = in.readByte() != 0; // Convert 1 byte (0 ou 1) to boolean
    }

    @Override
    /* DEFAULT METHOD OF PARCELABLE - that gets the object QUESTION and transforms its values into a PARCEL (packing (parcel) of data
     and can be read later by methods readString() and readByte().
     */
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(imageResId); // reference of flag
        dest.writeString(questionAsk); // dest (destination)
        dest.writeString(option1);
        dest.writeString(option2);
        dest.writeString(option3);
        dest.writeString(option4);
        dest.writeString( correctAnswer);
        dest.writeByte((byte) (answered ? 1 : 0)); //This line takes the value of the boolean variable answered (which can be either true(1) or false(2)) and saves it in Parcel as a number.
            }

    /* DEFAULT METHOD OF PARCELABLE - The CREATOR is a special object that defines how to recreate a Question object from a Parcel:
    📌 Android uses this CREATOR internally when we want to retrieve a Parcelable object from a Bundle(parcel) (like in onSaveInstanceState()). */
    public static final Creator<Questions> CREATOR = new Creator<Questions>() {
        @Override
        public Questions createFromParcel(Parcel in){
            return new Questions(in);
        }
        @Override
        public Questions[] newArray(int size) {
            return new Questions[size];
        }
    };
    @Override
    public int describeContents() { // default method of parcelable
        return 0;
    }
}
