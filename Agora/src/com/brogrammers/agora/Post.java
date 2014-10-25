package com.brogrammers.agora;

import java.util.List;

public interface Post {
	public Long getID();
	public Author getAuthor();
	public String getBody();
	public int getRating();
	public Long getDate();
	public List<Comment> getComments();
	
}
