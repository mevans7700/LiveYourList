package com.evansappwriter.liveyourlist.ui;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.evansappwriter.liveyourlist.R;
import com.evansappwriter.liveyourlist.model.Business;
import com.evansappwriter.liveyourlist.model.User;
import com.evansappwriter.liveyourlist.util.AltListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by markevans on 8/19/16.
 */
public class BusinessListFragment extends BaseListFragment<Business> {
    private static final String TAG = "BusinessListFragment";

    private BaseActivity mActivity;

    private boolean mFirstAPI;

    // empty public constructor
    // read here why this is needed:
    // http://developer.android.com/reference/android/app/Fragment.html
    @SuppressWarnings("unused")
    public BusinessListFragment() {

    }

    public static BusinessListFragment newInstance(Bundle b) {
        BusinessListFragment f = new BusinessListFragment();
        if (b != null) {
            f.setArguments(b);
        }
        f.setHasOptionsMenu(true);
        return f;
    }

    @Override
    protected AltListAdapter<Business> onCreateEmptyAdapter() {
        return new AltListAdapter<Business>(){
            @Override
            public BusinessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_row, parent, false);

                final BusinessHolder holder = new BusinessHolder(v);
                v.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        int itemPos = holder.getAdapterPosition();
                        Business business = getItem(itemPos);
                        Toast.makeText(mActivity, business.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

                return holder;
            }
        };
    }

    @Override
    protected boolean onSetupSwipeContainer() {
        return true;
    }

    @Override
    protected RecyclerView.LayoutManager onCreateLayoutManager() {
        return new LinearLayoutManager(mActivity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mActivity = (BaseActivity) context;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mActivity = (BaseActivity) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirstAPI = false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // item views are the same height and width
        getRecyclerView().setHasFixedSize(true);

        // Setup refresh listener which triggers new data loading
        getSwipeContainer().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isNetworkAvailable()) {
                    //Toast.makeText(mActivity, getString(R.string.error_no_connection), Toast.LENGTH_SHORT).show();
                    getSwipeContainer().setRefreshing(false);
                    return;
                }
                getNew();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!mFirstAPI) {
            getNew();
        }
        mFirstAPI = true;
    }

    @Override
    protected Bundle onPrepareGetNew() {
        Bundle params = new Bundle();
        //params.putString(APIService.PARAM_PAGE, "1");
        return params;
    }

    @Override
    protected Bundle onPrepareGetOlder() {
        Bundle params = new Bundle();
        //params.putString(APIService.PARAM_PAGE, String.valueOf(crtPage));
        return params;
    }


    protected void makeAPICall(final Bundle params) {
        List<Business> business = new ArrayList<>();
        business.add(new Business("Mark","Evans"));
        business.add(new Business("Joe","Bob"));
        business.add(new Business("Joe","Blow"));
        showNew(business);
        getSwipeContainer().setRefreshing(false);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }
}
