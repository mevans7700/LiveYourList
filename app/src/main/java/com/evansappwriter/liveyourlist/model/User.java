package com.evansappwriter.liveyourlist.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by markevans on 7/25/16.
 */
public class User implements Parcelable, Comparable<User>  {
    private String mFirstName;
    private String mLastName;

    public User(String firstName, String lastName) {
        mFirstName = firstName;
        mLastName = lastName;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public static Creator<User> getCREATOR() {
        return CREATOR;
    }

    protected User(Parcel in) {
        mFirstName = in.readString();
        mLastName = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int compareTo(User another) {
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
    }
}
