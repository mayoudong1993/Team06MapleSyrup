package ca.ualberta.app.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import ca.ualberta.app.activity.CreateAnswerActivity;
import ca.ualberta.app.activity.CreateAnswerReplyActivity;
import ca.ualberta.app.activity.CreateQuestionReplyActivity;
import ca.ualberta.app.activity.QuestionDetailActivity;
import ca.ualberta.app.activity.R;
import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.thread.UpdateAnswerThread;

public class AnswerListAdapter extends ArrayAdapter<Answer> {
	private ArrayList<Answer> answerList = null;
	private Question question;
	private Context context;

	public AnswerListAdapter(Context context, int singleAnswer,
			ArrayList<Answer> objects, Question question) {
		super(context, singleAnswer, objects);
		this.answerList = objects;
		this.question = question;
		this.context = context;
	}

	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(this.getContext());
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.single_answer, null);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.authorPic = (ImageView) convertView.findViewById(R.id.authorPic);
		holder.image = (ImageView) convertView.findViewById(R.id.answerImage);
		holder.authorName = (TextView) convertView
				.findViewById(R.id.authorNameTextView);
		holder.answerContent = (TextView) convertView
				.findViewById(R.id.answerContentTextView);
		holder.upvoteState = (TextView) convertView
				.findViewById(R.id.upvoteStateTextView);
		holder.upvote_Rb = (RadioButton) convertView
				.findViewById(R.id.upvote_button);
		holder.timestamp = (TextView) convertView
				.findViewById(R.id.answer_time_textView);
		holder.replyList = (ExpandableListView) convertView
				.findViewById(R.id.answer_reply_expandableListView);
		holder.image.setVisibility(View.GONE);
		holder.reply_Rb = (RadioButton) convertView
				.findViewById(R.id.answer_reply_button);
		convertView.setTag(holder);
		Answer answer = this.getItem(position);

		if (answer != null) {
			holder.answerContent.setText(answer.getContent());
			holder.authorName.setText(answer.getAuthor());
			holder.timestamp.setText(answer.getTimestamp().toString());
			holder.upvoteState.setText("Upvote: "
					+ answer.getAnswerUpvoteCount());
			if (answer.hasImage()) {
				holder.image.setVisibility(View.VISIBLE);
				holder.image.setImageBitmap(answer.getImage());
			}
		}
		holder.upvote_Rb
				.setOnClickListener(new upvoteOnClickListener(position));
		holder.reply_Rb
				.setOnClickListener(new AddReplyOnClickListener(position));
		return convertView;
	}

	private class upvoteOnClickListener implements OnClickListener {

		int position;

		public upvoteOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			Answer answer = answerList.get(position);
			answer.upvoteAnswer();

			Thread thread = new UpdateAnswerThread(question, answer);
			thread.start();

			notifyDataSetChanged();
		}
	}

	private class AddReplyOnClickListener implements OnClickListener {

		int position;

		public AddReplyOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getContext(),CreateAnswerReplyActivity.class);
			intent.putExtra(CreateAnswerReplyActivity.QUESTION_ID, question.getID());
			intent.putExtra(CreateAnswerReplyActivity.ANSWER_POS, position);
			context.startActivity(intent);
		}
	}

	class ViewHolder {
		ImageView authorPic;
		TextView authorName;
		TextView answerContent;
		RadioButton upvote_Rb;
		TextView timestamp;
		TextView upvoteState;
		ImageView image;
		ExpandableListView replyList;
		RadioButton reply_Rb;
	}

}
