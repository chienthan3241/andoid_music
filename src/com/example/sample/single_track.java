package com.example.sample;

public class single_track {
	private String title, artist, thumbnailUrl, info, id;
	
	public single_track(){
		
	}
	public single_track(String title, String artist, String thumbnailUrl, String info, String id){
		this.title = title;
		this.artist = artist;
		this.thumbnailUrl = thumbnailUrl;
		this.info = info;
		this.id = id;
	}
	
	public String getId(){
		return this.id;
	}
	
	public void setId(String id) {
        this.id = id;
    }
	
	public String getTitle(){
		return this.title;
	}
	
	public void setTitle(String name) {
        this.title = name;
    }
	
	public String getArtist(){
		return this.artist;
	}
	
	public void setArtist(String name) {
        this.artist = name;
    }
	
	public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }
	
	public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
	
	public String getInfo(){
		return this.info;
	}
	
	public void setInfo(String name) {
        this.info = name;
    }
}
