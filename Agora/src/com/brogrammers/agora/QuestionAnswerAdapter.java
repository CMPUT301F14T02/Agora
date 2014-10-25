package com.brogrammers.agora;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionAnswerAdapter extends BaseExpandableListAdapter {

	private Question question;
	private LayoutInflater inflater;
	private List<Answer> answers = new ArrayList<Answer>();
	
	QuestionAnswerAdapter(Question q) {
		this.question = q;
		this.inflater = (LayoutInflater)Agora.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		this.answers = q.getAnswers();
		Answer a = (new Answer("Thunderwave is OP because of the paralysis", null, new Author("BingsF")));
		a.upvote();
		answers.add(a);
//		Toast.makeText(Agora.getContext(), q.getBody(), 0).show();
	}
	
	@Override
	public Object getChild(int groupPos, int childPos) {
		Post post = (Post)getGroup(groupPos);
		return post.getComments().get(childPos);
	}

	@Override
	public long getChildId(int groupPos, int childPos) {
		return childPos;
	}

	@Override
	public View getChildView(int groupPos, int childPos, boolean isLastChild, View convertView,
			ViewGroup parent) {
		String commentBody = ((Comment)(getChild(groupPos, childPos))).getBody();
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_question_answer_comment, null);
		}
		
		((TextView)convertView.findViewById(R.id.commentText)).setText(commentBody);
		return convertView;
		// TODO: make the last child have an EditText for adding a comment
	}

	@Override
	public int getChildrenCount(int groupPos) {
		if (groupPos == 0) {
			return ((Question)getGroup(0)).countComments();
		} else {
			return ((Answer)getGroup(groupPos)).countComments();
		}
	}

	@Override
	public Object getGroup(int groupPos) {
		if (groupPos == 0) {
			return question;
		} else {
			return answers.get(groupPos-1);
		}
	}

	@Override
	public int getGroupCount() {
		return answers.size()+1; 
	}

	@Override
	public long getGroupId(int pos) {
		return pos;
	}

	@Override
	public View getGroupView(int groupPos, boolean isExpanded, View convertView, ViewGroup parent) {
//		Toast.makeText(Agora.getContext(), question.getBody(), 0).show();
		if (convertView == null) {
			if (groupPos == 0) {
				convertView = inflater.inflate(R.layout.activity_question_answer_question, null);
				((TextView)convertView.findViewById(R.id.questionBody)).setText(question.getBody());
				((TextView)convertView.findViewById(R.id.questionTitle)).setText(question.getTitle());
				((TextView)convertView.findViewById(R.id.questionRating)).setText(Integer.toString(question.getRating()));
				if (isExpanded) {
					((ImageButton)convertView.findViewById(R.id.questionExpand)).setImageResource(R.drawable.ic_action_collapse);	
				} else {
					((ImageButton)convertView.findViewById(R.id.questionExpand)).setImageResource(R.drawable.ic_action_expand);	
				}
				List<Long> favoritedQuestions = DeviceUser.getUser().getFavoritedQuestionIDs();
				if (favoritedQuestions.contains(question.getID())) {
					((ImageButton)convertView.findViewById(R.id.questionFavorite)).setImageResource(R.drawable.ic_action_rating_favoritepink);	
				} else {
					// TODO: change to grey
					((ImageButton)convertView.findViewById(R.id.questionFavorite)).setImageResource(R.drawable.ic_action_rating_favoritepink);	
				}
			} else {
				convertView = inflater.inflate(R.layout.activity_question_answer_answer, null);
				Answer answer = (Answer)getGroup(groupPos);
				((TextView)convertView.findViewById(R.id.answerBody)).setText(answer.getBody());
				((TextView)convertView.findViewById(R.id.answerRating)).setText(Integer.toString(answer.getRating()));
				if (isExpanded) {
					((ImageButton)convertView.findViewById(R.id.answerExpand)).setImageResource(R.drawable.ic_action_collapse);	
				} else {
					((ImageButton)convertView.findViewById(R.id.answerExpand)).setImageResource(R.drawable.ic_action_expand);	
				}
			}
		}
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return false;
	}



}
