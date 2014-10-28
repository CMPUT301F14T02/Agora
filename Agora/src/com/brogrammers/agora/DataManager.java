package com.brogrammers.agora;

import java.util.List;

public interface DataManager {
	List<Question> getQuestions();
	Question getQuestionById(Long id);
	boolean pushQuestion(Question q);
	boolean pushAnswer(Answer a, Long qID);
	boolean pushComment(Comment c, Long qID, Long aID);
}
