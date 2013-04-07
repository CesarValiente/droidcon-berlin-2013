package com.spanisharmada.droidcon.data.model;

/**
 * Profile data bean to store all user data
 * 
 */
public class SAProfileData {

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

    // ----------- Getters & Setters --------------//

    public String getmName() {
        return mName;
    }

    public void setmName(final String mName) {
        this.mName = mName;
    }

    public String getmPictureURL() {
        return mPictureURL;
    }

    public void setmPictureURL(final String mPictureURL) {
        this.mPictureURL = mPictureURL;
    }

    public String getmSex() {
        return mSex;
    }

    public void setmSex(final String mSex) {
        this.mSex = mSex;
    }

    public int getmAge() {
        return mAge;
    }

    public void setmAge(final int mAge) {
        this.mAge = mAge;
    }

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(final String mLocation) {
        this.mLocation = mLocation;
    }

    public String getmInterests() {
        return mInterests;
    }

    public void setmInterests(final String mInterests) {
        this.mInterests = mInterests;
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

}
