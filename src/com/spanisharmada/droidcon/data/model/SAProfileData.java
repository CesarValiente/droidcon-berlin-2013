package com.spanisharmada.droidcon.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Profile data bean to store all user data
 * 
 */
public class SAProfileData implements Parcelable {

    public static final String PROFILE_DATA_KEY = "profileDataKey";

    private String mName;
    private String mPictureURL;
    private String mSex;
    private int mAge;
    private String mLocation;
    private String mInterests;

    /**
     * Constructor
     * 
     * @param name
     * @param pictureURL
     * @param sex
     * @param age
     * @param location
     * @param interests
     */
    public SAProfileData(final String name, final String pictureURL,
            final String sex, final int age, final String location,
            final String interests) {

        mName = name;
        mPictureURL = pictureURL;
        mSex = sex;
        mAge = age;
        mLocation = location;
        mInterests = interests;
    }

    /**
     * Constructor from {@link Parcel}
     * 
     * @param source
     */
    public SAProfileData(final Parcel source) {

        mName = source.readString();
        mPictureURL = source.readString();
        mSex = source.readString();
        mAge = source.readInt();
        mLocation = source.readString();
        mInterests = source.readString();
    }

    // ----------- Getters & Setters --------------//

    public String getName() {
        return mName;
    }

    public void setName(final String name) {
        mName = name;
    }

    public String getPictureURL() {
        return mPictureURL;
    }

    public void setPictureURL(final String pictureURL) {
        mPictureURL = pictureURL;
    }

    public String getSex() {
        return mSex;
    }

    public void setSex(final String sex) {
        mSex = sex;
    }

    public int getAge() {
        return mAge;
    }

    public void setAge(final int age) {
        mAge = age;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(final String location) {
        mLocation = location;
    }

    public String getInterests() {
        return mInterests;
    }

    public void setInterests(final String interests) {
        mInterests = interests;
    }

    // ------------------------------------------//

    /*
     * mName = name; mPictureURL = pictureURL; mSex = sex; mAge = age; mLocation
     * = location; mInterests = interests;
     */

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return new StringBuilder().append("Name: ").append(mName)
                .append("\nPicture URL: ").append(mPictureURL)
                .append("\nSex: ").append(mSex).append("\nAge: ")
                .append("\nLocation: ").append(mLocation)
                .append("\nInterests: ").append(mInterests).toString();
    }

    // -------- Parcel section ---------------//

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(mName);
        dest.writeString(mPictureURL);
        dest.writeString(mSex);
        dest.writeInt(mAge);
        dest.writeString(mLocation);
        dest.writeString(mInterests);
    }

    public static final Parcelable.Creator<SAProfileData> CREATOR = new Parcelable.Creator<SAProfileData>() {

        @Override
        public SAProfileData createFromParcel(final Parcel source) {
            return new SAProfileData(source);
        }

        @Override
        public SAProfileData[] newArray(final int size) {
            return new SAProfileData[size];
        }

    };

}
