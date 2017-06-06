package info.androidhive.materialdesign.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import info.androidhive.materialdesign.activity.ChatListFragment;
import info.androidhive.materialdesign.activity.FavFragment;
import info.androidhive.materialdesign.activity.Search;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {

	CharSequence Titles[];
	int NumbOfTabs;

	public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[],
			int mNumbOfTabsumb) {
		super(fm);
		this.Titles = mTitles;
		this.NumbOfTabs = mNumbOfTabsumb;
	}
	// This method return the fragment for the every position in the View Pager
	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			return new Search();

		case 1:
			return new ChatListFragment();
		case 2:
			return new FavFragment();
		}
		return null;
	}
	// This method return the titles for the Tabs in the Tab Strip
	@Override
	public CharSequence getPageTitle(int position) {
		return Titles[position];
	}
	// This method return the Number of tabs for the tabs Strip
	@Override
	public int getCount() {
		return NumbOfTabs;
	}
}