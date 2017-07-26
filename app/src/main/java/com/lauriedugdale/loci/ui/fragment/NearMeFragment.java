package com.lauriedugdale.loci.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.lauriedugdale.loci.EntriesDownloadedListener;
import com.lauriedugdale.loci.R;
import com.lauriedugdale.loci.data.DataUtils;
import com.lauriedugdale.loci.data.dataobjects.GeoEntry;
import com.lauriedugdale.loci.utils.LocationUtils;


/**
 * @author Laurie Dugdale
 */

public class NearMeFragment extends BaseFragment implements EntriesDownloadedListener {

    public static final String TAG = "NearMeFragment";

    private DataUtils mDataUtils;

    private double mLatitute;
    private double mLongitude;

    private BroadcastReceiver mDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "received location broadcast");

            if ("location_update".equals(intent.getAction())) {
                mLatitute = intent.getDoubleExtra("latitude", 0L);
                mLongitude = intent.getDoubleExtra("longitude", 0L);
            }

        }
    };

    public static NearMeFragment create(){
        return new NearMeFragment();
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mDataUtils = new DataUtils(getActivity());
    }

    public void fetchEntries(){
        LocationUtils.toBounds(mLatitute, mLongitude, LocationUtils.QUERY_RADIUS);
    }

    @Override
    public void onEntriesFetched() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_near_me,container, false);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_near_me;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onResume() {
        super.onResume();


        IntentFilter filter = new IntentFilter("location_update");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mDataReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mDataReceiver);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem notificationItem = menu.findItem(R.id.action_notification);
        MenuItem filterItem = menu.findItem(R.id.action_filter);
        MenuItem locationItem = menu.findItem(R.id.action_location);
        MenuItem arItem = menu.findItem(R.id.action_ar);
        MenuItem addGroupItem = menu.findItem(R.id.action_add_group);

        addGroupItem.setVisible(false);
        filterItem.setVisible(false);
        locationItem.setVisible(false);
        arItem.setVisible(false);
        notificationItem.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return false;
    }

}
