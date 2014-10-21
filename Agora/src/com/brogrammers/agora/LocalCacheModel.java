package com.brogrammers.agora;

public class LocalCacheModel {
	static private LocalCacheModel self = null;
	
	static public LocalCacheModel getLocalCacheModel() {
		if (self == null) {
			self = new LocalCacheModel();
		}
		return self;
	}
}
