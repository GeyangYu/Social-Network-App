package csc296.project02.model;

/**
 * Created by yugeyang on 15-11-11.
 */
public class FeedItem {
    private String mEmail;
    private String mPostedDate;
    private String mContent;
    private String mPhotoPath;

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPostedDate() {
        return mPostedDate;
    }

    public void setPostedDate(String postedDate) {
        mPostedDate = postedDate;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getPhotoPath() {
        return mPhotoPath;
    }

    public void setPhotoPath(String photoPath) {
        mPhotoPath = photoPath;
    }
}
