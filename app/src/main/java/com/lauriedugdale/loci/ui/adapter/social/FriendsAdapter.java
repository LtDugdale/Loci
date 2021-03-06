package com.lauriedugdale.loci.ui.adapter.social;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lauriedugdale.loci.R;
import com.lauriedugdale.loci.data.GroupDatabase;
import com.lauriedugdale.loci.data.UserDatabase;
import com.lauriedugdale.loci.data.dataobjects.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mnt_x on 21/06/2017.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {


    // Store the context and cursor for easy access
    private Context mContext;
    private List<User> mUsers;

    private UserDatabase mUserDatabase;
    private GroupDatabase mGroupDatabase;

    private boolean isAdmin;

    // This interface handles clicks on items within this Adapter. This is populated from the constructor
    // Call the instance in this variable to call the onClick method whenever and item is clicked in the list.
    final private SocialAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface SocialAdapterOnClickHandler {
        void onSocialClick(User user);
    }

    /**
     * Entry adapter constructor
     *
     * @param context
     * @param clickHandler
     */
    public FriendsAdapter(Context context, SocialAdapterOnClickHandler clickHandler, boolean isAdmin) {
        this.mContext = context;
        this.mClickHandler = clickHandler;
        mUsers = new ArrayList<User>();
        mUserDatabase = new UserDatabase(context);
        mGroupDatabase = new GroupDatabase(context);
        this.isAdmin = isAdmin;
    }

    public void addToUsers(User user){
        mUsers.add(user);
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    /**
     * Inflates a layout depending on its position and returns a ViewHolder
     */
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View contactView = null;

        // inflate second item layout & return that viewHolder
        contactView = inflater.inflate(R.layout.item_social_entry, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    /**
     * Populates data into the layout through the viewholder
     */
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        final User user = mUsers.get(position);

        // set username
        viewHolder.mName.setText(user.getUsername());

        // set profile picture
        mUserDatabase.downloadNonLoggedInProfilePic(user.getUserID(), viewHolder.mProfilePic, R.drawable.default_profile);

        if (isAdmin) {

            viewHolder.mRightArrow.setVisibility(View.GONE);
            viewHolder.mOptions.setVisibility(View.VISIBLE);

            viewHolder.mOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //creating a popup menu
                    final PopupMenu popup = new PopupMenu(mContext, viewHolder.mOptions);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.friend_options);

                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.remove_friend:
                                    mUserDatabase.removeFriend(user.getUserID());
                                    mUsers.remove(position);
                                    notifyDataSetChanged();
                                    break;
                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    popup.show();
                }
            });
        }
    }


    @Override
    /**
     * return the total count of items in the list
     */
    public int getItemCount() {
        if (mUsers == null) {
            return 0;
        }
        return mUsers.size();
    }

    /**
     * CLASS
     * Used to cache the views within the layout for quick access
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // The UI elements
        public TextView mName;
        public ImageView mProfilePic;
        public ImageView mRightArrow;
        public TextView mViewOptions;
        public TextView mOptions;

        public ViewHolder(View itemView) {
            super(itemView);

            // Find the UI elements
            mName = (TextView) itemView.findViewById(R.id.ise_name);
            mProfilePic = (ImageView) itemView.findViewById(R.id.ise_profile_pic);
            mRightArrow = (ImageView) itemView.findViewById(R.id.ise_right_arrow);
            mOptions = (TextView) itemView.findViewById(R.id.ise_view_options);


            // set the listener as this class
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            mClickHandler.onSocialClick(mUsers.get(pos));
        }
    }
}