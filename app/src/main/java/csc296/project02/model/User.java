package csc296.project02.model;

/**
 * Created by yugeyang on 15-11-11.
 */
public class User {
    private String mEmail;
    private String mPassword;
    private String mFullName;
    private String mBirthDate;
    private String mProfilePic;
    private String mHomeTown;
    private String mBIO;

    public User() {
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String fullName) {
        mFullName = fullName;
    }

    public String getBirthDate() {
        return mBirthDate;
    }

    public void setBirthDate(String birthDate) {
        mBirthDate = birthDate;
    }

    public String getHomeTown() {
        return mHomeTown;
    }

    public void setHomeTown(String homeTown) {
        mHomeTown = homeTown;
    }

    public String getProfilePic() {
        return mProfilePic;
    }

    public void setProfilePic(String profilePic) {
        mProfilePic = profilePic;
    }

    public String getBIO() {
        return mBIO;
    }

    public void setBIO(String bio) {
        mBIO = bio;
    }
}
