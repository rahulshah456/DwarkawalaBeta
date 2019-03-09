package Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import Fragments.DealsFragment;
import Fragments.FeedsFragment;

public class HomePagerAdapter extends FragmentStatePagerAdapter {


    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new FeedsFragment();
        }
        else if (position == 1)
        {
            fragment = new DealsFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Feeds";
        }
        else if (position == 1)
        {
            title = "Deals";
        }
        return title;
    }
}
