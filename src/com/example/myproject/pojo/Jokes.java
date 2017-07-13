package com.example.myproject.pojo;

import java.util.ArrayList;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Cache
public class Jokes {

	@Id
	private long id;
	private String joke;
    private ArrayList<String> category;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getJoke() {
		return joke;
	}
	public void setJoke(String joke) {
		this.joke = joke;
	}
	public ArrayList<String> getCategory() {
		return category;
	}
	public void setCategory(ArrayList<String> category) {
		this.category = category;
	}
	
	
	
}
