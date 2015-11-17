package csc296.project02;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import csc296.project02.database.DbCursorWrapper;
import csc296.project02.model.FeedItem;
import csc296.project02.model.User;

public class Post extends AppCompatActivity {

    private EditText mPost;
    private ImageButton mPhoto;

    private String mPhotoUri;
    private File mPhotoFile;
    private Uri photoUri;

    private User mUser = MainActivity.USER;
    private FeedItem mFeedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        SysApplication.getInstance().addActivity(this);

        mPost = (EditText)findViewById(R.id.editText_post);
        mPhoto = (ImageButton)findViewById(R.id.imageButton2);

        Log.d("user", mUser.getFullName());

        ActionBar aBar = getSupportActionBar();
        aBar.setSubtitle(R.string.post);

    }

    public void photo_post(View v) {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        //make a random filename
        String filename = "IMG_" + UUID.randomUUID().toString() + ".jpg";
        //make a file in the external photos directory
        File picturesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        mPhotoFile = new File(picturesDir, filename);

        photoUri = Uri.fromFile(mPhotoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            mPhotoUri = photoUri.toString();
            mPhoto.setImageURI(Uri.parse(mPhotoUri));
        }
    }

    public void submit_post(View v) {

        mFeedItem = new FeedItem();
        mFeedItem.setEmail(mUser.getEmail());
        mFeedItem.setPostedDate((new Date(System.currentTimeMillis()).toString()));
        mFeedItem.setContent(mPost.getText().toString());
        if (mPhotoUri != null) {
            mFeedItem.setPhotoPath(mPhotoUri);
            Log.d("P", mFeedItem.getPhotoPath());
        } else {
            Log.d("P", "no");
        }

        DbCursorWrapper.get(getApplicationContext()).insertFeedItem(mFeedItem);
        Log.d(mFeedItem.getEmail(), mFeedItem.getContent());
        Log.d("D", mFeedItem.getPostedDate());
        Toast.makeText(getApplicationContext(), "Submit successful! You made a post!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MyPost.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled;
        switch(item.getItemId()) {
            case R.id.menu_browse:
                restartActivity(Browse.class);
                handled = true;
                break;
            case R.id.menu_favorite:
                restartActivity(UserListF.class);
                handled = true;
                break;
            case R.id.menu_list:
                restartActivity(UserList.class);
                handled = true;
                break;
            case R.id.menu_logout:
                restartActivity(Logout.class);
                handled = true;
                break;
            case R.id.menu_post:
                restartActivity(Post.class);
                handled = true;
                break;
            case R.id.menu_profile:
                restartActivity(UserProfile.class);
                handled = true;
                break;
            case R.id.menu_my:
                restartActivity(MyPost.class);
                handled = true;
                break;
            default:
                handled = super.onOptionsItemSelected(item);
        }
        return handled;
    }

    private void restartActivity(Class activityClass) {
        Intent intent = new Intent(this, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
}
