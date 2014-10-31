package com.brogrammers.agora;

import java.lang.reflect.Array;

public class WebServiceModel {
	static private WebServiceModel self;
	private Array alist;
	
	private WebServiceModel() {
		// TODO Auto-generated constructor stub
	}
	
	static public WebServiceModel getModel() {
		if (self == null) {
			self = new WebServiceModel();
		}
		return self;
	}

	public Question searchQuestions(String string) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
