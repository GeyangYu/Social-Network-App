package csc296.project02.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import csc296.project02.model.FeedItem;
import csc296.project02.model.User;

/**
 * Created by yugeyang on 15-11-11.
 */
public class DbCursorWrapper {
    private static DbCursorWrapper SINGLETON;

    private final SQLiteDatabase mDatabase;

    private List<User> mUser;
    private Map<String,User> mUserMap;
    private List<FeedItem> mFeedItem;

    public DbCursorWrapper(Context context) {

        mDatabase = new OpenHelper(context).getWritableDatabase();
        mUser = new LinkedList<User>();
        mUserMap = new HashMap<String, User>();
        mFeedItem = new LinkedList<FeedItem>();
    }

    public static DbCursorWrapper get(Context context) {
        if(SINGLETON == null) {
            SINGLETON = new DbCursorWrapper(context);
        }
        return SINGLETON;
    }

    public List<User> getEach(User userown) {
        mUser.clear();
        mUserMap.clear();
        UserCursorWrapper wrapper = queryUser(null, null);

        try {
            wrapper.moveToFirst();
            while(!wrapper.isAfterLast()) {
                User user = wrapper.getUser();
                if (!userown.getEmail().equals(user.getEmail())) {
                    mUser.add(user);
                    mUserMap.put(user.getEmail(), user);
                }
                wrapper.moveToNext();
            }
        }
        finally {
            wrapper.close();
        }

        return mUser;
    }

    public List<FeedItem> getFeedItem(User userown) {

        mFeedItem.clear();
        UserCursorWrapper wrapper = queryFeedItem(null, null);
        try {
            wrapper.moveToFirst();
            while (!wrapper.isAfterLast()) {
                FeedItem feedItem = wrapper.getFeedItem();
                if (ifFavorite(userown.getEmail(), feedItem.getEmail()) || userown.getEmail().equals(feedItem.getEmail())) {
                    mFeedItem.add(feedItem);
                }
                wrapper.moveToNext();
            }
        } finally {
            wrapper.close();
        }
        return mFeedItem;
    }

    public List<FeedItem> getMyPost(User userown) {

        mFeedItem.clear();
        UserCursorWrapper wrapper = queryFeedItem(null, null);
        try {
            wrapper.moveToFirst();
            while (!wrapper.isAfterLast()) {
                FeedItem feedItem = wrapper.getFeedItem();
                if (userown.getEmail().equals(feedItem.getEmail())) {
                    mFeedItem.add(feedItem);
                }
                wrapper.moveToNext();
            }
        } finally {
            wrapper.close();
        }
        return mFeedItem;
    }


    public List<User> getFavorite(User userown) {
        mUser.clear();
        mUserMap.clear();
        UserCursorWrapper wrapper = queryUser(null, null);

        try {
            wrapper.moveToFirst();
            while(!wrapper.isAfterLast()) {
                User user = wrapper.getUser();
                if (ifFavorite(userown.getEmail(), user.getEmail())) {
                    mUser.add(user);
                    mUserMap.put(user.getEmail(), user);
                }
                wrapper.moveToNext();
            }
        }
        finally {
            wrapper.close();
        }

        return mUser;
    }

    public boolean delete() {

        mDatabase.delete(Schema.Users.NAME, null, null);
        mDatabase.delete(Schema.FeedItems.NAME, null, null);
        mDatabase.delete(Schema.Favorites.NAME, null, null);
        Log.d("delete", "ok");
        return false;
    }

    public User checkEmail(String email) {
        Cursor cursor = mDatabase.query(
                Schema.Users.NAME, // table name
                null,
                "email = ?",
                new String[] { email},
                null,
                null,
                null
        );
        UserCursorWrapper wrapper = new UserCursorWrapper(cursor);
        User user;

        if(wrapper.getCount() > 0) {
            wrapper.moveToFirst();
            user = wrapper.getUser();
        }
        else {
            user = null;
        }
        wrapper.close();

        return user;
    }

    public User getUser(String email, String password) {
        Cursor cursor = mDatabase.query(
                Schema.Users.NAME, // table name
                null,
                "email = ? AND password = ?",
                new String[] { email, password},
                null,
                null,
                null
        );
        UserCursorWrapper wrapper = new UserCursorWrapper(cursor);
        User user;

        if(wrapper.getCount() > 0) {
            wrapper.moveToFirst();
            user = wrapper.getUser();
        }
        else {
            user = null;
        }
        wrapper.close();

        return user;
    }

    public void insertUser(User user) {
        ContentValues values = getUserContentValues(user);
        mDatabase.insert(
                Schema.Users.NAME,
                null,
                values
        );
    }

    public void updateUser(User user) {
        String email = user.getEmail();
        ContentValues values = getUserContentValues(user);
        mDatabase.update(Schema.Users.NAME,
                values,
                Schema.Users.Cols.EMAIL + "=?",
                new String[]{email});
    }

    public void insertFeedItem(FeedItem item) {
        ContentValues values = getFeedItemContentValues(item);
        mDatabase.insert(
                Schema.FeedItems.NAME,
                null,
                values
        );
    }

    public void deleteFeedItem(FeedItem item) {
        mDatabase.delete(
                Schema.FeedItems.NAME,
                "email = ? AND posted_date = ?",
                new String[]{item.getEmail(), item.getPostedDate()}
        );
    }

    public void deleteUser(User user) {
        mDatabase.delete(
                Schema.Users.NAME,
                "email = ?",
                new String[] {user.getEmail()}
        );
        mDatabase.delete(
                Schema.FeedItems.NAME,
                "email = ?",
                new String[] {user.getEmail()}
        );
        mDatabase.delete(
                Schema.Favorites.NAME,
                "email = ?",
                new String[] {user.getEmail()}
        );

    }

    public void insertFavorite(String email, String favorite) {
        ContentValues values = new ContentValues();
        values.put(Schema.Favorites.Cols.EMAIL, email);
        values.put(Schema.Favorites.Cols.FAVORITE, favorite);

        mDatabase.insert(
                Schema.Favorites.NAME,
                null,
                values
        );
    }

    public void deleteFavorite(String email, String favorite) {
        // ContentValues values = new ContentValues();
        mDatabase.delete(
                Schema.Favorites.NAME,
                "email = ? AND favorite = ?",
                new String[]{email, favorite});
    }

    public boolean ifFavorite(String email, String favorite) {
        Cursor cursor = mDatabase.query(
                Schema.Favorites.NAME, // table name
                null,
                "email = ? AND favorite = ?",
                new String[] { email, favorite},
                null,
                null,
                null
        );
        UserCursorWrapper wrapper = new UserCursorWrapper(cursor);

        boolean ifF;
        if(wrapper.getCount() > 0) {
            wrapper.moveToFirst();
            ifF = true;
        }
        else {
            ifF = false;
        }
        wrapper.close();

        return ifF;
    }

    private UserCursorWrapper queryUser(String where, String[] args) {
        Cursor cursor = mDatabase.query(
                Schema.Users.NAME, // table name
                null,                          // which columns; null for all
                where,                         // where clause, e.g. id=?
                args,                          // where arguments
                null,                          // group by
                null,                          // having
                null                           // order by
        );

        return new UserCursorWrapper(cursor);
    }

    private UserCursorWrapper queryFeedItem(String where, String[] args) {
        Cursor cursor = mDatabase.query(
                Schema.FeedItems.NAME, // table name
                null,                          // which columns; null for all
                where,                         // where clause, e.g. id=?
                args,                          // where arguments
                null,                          // group by
                null,                          // having
                "posted_date desc"                           // order by
        );

        return new UserCursorWrapper(cursor);
    }

    private static ContentValues getUserContentValues(User user) {
        ContentValues values = new ContentValues();

        values.put(Schema.Users.Cols.EMAIL, user.getEmail());
        values.put(Schema.Users.Cols.PASSWORD, user.getPassword());
        values.put(Schema.Users.Cols.FULL_NAME, user.getFullName());
        values.put(Schema.Users.Cols.BIRTH_DATE, user.getBirthDate());
        values.put(Schema.Users.Cols.HOMETOWN, user.getHomeTown());
        values.put(Schema.Users.Cols.BIO, user.getBIO());
        values.put(Schema.Users.Cols.PROFILE_PIC, user.getProfilePic());

        return values;
    }

    private static ContentValues getFeedItemContentValues(FeedItem item) {
        ContentValues values = new ContentValues();

        values.put(Schema.FeedItems.Cols.EMAIL, item.getEmail());
        values.put(Schema.FeedItems.Cols.POSTED_DATE, item.getPostedDate());
        values.put(Schema.FeedItems.Cols.CONTENT, item.getContent());
        values.put(Schema.FeedItems.Cols.PHOTO_PATH, item.getPhotoPath());

        return values;
    }

    private static class UserCursorWrapper extends CursorWrapper {
        public UserCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public User getUser() {
            User user = new User();

            int emailIndex = getColumnIndex(Schema.Users.Cols.EMAIL);
            String email = getString(emailIndex);
            user.setEmail(email);
            user.setPassword(getString(getColumnIndex(Schema.Users.Cols.PASSWORD)));
            user.setFullName(getString(getColumnIndex(Schema.Users.Cols.FULL_NAME)));
            user.setBirthDate(getString(getColumnIndex(Schema.Users.Cols.BIRTH_DATE)));
            user.setHomeTown(getString(getColumnIndex(Schema.Users.Cols.HOMETOWN)));
            user.setBIO(getString(getColumnIndex(Schema.Users.Cols.BIO)));
            user.setProfilePic(getString(getColumnIndex(Schema.Users.Cols.PROFILE_PIC)));

            return user;
        }

        public FeedItem getFeedItem() {
            FeedItem feedItem = new FeedItem();

            int emailIndex = getColumnIndex(Schema.FeedItems.Cols.EMAIL);
            String email = getString(emailIndex);
            feedItem.setEmail(email);
            feedItem.setPostedDate(getString(getColumnIndex(Schema.FeedItems.Cols.POSTED_DATE)));
            feedItem.setPhotoPath(getString(getColumnIndex(Schema.FeedItems.Cols.PHOTO_PATH)));
            feedItem.setContent(getString(getColumnIndex(Schema.FeedItems.Cols.CONTENT)));

            return feedItem;
        }

    }
}
