package com.example.sample;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;



public class MainActivity extends FragmentActivity implements ActionBar.TabListener {	
	
	/**
	 * my variables
	 */
	private static RequestQueue queue = null;
	static JSONObject searchjson = null;
	static String search_format = "";
	static ArrayList<String> SearchListItems = new ArrayList<String>();
	static ArrayAdapter<String> SearchListAdapter;
	
	
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    static AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;   


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //check internet connect status      
        if(!isOnline()){
        	showAlert("internet connection","you muss have internet connection to access streaming Music!! \n try to connect with WLAN or 3G or LTE or something like that!!",true);
        }
        
        //init Volley Request Queue
        queue = Volley.newRequestQueue(this);
        
        //init Search_list
        SearchListAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, SearchListItems);

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }
    
    public void onBackPressed(){
    	Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" +R.id.pager + ":" + mViewPager.getCurrentItem());
    	if(fragment != null && fragment instanceof BaseFragment){
    		if(fragment.getView() != null){
    			BaseFragment bf = (BaseFragment)fragment;
    			if(bf.isShowingChild()){
    				replaceChild(bf, mViewPager.getCurrentItem());
    			} else {
    				backButton();
    			}
    		}
    	}
    }
    
 // Back Button Pressed
 	private void backButton() {
 		finish();
 	}
    
    public boolean isOnline(){
    	ConnectivityManager cm =
    	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    	    return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    
    public void showAlert(String title, String message, boolean quitwhenclick){
    	final AlertDialog arlertDialog = new AlertDialog.Builder(this).create();
        arlertDialog.setTitle(title);
        arlertDialog.setMessage(message);
        if(quitwhenclick){
	        arlertDialog.setButton("OK", new DialogInterface.OnClickListener() {			
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					finish();
		            System.exit(0);
				}
			});
        } else {
        	arlertDialog.setButton("OK", new DialogInterface.OnClickListener() {			
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					arlertDialog.cancel();
				}
			});
        }
        arlertDialog.show();
    }

    public void replaceChild(BaseFragment oldFrg, int position){
    	mAppSectionsPagerAdapter.replaceSearchContentFragment(oldFrg, position);
    }
    
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

   /**
     * Fragment Search
     */
    public static  class SearchSectionFragment extends BaseFragment {    
    	public SearchSectionFragment(){
    		
    	}
    	
    	public SearchSectionFragment(SearchFragmentListener listener){
    		mListener = listener;
    	}
    	
    	@Override
    	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
    		
    		final View rootView = inflater.inflate(R.layout.fragment_section_search, container, false);
    		final View contentView = inflater.inflate(R.layout.fragment_section_search_content, container, false);
    		final EditText titletxt = (EditText)rootView.findViewById(R.id.titleinput);
    		final ToggleButton typebtn = (ToggleButton)rootView.findViewById(R.id.searchtype);
    		final EditText artisttxt = (EditText)rootView.findViewById(R.id.artistinput);
    		searchjson = null;
    		search_format = "";
    		//button search clicked
    		rootView.findViewById(R.id.searchbtn).setOnClickListener(new View.OnClickListener() {
    			
				@Override
				public void onClick(View view) {
					String titletext = titletxt.getText().toString();
					String artisttext = artisttxt.getText().toString();
					if(titletext.equals("") && artisttext.equals("")){						
							titletxt.setBackgroundColor(-65536);						
							artisttxt.setBackgroundColor(-16776961);
					} else{						
						String rq = "https://api.spotify.com/v1/search?q=track:happy&type=track";
						JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, rq, null,
							    new Response.Listener<JSONObject>() 
							    {
							        @Override
							        public void onResponse(JSONObject response) {   
							            searchjson = response;
							            if(typebtn.isChecked()){
							            	search_format = "TRACK";
							            }else{
							            	search_format = "ALBUM";
							            }
							        	
							        	if(mListener != null){
							        		mListener.onSwitchToNextFragment();
							        	}						        
							        	
							        }
							    }, 
							    new Response.ErrorListener() 
							    {
							         @Override
							         public void onErrorResponse(VolleyError error) {            
							            Log.v("Error.Response", "error");
							       }								
							    }
							);
						queue.add(getRequest);					
					}
				}
				
			});
    		
    		return rootView;
    	}
    	
    	public static  SearchSectionFragment newInstance(SearchFragmentListener listener){
    		return new SearchSectionFragment(listener);
    	}
    	
    }
    /**
     * Fragment Search content
     */
    public static  class SearchContentSectionFragment extends BaseFragment {
    	
    	private String mTitle;
    	
    	public SearchContentSectionFragment(String title){
    		mTitle = title;
    	}
    	
    	@Override
    	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
    		View rootView = inflater.inflate(R.layout.fragment_section_search_content, container, false);    		
    		ListView list = (ListView)rootView.findViewById(R.id.listsearch);
    		JSONArray items = null;
    		JSONObject item = null;
    		if(search_format == "TRACK"){
    			try {
					items = searchjson.getJSONObject("tracks").getJSONArray("items");
					for(int i = 0; i<items.length();i++){
						item = items.getJSONObject(i);
						SearchListItems.add(item.getString("name"));
					}
					SearchListAdapter.notifyDataSetChanged();					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		
    		list.setAdapter(SearchListAdapter);
    		
    		return rootView;
    	}
    	
    	public static  SearchContentSectionFragment newInstance(String title){
    		return new SearchContentSectionFragment(title);
    	}
    }   
  
}
