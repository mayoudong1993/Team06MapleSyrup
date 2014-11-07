package ca.ualberta.app.adapter;

import java.util.ArrayList;

import ca.ualberta.app.activity.R;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.Reply;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ReplyListAdapter extends BaseExpandableListAdapter {
	private ArrayList<Reply> replyList = null;
	private Context context;
	public ReplyListAdapter(Context context, int singleReply,
			ArrayList<Reply> objects, Question question) {
		this.context = context;
		this.replyList = objects;
	}

	@Override
	public int getGroupCount() {
		return 1;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return replyList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return replyList.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.single_reply_group, null);

		return convertView;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.single_reply, null);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.authorName = (TextView) convertView
				.findViewById(R.id.replyAuthor_textView);
		holder.replyContent = (TextView) convertView
				.findViewById(R.id.reply_textView);
		holder.timestamp = (TextView) convertView
				.findViewById(R.id.reply_time_textView);
		convertView.setTag(holder);
		Reply reply = replyList.get(childPosition);
		if (reply != null) {
			holder.replyContent.setText(reply.getContent());
			holder.authorName.setText(reply.getAuthor());
			holder.timestamp.setText(reply.getTimestamp().toString());
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	class ViewHolder {
		TextView authorName;
		TextView replyContent;
		TextView timestamp;
	}

}