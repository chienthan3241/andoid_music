package com.example.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class autocompleteSearchArtist extends BaseAdapter implements Filterable{
	private Context mContext;
	private List<String> resultList = new ArrayList<String>();
	
	public autocompleteSearchArtist(Context context){
		mContext = context;		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return resultList.size();
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return resultList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView tv;
		if(convertView==null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.simple_dropdown_list, parent, false);
		}
		tv = (TextView) convertView.findViewById(R.id.text1);
		tv.setText(getItem(position));
		return convertView;
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter(){

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				// TODO Auto-generated method stub
                final FilterResults filterResults = new FilterResults();
                List<String> resultlist = new ArrayList<String>();
                JSONArray items;           
                String rq = "";
                
                if (constraint != null) {                	
                		rq = "https://api.spotify.com/v1/search?q=artist:"+constraint.toString().replace(" ", "%20")+"&type=artist&limit=10";              	
                	
                	Log.v("mmmmmmmm",rq);
                	HttpClient httpclient = new DefaultHttpClient();
                	HttpGet httpget = new HttpGet(rq);
                	HttpResponse response;
                	StringBuilder builder = new StringBuilder();
                    try {
						response = httpclient.execute(httpget);
						StatusLine statusLine = response.getStatusLine();
						int statusCode = statusLine.getStatusCode();
						if(statusCode == 200){
			    			HttpEntity entity = response.getEntity();
			    			InputStream content = entity.getContent();
			    			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
			    			String line;
			    			while((line = reader.readLine()) != null){
			    				builder.append(line);
			    			}
			    		} else {
			    			Log.e("parse json","Failed");
			    		}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
                    
                    try {
						JSONObject json_result = new JSONObject(builder.toString());
						try {
							items = json_result.getJSONObject("artists").getJSONArray("items");							
							for(int i = 0; i<items.length();i++){
								resultlist.add(items.getJSONObject(i).getString("name"));
							}
							filterResults.values = resultlist;
							filterResults.count = resultlist.size();							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }

				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				// TODO Auto-generated method stub
                if (results != null && results.count > 0) {
                    resultList = (List<String>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }

			}
			
		};
		
		return filter;
	}

}
