package csc296.project02;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class UserView extends AppCompatActivity {

    public static final String EXTRA_ID = "email";

    private UserViewFragment mUserView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);
        SysApplication.getInstance().addActivity(this);

        Intent intent = getIntent();
        String email = (String)intent.getSerializableExtra(EXTRA_ID);

        mUserView = new UserViewFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(R.id.frame_layout_user_view, mUserView, null)
                .commit();

        ActionBar aBar = getSupportActionBar();
        aBar.setSubtitle("View User");
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

    public static Intent newIntent(Context context, String email) {
        Intent intent = new Intent(context, UserView.class);
        intent.putExtra(EXTRA_ID, email);
        return intent;
    }
}
