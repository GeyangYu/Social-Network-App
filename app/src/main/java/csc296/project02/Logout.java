package csc296.project02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Logout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        SysApplication.getInstance().exit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
