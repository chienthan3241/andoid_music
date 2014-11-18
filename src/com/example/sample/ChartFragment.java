package com.example.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ChartFragment extends Fragment {
	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);

         // Demonstration of a collection-browsing activity.
        /*
         rootView.findViewById(R.id.demo_collection_button)
                 .setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         Intent intent = new Intent(getActivity(), CollectionDemoActivity.class);
                         startActivity(intent);
                     }
                 });

         // Demonstration of navigating to external activities.
         rootView.findViewById(R.id.demo_external_activity)
                 .setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         // Create an intent that asks the user to pick a photo, but using
                         // FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET, ensures that relaunching
                         // the application from the device home screen does not return
                         // to the external activity.
                         Intent externalActivityIntent = new Intent(Intent.ACTION_PICK);
                         externalActivityIntent.setType("image/*");
                         externalActivityIntent.addFlags(
                                 Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                         startActivity(externalActivityIntent);
                     }
                 });
                 */           
      // itunes-store clicked.
         rootView.findViewById(R.id.imageitunes)
                 .setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                     	Log.v("MyApp","Itunes clicked");
                     }
                 });
         
      // google-store clicked.
         rootView.findViewById(R.id.imagegoogleplay)
                 .setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                     	Log.v("MyApp","Google clicked");
                     }
                 });
      // Spotify-store clicked.
         rootView.findViewById(R.id.imagespotify)
                 .setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                     	Log.v("MyApp","Spotify clicked");
                     }
                 });
      // Amazon-store clicked.
         rootView.findViewById(R.id.imageamazon)
                 .setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                     	Log.v("MyApp","Amazon clicked");
                     }
                 });
      // Napster-store clicked.
         rootView.findViewById(R.id.imagenapster)
                 .setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                     	Log.v("MyApp","Napster clicked");
                     }
                 });
      // Lastfm-store clicked.
         rootView.findViewById(R.id.imagelastfm)
                 .setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                     	Log.v("MyApp","Lastfm clicked");
                     }
                 });

         return rootView;
     }
}
