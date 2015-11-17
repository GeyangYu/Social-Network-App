package csc296.project02;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.util.UUID;

import csc296.project02.database.DbCursorWrapper;
import csc296.project02.model.User;

public class Logon extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private EditText mName;
    private EditText mBirthday;
    private EditText mHometown;
    private EditText mBIO;
    private ImageButton mPhotos;
    private String mPhoto;
    private File mPhotoFile;
    private Uri photoUri;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);

        mEmail = (EditText)findViewById(R.id.editText3);
        mPassword = (EditText)findViewById(R.id.editText4);
        mName = (EditText)findViewById(R.id.editText5);
        mBirthday = (EditText)findViewById(R.id.editText6);
        mHometown = (EditText)findViewById(R.id.editText7);
        mBIO = (EditText)findViewById(R.id.editText8);
        mPhotos = (ImageButton)findViewById(R.id.imageButton);

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
        }
    }

    public void submit(View v) {

        DbCursorWrapper dbUser = new DbCursorWrapper(this);

        if (mEmail.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "You haven't enter an email!", Toast.LENGTH_LONG).show();
        } else if (mPassword.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "You haven't enter a password!", Toast.LENGTH_LONG).show();
        } else {
            mUser = dbUser.checkEmail(mEmail.getText().toString());
            if (mUser != null) {

                Toast.makeText(getApplicationContext(), "Account exists! Please back to login!", Toast.LENGTH_LONG).show();
            }
            else {
                mUser = new User();
                mUser.setEmail(mEmail.getText().toString());
                mUser.setPassword(mPassword.getText().toString());
                mUser.setFullName(mName.getText().toString());
                mUser.setBirthDate(mBirthday.getText().toString());
                mUser.setHomeTown(mHometown.getText().toString());
                mUser.setBIO(mBIO.getText().toString());
                if (mPhoto != null) {
                    mUser.setProfilePic(mPhoto);
                    Log.d("P", mUser.getProfilePic());
                } else {
                    Log.d("P", "no");
                }

                DbCursorWrapper.get(getApplicationContext()).insertUser(mUser);
                Toast.makeText(getApplicationContext(), "Submit successful! Please log in!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}

