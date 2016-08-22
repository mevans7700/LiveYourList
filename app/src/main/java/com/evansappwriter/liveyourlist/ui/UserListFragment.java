package com.evansappwriter.liveyourlist.ui;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.evansappwriter.liveyourlist.R;
import com.evansappwriter.liveyourlist.model.User;
import com.evansappwriter.liveyourlist.util.AltListAdapter;
import com.evansappwriter.liveyourlist.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by markevans on 7/25/16.
 */
public class UserListFragment extends BaseListFragment<User> {
    private static final String TAG = "UserListFragment";

    private BaseActivity mActivity;

    private boolean mFirstAPI;

    // empty public constructor
    // read here why this is needed:
    // http://developer.android.com/reference/android/app/Fragment.html
    @SuppressWarnings("unused")
    public UserListFragment() {

    }

    public static UserListFragment newInstance(Bundle b) {
        UserListFragment f = new UserListFragment();
        if (b != null) {
            f.setArguments(b);
        }
        f.setHasOptionsMenu(true);
        return f;
    }

    @Override
    protected AltListAdapter<User> onCreateEmptyAdapter() {
        return new AltListAdapter<User>(){
            @Override
            public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_row, parent, false);

                final UserHolder holder = new UserHolder(v);
                v.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        int itemPos = holder.getAdapterPosition();
                        User user = getItem(itemPos);
                        Toast.makeText(mActivity, user.toString(), Toast.LENGTH_SHORT).show();
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
        List<User> users = new ArrayList<>();
        users.add(new User("Mark","Evans"));
        users.add(new User("Joe","Bob"));
        users.add(new User("Joe","Blow"));
        showNew(users);
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
