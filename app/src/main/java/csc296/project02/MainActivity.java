package csc296.project02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import csc296.project02.database.DbCursorWrapper;
import csc296.project02.model.User;

public class MainActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;

    private User mUser;

    static User USER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View v) {
        mEmail = (EditText)findViewById(R.id.editText2);
        mPassword = (EditText)findViewById(R.id.editText);

        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        DbCursorWrapper dbUser = new DbCursorWrapper(this);

        mUser = dbUser.checkEmail(mEmail.getText().toString());
        if (mUser == null) {
            Toast.makeText(getApplicationContext(), "Nonexistent account! Please sign up!", Toast.LENGTH_LONG).show();
        }
        else {
            mUser = dbUser.getUser(email, password);
            if (mUser != null) {
                USER = mUser;
                Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, UserProfile.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(getApplicationContext(), "Wrong password! Try again!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void logon(View v) {
        Intent intent = new Intent(this, Logon.class);
        startActivity(intent);
    }

    public void delete(View v) {
        DbCursorWrapper.get(getApplicationContext()).delete();
    }


}
