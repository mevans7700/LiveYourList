package com.evansappwriter.liveyourlist.model;

/**
 * Created by markevans on 8/19/16.
 */
public class Business implements Comparable<Business> {
    private String mFirstName;
    private String mLastName;

    public Business(String firstName, String lastName) {
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

    @Override
    public int compareTo(Business another) {
        return 0;
    }
}
