package com.example.sample;

import com.example.sample.MainActivity.SearchContentSectionFragment;
import com.example.sample.MainActivity.SearchSectionFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AppSectionsPagerAdapter extends FragmentPagerAdapter{
	
	 private BaseFragment mFragmentAtPos1; // Fragment at index 1
     private BaseFragment mFragmentAtPos2; // Fragment at index 2
	 private final FragmentManager mFragmentManager;
	 
	 public AppSectionsPagerAdapter(FragmentManager fm) {
         super(fm);
         mFragmentManager = fm;
     }

	@Override
	public Fragment getItem(int i) {
		switch (i) {
        case 0:
            // The first section of the app is the most interesting -- it offers
            // a launchpad into the other demonstrations in this example application.
            return new ChartFragment();
        case 1:
        	// section of search + searchcontent in nest Fragment        	
        	if(mFragmentAtPos1 == null){
        		mFragmentAtPos1 = SearchSectionFragment.newInstance(new SearchFragmentListener() {
					
					@Override
					public void onSwitchToNextFragment() {
						mFragmentManager.beginTransaction().remove(mFragmentAtPos1).commit();
						mFragmentAtPos1 = 	SearchContentSectionFragment.newInstance("search result");
						mFragmentAtPos1.setShowingChild(true);
						notifyDataSetChanged();
					}
				});
        	}
        	return mFragmentAtPos1;
        default:
            // The other sections of the app are dummy placeholders.
            Fragment fragment = new DummySectionFragment();
            Bundle args = new Bundle();
            args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
            fragment.setArguments(args);
            return fragment;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 4;
	}
	
	@Override
	public  int getItemPosition(Object object){
		if (object instanceof SearchSectionFragment && mFragmentAtPos1 instanceof SearchContentSectionFragment) {
            return POSITION_NONE;
        } else if(object instanceof SearchContentSectionFragment){
        	return POSITION_NONE;
        }
		return POSITION_UNCHANGED;
	}
		
	@Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
        	case 0: 
        		return "Top Charts";
        	case 1: 
        		return "Search";
        	case 2:
        		return "Events";
        	default: 
        		return "My Playlist";
        }
    }
	
	public void replaceSearchContentFragment(BaseFragment oldFrg, int position){
		switch (position) {
		case 1:
			mFragmentManager.beginTransaction().remove(oldFrg).commit();
			mFragmentAtPos1 = SearchSectionFragment.newInstance(new SearchFragmentListener() {
				
				@Override
				public void onSwitchToNextFragment() {
					mFragmentManager.beginTransaction().remove(mFragmentAtPos1).commit();
					mFragmentAtPos1 = SearchContentSectionFragment.newInstance("search result");
					mFragmentAtPos1.setShowingChild(true);
					notifyDataSetChanged();
				}
			});
			notifyDataSetChanged();
			break;

		default:
			break;
		}
	}
}
