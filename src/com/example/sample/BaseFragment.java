package com.example.sample;

import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
	private boolean mShowingChild;
	protected SearchFragmentListener mListener;
	
	protected boolean isShowingChild(){
		return mShowingChild;
	}
	
	protected void setShowingChild(boolean whichChild){
		mShowingChild = whichChild;
	}
}
