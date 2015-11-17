package csc296.project02;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.UUID;

import csc296.project02.database.DbCursorWrapper;
import csc296.project02.model.User;

public class UserProfile extends AppCompatActivity {

    private static final String KEY_ID = "ID";

    private TextView mEmail;
    private EditText mEmailNo;
    private EditText mPassword;
    private EditText mName;
    private EditText mBirthday;
    private EditText mHometown;
    private EditText mBIO;
    private ImageButton mPhotos;
    private Button mDelete;
    private String mPhoto;
    private File mPhotoFile;
    private Uri photoUri;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);
        SysApplication.getInstance().addActivity(this);

        mEmail = (TextView)findViewById(R.id.text_email);
        mEmailNo = (EditText)findViewById(R.id.editText3);
        mPassword = (EditText)findViewById(R.id.editText4);
        mName = (EditText)findViewById(R.id.editText5);
        mBirthday = (EditText)findViewById(R.id.editText6);
        mHometown = (EditText)findViewById(R.id.editText7);
        mBIO = (EditText)findViewById(R.id.editText8);
        mPhotos = (ImageButton)findViewById(R.id.imageButton);
        mDelete = (Button)findViewById(R.id.button6);
        mUser = MainActivity.USER;

        mDelete.setVisibility(View.VISIBLE);
        mEmail.setVisibility(View.VISIBLE);
        mEmailNo.setVisibility(View.INVISIBLE);
        mEmail.setText("Email: " + mUser.getEmail());
        mPassword.setText(mUser.getPassword());
        mName.setText(mUser.getFullName());
        mBirthday.setText(mUser.getBirthDate());
        mHometown.setText(mUser.getHomeTown());
        mBIO.setText(mUser.getBIO());
        if (mUser.getProfilePic() != null) {
            mPhotos.setImageURI(Uri.parse(mUser.getProfilePic()));
        }
        else {
            mPhotos.setImageResource(R.drawable.head);
        }

        //Toast.makeText(getApplicationContext(), "Update your profile if you want!", Toast.LENGTH_LONG).show();
        Log.d("user", mUser.getFullName());

        ActionBar aBar = getSupportActionBar();
        aBar.setSubtitle(R.string.Profile);

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

    public void photo(View v) {
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
            mPhoto = photoUri.toString();
            mPhotos.setImageURI(Uri.parse(mPhoto));
            mUser.setProfilePic(mPhoto);
        }
    }

    public void submit(View v) {

        mUser.setPassword(mPassword.getText().toString());
        mUser.setFullName(mName.getText().toString());
        mUser.setBirthDate(mBirthday.getText().toString());
        mUser.setHomeTown(mHometown.getText().toString());
        mUser.setBIO(mBIO.getText().toString());
        DbCursorWrapper.get(getApplicationContext()).updateUser(mUser);

        Toast.makeText(getApplicationContext(), "Your profile is changed!", Toast.LENGTH_LONG).show();
    }

    public void delete(View v) {

        DbCursorWrapper.get(getApplicationContext()).deleteUser(mUser);
        SysApplication.getInstance().exit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}


