package csc296.project02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import csc296.project02.database.DbCursorWrapper;
import csc296.project02.model.User;

public class UserListFragment extends android.support.v4.app.Fragment {
    static User USER;
    private static final String TAG = "UserListFragment";
    private DbCursorWrapper mDb;
    private RecyclerView mRecyclerView;
    private UserAdapter mAdapter;

    private User mUserOwn = MainActivity.USER;
    public UserListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDb = DbCursorWrapper.get(getContext());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_user);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        updateUI();
        Log.d("user", mUserOwn.getFullName());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void updateUI() {

        List<User> users = mDb.getEach(mUserOwn);
        Log.d(TAG, users.toString());
        if(mAdapter == null) {
            mAdapter = new UserAdapter(users);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setUsers(users);
        }
    }

    private class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
        private static final String TAG = "UserAdapter";
        private List<User> mUsers;

        public UserAdapter(List<User> users) {
            mUsers = users;
        }

        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.d(TAG, "onCreateViewHolder() called");
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.user_list, parent, false);
            return new UserViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(UserViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder(holder," + position +") called");
            User user = mUsers.get(position);
            holder.bind(user);
        }

        @Override
        public int getItemCount() {
            Log.d(TAG, "getItemCount() returning " + mUsers.size());
            return mUsers.size();
        }


        public void setUsers(List<User> users) {
            mUsers = users;
            notifyDataSetChanged();
        }
    }

    private class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mName;
        private ImageButton mFavorite;
        private User mUser;

        public UserViewHolder(View itemView) {
            super(itemView);
            mName = (TextView)itemView.findViewById(R.id.text_view_name);
            mFavorite = (ImageButton)itemView.findViewById(R.id.imageButton_favorite);
        }

        public void bind(User users) {
            mUser = users;
            mName.setText(users.getFullName());
            final boolean ifF ;
            ifF = DbCursorWrapper.get(getContext()).ifFavorite(mUserOwn.getEmail(), mUser.getEmail());
            if (ifF) {
                mFavorite.setImageResource(R.drawable.star);
            }
            else {
                mFavorite.setImageResource(R.drawable.star0);
            }

            mFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (DbCursorWrapper.get(getContext()).ifFavorite(mUserOwn.getEmail(), mUser.getEmail())) {
                        mFavorite.setImageResource(R.drawable.star0);
                        DbCursorWrapper.get(getContext()).deleteFavorite(mUserOwn.getEmail(), mUser.getEmail());
                    }
                    else {
                        mFavorite.setImageResource(R.drawable.star);
                        DbCursorWrapper.get(getContext()).insertFavorite(mUserOwn.getEmail(), mUser.getEmail());
                    }
                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            UserViewFragment.ifFromF = false;
            USER = mUser;
            Intent intent = UserView.newIntent(getContext(), mUser.getEmail());
            getContext().startActivity(intent);
        }
    }
}
