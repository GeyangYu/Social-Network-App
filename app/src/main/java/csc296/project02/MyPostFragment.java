package csc296.project02;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import csc296.project02.database.DbCursorWrapper;
import csc296.project02.model.FeedItem;
import csc296.project02.model.User;

public class MyPostFragment extends Fragment {
    private DbCursorWrapper mDb;
    private RecyclerView mRecyclerView;
    private UserAdapter mAdapter;

    private User mUserOwn = MainActivity.USER;

    public MyPostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDb = DbCursorWrapper.get(getContext());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_post, container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_my);
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

        List<FeedItem> feedItems = mDb.getMyPost(mUserOwn);
        if(mAdapter == null) {
            mAdapter = new UserAdapter(feedItems);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setUsers(feedItems);
        }
    }

    private class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
        private static final String TAG = "UserAdapter";
        private List<FeedItem> mFeedItem;

        public UserAdapter(List<FeedItem> feedItems) {
            mFeedItem = feedItems;
        }

        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.d(TAG, "onCreateViewHolder() called");
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.feed_list, parent, false);
            return new UserViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(UserViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder(holder," + position +") called");
            FeedItem feedItem = mFeedItem.get(position);
            holder.bind(feedItem);
        }

        @Override
        public int getItemCount() {
            Log.d(TAG, "getItemCount() returning " + mFeedItem.size());
            return mFeedItem.size();
        }

        public void setUsers(List<FeedItem> feedItems) {
            mFeedItem = feedItems;
            notifyDataSetChanged();
        }
    }

    private class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView mName;
        private TextView mDate;
        private TextView mContent;
        private ImageView mPhoto;
        private Button mDelete;

        public UserViewHolder(View itemView) {
            super(itemView);
            mName = (TextView)itemView.findViewById(R.id.textView2);
            mDate = (TextView)itemView.findViewById(R.id.textView3);
            mContent = (TextView)itemView.findViewById(R.id.textView4);
            mPhoto = (ImageView)itemView.findViewById(R.id.imageView);
            mDelete = (Button) itemView.findViewById(R.id.button_delete);
        }

        public void bind(final FeedItem feedItem) {
            DbCursorWrapper dbUser = new DbCursorWrapper(getContext());

            User mUser = dbUser.checkEmail(feedItem.getEmail());
            mName.setText(mUser.getFullName());
            mDate.setText(feedItem.getPostedDate());
            mContent.setText(feedItem.getContent());
            mDelete.setVisibility(View.VISIBLE);
            if (feedItem.getPhotoPath() != null) {
                mPhoto.setImageURI(Uri.parse(feedItem.getPhotoPath()));
            }
            else {
                mPhoto.setVisibility(View.INVISIBLE);
            }
            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DbCursorWrapper.get(getContext()).deleteFeedItem(feedItem);
                    updateUI();
                }
            });
        }
    }
}
