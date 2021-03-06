package com.lauriedugdale.loci.ui.adapter.search;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lauriedugdale.loci.R;
import com.lauriedugdale.loci.data.EntryStorage;
import com.lauriedugdale.loci.data.UserDatabase;
import com.lauriedugdale.loci.data.dataobjects.GeoEntry;
import com.lauriedugdale.loci.ui.activity.MainActivity;
import com.lauriedugdale.loci.utils.EntryUtils;
import com.lauriedugdale.loci.utils.LocationUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by mnt_x on 21/07/2017.
 */

public class SearchEntriesSection extends StatelessSection {

    private Context mContext;
    private List<GeoEntry> mEntries;
    private EntryStorage mEntryStorage;
    private UserDatabase mUserDatabase;


    public SearchEntriesSection(Context context) {

        // call constructor with layout resources for this Section header and items
        super(new SectionParameters.Builder(R.layout.item_files)
                .headerResourceId(R.layout.search_header_entry_section)
                .build());

        mContext = context;
        mEntries = new ArrayList<GeoEntry>();
        mEntryStorage = new EntryStorage(context);
        mUserDatabase = new UserDatabase(context);


    }

    public void addToEntries(GeoEntry entry) {
        mEntries.add(entry);
    }

    public void clearData(){
        mEntries.clear();
    }

    @Override
    public int getContentItemsTotal() {
        if (mEntries == null) {
            return 0;
        }
        return mEntries.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new SearchEntriesViewholder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder entriesViewHolder, final int position) {
        final SearchEntriesViewholder viewHolder = (SearchEntriesViewholder) entriesViewHolder;

        final GeoEntry entry = mEntries.get(position);
        viewHolder.mTitle.setText(entry.getTitle());

        // set file picture
//        EntryUtils.getFilePic(viewHolder.mFilePic, entry);
        mUserDatabase.downloadNonLoggedInProfilePic(entry.getCreator(), viewHolder.mFilePic, R.drawable.default_profile);


        // Set distance
        LocationUtils.displayDistance(viewHolder.mDistance, mContext, entry.getLatitude(), entry.getLongitude());
        // set author
        viewHolder.mAuthor.setText(entry.getCreatorName());

        viewHolder.mLocateFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.setAction("single_entry");
                intent.putExtra("entry", mEntries.get(position));
                mContext.startActivity(intent);
            }
        });


    }

    class SearchEntriesViewholder extends RecyclerView.ViewHolder {

        // The UI elements
        public TextView mDistance;
        public TextView mTitle;
        public ImageView mFilePic;
        public ImageView mLocateFile;
        public TextView mAuthor;

        public SearchEntriesViewholder(View itemView) {
            super(itemView);

            // Find the UI elements
            mTitle = (TextView) itemView.findViewById(R.id.if_name);
            mFilePic = (ImageView) itemView.findViewById(R.id.if_author_pic);
            mLocateFile = (ImageView) itemView.findViewById(R.id.if_locate_file);
            mDistance = (TextView) itemView.findViewById(R.id.info_bar_marker_distance);
            mAuthor = (TextView) itemView.findViewById(R.id.info_bar_marker_author);
        }
    }
}