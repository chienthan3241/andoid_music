package com.example.sample;

import java.util.ArrayList;
import java.util.List;

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
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;



@SuppressWarnings("deprecation")
public class MainActivity extends FragmentActivity implements ActionBar.TabListener {	
	
	/**
	 * my variables
	 */
	private static RequestQueue queue = null;
	private static JSONObject searchjson = null;
	private static String search_format = "";
	private static List<single_track> SearchListItems = new ArrayList<single_track>();
	private static ListView SearchlistView;
	private static searchTrackList SearchListAdapter;
	
	
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
        SearchListAdapter=new searchTrackList(this, SearchListItems); 

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
    		final DelayAutoCompleteTextView titletxt = (DelayAutoCompleteTextView)rootView.findViewById(R.id.titleinput);
    		final ToggleButton typebtn = (ToggleButton)rootView.findViewById(R.id.searchtype);
    		final DelayAutoCompleteTextView albumtxt = (DelayAutoCompleteTextView)rootView.findViewById(R.id.albuminput);
    		final DelayAutoCompleteTextView artisttxt = (DelayAutoCompleteTextView)rootView.findViewById(R.id.artistinput);
    		searchjson = null;
    		search_format = "";
    		rootView.findViewById(R.id.titleinput).requestFocus();
    		titletxt.setThreshold(0);
    		titletxt.setAdapter(new autocompleteSearchTitle(rootView.getContext()));
    		titletxt.setLoadingIndicator((android.widget.ProgressBar) rootView.findViewById(R.id.pb_loading_indicator));
    		titletxt.setOnItemClickListener(new AdapterView.OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					String title = (String) parent.getItemAtPosition(position);
					titletxt.setText(title);
				}
    			
    		});
    		albumtxt.setThreshold(0);
    		albumtxt.setAdapter(new autocompleteSearchAlbum(rootView.getContext()));
    		albumtxt.setLoadingIndicator((android.widget.ProgressBar) rootView.findViewById(R.id.al_loading_indicator));
    		albumtxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					String album = (String) parent.getItemAtPosition(position);
					albumtxt.setText(album);
				}
    			
			});
    		artisttxt.setThreshold(0);
    		artisttxt.setAdapter(new autocompleteSearchArtist(rootView.getContext()));
    		artisttxt.setLoadingIndicator((android.widget.ProgressBar) rootView.findViewById(R.id.art_loading_indicator));
    		artisttxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					String name = (String) parent.getItemAtPosition(position);
					artisttxt.setText(name);
					
				}
			});
    		//tootgle button clicked
    		typebtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						titletxt.setVisibility(View.VISIBLE);
						albumtxt.setVisibility(View.GONE);
						albumtxt.setText("");
						titletxt.requestFocus();
					}else{
						titletxt.setVisibility(View.GONE);
						titletxt.setText("");
						albumtxt.setVisibility(View.VISIBLE);
						albumtxt.requestFocus();
					}					
				}
			});

    		//button search clicked
    		rootView.findViewById(R.id.searchbtn).setOnClickListener(new View.OnClickListener() {
    			
				@Override
				public void onClick(View view) {
					String titletext = titletxt.getText().toString().trim();
					String artisttext = artisttxt.getText().toString().trim();
					String albumtext = albumtxt.getText().toString().trim();
					if(typebtn.isChecked()){
						//track search
						if(titletext.equals("") && artisttext.equals("") ){						
							//show alert
							final AlertDialog arlertDialog = new AlertDialog.Builder(view.getContext()).create();
							arlertDialog.setTitle("warning!!");
					        arlertDialog.setMessage("wtf! what do you want to search??");
					        arlertDialog.setButton("OK", new DialogInterface.OnClickListener() {			
								@Override
								public void onClick(DialogInterface dialog, int which) {
									arlertDialog.cancel();
									titletxt.requestFocus();
								}
							});
					        arlertDialog.show();
						} else{		
								//buil request
								String rq = "";
								rq = "https://api.spotify.com/v1/search?q=";
								if(!titletext.equals("")){
									rq=rq+"track:"+titletext.replace(" ", "%20");
									if(!artisttext.equals(""))
										rq=rq+"+artist:"+artisttext.replace(" ", "%20");
								}else{
									rq=rq+"artist:"+artisttext.replace(" ", "%20");
								}
									rq=rq+"&type=track";								
									Log.v("meeeeeeeee",rq);
								JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, rq, null,
									    new Response.Listener<JSONObject>() 
									    {
									        @Override
									        public void onResponse(JSONObject response) {   
									            searchjson = response;
									            search_format = "TRACK";									          
									        	
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
					}else{
						//album search
						if(albumtxt.equals("") && artisttext.equals("") ){						
							//show alert
							final AlertDialog arlertDialog = new AlertDialog.Builder(view.getContext()).create();
							arlertDialog.setTitle("warning!!");
					        arlertDialog.setMessage("wtf! what do you want to search??");
					        arlertDialog.setButton("OK", new DialogInterface.OnClickListener() {			
								@Override
								public void onClick(DialogInterface dialog, int which) {
									arlertDialog.cancel();
									albumtxt.requestFocus();
								}
							});
					        arlertDialog.show();
						} else{		
								//buil request
								String rq = "";
								rq = "https://api.spotify.com/v1/search?q=";
								if(!albumtext.equals("")){
									rq=rq+"album:"+albumtext.replace(" ", "%20");
									if(!artisttext.equals(""))
										rq=rq+"+artist:"+artisttext.replace(" ", "%20");
								}else{
									rq=rq+"artist:"+artisttext.replace(" ", "%20");
								}
									rq=rq+"&type=album";								
									Log.v("meeeeeeeee",rq);
								JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, rq, null,
									    new Response.Listener<JSONObject>() 
									    {
									        @Override
									        public void onResponse(JSONObject response) {   
									            searchjson = response;
									           search_format = "ALBUM";									           
									        	
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
    		JSONArray items = null;
    		JSONObject item = null;
    		SearchListItems.clear();
    		SearchlistView = (ListView) rootView.findViewById(R.id.listsearch);
    		if(search_format == "TRACK"){
    			try {
					items = searchjson.getJSONObject("tracks").getJSONArray("items");
					for(int i = 0; i<items.length();i++){
						item = items.getJSONObject(i);
						single_track track_tmp = new single_track();
						track_tmp.setId(item.getString("id"));
						track_tmp.setTitle(item.getString("name"));
						track_tmp.setArtist(item.getJSONArray("artists").getJSONObject(0).getString("name"));
						track_tmp.setThumbnailUrl(item.getJSONObject("album").getJSONArray("images").getJSONObject(2).getString("url"));
						track_tmp.setInfo("Album: "+item.getJSONObject("album").getString("name")+
										"\t Disc_nr: "+item.getString("disc_number")+
										"\t track_nr: "+item.getString("track_number") + 
										"\t duration: "+ String.format("%.2f", (float)(Integer.parseInt(item.getString("duration_ms"))/60000.0f))+" min.");
						SearchListItems.add(track_tmp);						
					}					
					SearchListAdapter.notifyDataSetChanged();					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}else if(search_format == "ALBUM"){
    			try {
					items = searchjson.getJSONObject("albums").getJSONArray("items");
					for(int i = 0; i<items.length();i++){
						item = items.getJSONObject(i);
						final single_track album_tmp = new single_track();
						album_tmp.setId(item.getString("id"));
						album_tmp.setTitle(item.getString("name"));
						album_tmp.setArtist(item.getString("album_type"));
						album_tmp.setThumbnailUrl(item.getJSONArray("images").getJSONObject(2).getString("url"));
						album_tmp.setInfo(item.getString("id"));
						SearchListItems.add(album_tmp);		
					}
					SearchListAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		
    		SearchlistView.setAdapter(SearchListAdapter);
    		return rootView;
    	}
    	
    	public static  SearchContentSectionFragment newInstance(String title){
    		return new SearchContentSectionFragment(title);
    	}
    }   
  
}
