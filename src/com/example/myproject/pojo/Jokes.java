package com.example.myproject.pojo;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Cache
public class Jokes {

	@Id
	private long id;
	private String joke;
	@Index
    private List<String> category;
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
	public List<String> getCategory() {
		return category;
	}
	public void setCategory(List<String> category) {
		this.category = category;
	}
		
}
