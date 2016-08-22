package com.evansappwriter.liveyourlist.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.evansappwriter.liveyourlist.R;
import com.evansappwriter.liveyourlist.ui.BaseActivity;
import com.evansappwriter.liveyourlist.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by markevans on 7/25/16.
 */
public class UserListActivity extends BaseActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setupWithViewPager(viewPager);
//        Bundle b = getIntent().getExtras();
//
//        int code = (getClass().getName() + "UserListFragment").hashCode();
//        Utils.printLogInfo("FRAG", "id: ", code);
//
//        FragmentManager fm = getSupportFragmentManager();
//        if (fm.findFragmentByTag("f" + code) == null) // first time in the activity
//        {
//            Utils.printLogInfo("FRAG", 'f', code);
//            Fragment f = UserListFragment.newInstance(b);
//
//            FragmentTransaction ft = fm.beginTransaction();
//            ft.replace(R.id.fragment_holder, f, "f" + code);
//            ft.commit();
//        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(UserListFragment.newInstance(getIntent().getExtras()), "Businesses");
        adapter.addFragment(UserListFragment.newInstance(getIntent().getExtras()), "Places");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
