package csc296.project02;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import csc296.project02.database.DbCursorWrapper;
import csc296.project02.model.User;


public class UserViewFragment extends android.support.v4.app.Fragment {
    private static final String KEY_ID = "ID";

    private TextView mName;
    private TextView mBd;
    private TextView mHt;
    private TextView mBIO;

    private ImageButton mIfFavorite;
    private ImageView mPhoto;
    private Button mFavorite;
    private Button mUnFavorite;

    private User mUser;
    static boolean ifFromF;
    private User mUserOwn = MainActivity.USER;

    public UserViewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_view, container, false);

        mName = (TextView)view.findViewById(R.id.text_name);
        mBd = (TextView)view.findViewById(R.id.text_bd);
        mHt = (TextView)view.findViewById(R.id.text_ht);
        mBIO = (TextView)view.findViewById(R.id.text_bio);
        mPhoto = (ImageView)view.findViewById(R.id.imageView_each);
        mIfFavorite = (ImageButton)view.findViewById(R.id.imageButton_favorite);
        mFavorite = (Button)view.findViewById(R.id.button_favorite);
        mUnFavorite = (Button)view.findViewById(R.id.button_unfavorite);

        if (ifFromF) {
            mUser = UserListFFragment.USER;
            updateUserInterface();
        }
        else {
            mUser = UserListFragment.USER;
            updateUserInterface();
        }

        mFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIfFavorite.setVisibility(View.VISIBLE);
                DbCursorWrapper.get(getContext()).insertFavorite(mUserOwn.getEmail(), mUser.getEmail());
            }
        });

        mUnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIfFavorite.setVisibility(View.INVISIBLE);
                DbCursorWrapper.get(getContext()).deleteFavorite(mUserOwn.getEmail(), mUser.getEmail());
            }
        });

        boolean ifF = DbCursorWrapper.get(getContext()).ifFavorite(mUserOwn.getEmail(), mUser.getEmail());
        if (ifF) {
            mIfFavorite.setVisibility(View.VISIBLE);
        }
        else {
            mIfFavorite.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    private void updateUserInterface() {
        mName.setText("Name: "+mUser.getFullName());
        mBd.setText("Birthday: "+mUser.getBirthDate());
        mHt.setText("Hometown: "+mUser.getHomeTown());
        mBIO.setText("BIO: " + mUser.getBIO());
        if (mUser.getProfilePic() != null) {
            mPhoto.setImageURI(Uri.parse(mUser.getProfilePic()));
            Log.d("P", mUser.getProfilePic());
        } else {
            Log.d("P", "no");
        }
    }
}
