package csc296.project02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class UserListF extends AppCompatActivity {

    private UserListFFragment mUserListF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_f);
        SysApplication.getInstance().addActivity(this);

        mUserListF = new UserListFFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(R.id.frame_layout_users_f, mUserListF, null)
                .commit();

        ActionBar aBar = getSupportActionBar();
        aBar.setSubtitle(R.string.favoritelist);
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
