package com.brogrammers.agora;

import java.util.List;

public class LocalCacheModel {
	static private LocalCacheModel self = null;
	static private List<QuestionPreview> questionPreviewList = null;
	
	static public LocalCacheModel getLocalCacheModel() {
		if (self == null) {
			self = new LocalCacheModel();
		}
		return self;
	}
	
	static public void setQuestionPreviewList(List<QuestionPreview> qpList){
		questionPreviewList = qpList;
		//notifyUpdate();
	}
}
