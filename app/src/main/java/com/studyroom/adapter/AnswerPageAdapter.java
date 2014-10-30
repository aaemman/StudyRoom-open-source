package com.studyroom.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.studyroom.R;
import com.studyroom.model.Answer;
import com.studyroom.model.Question;

import java.util.ArrayList;

public class AnswerPageAdapter extends ArrayAdapter<Object> {
	private final Context           context;
	private       ArrayList<Object> mValues;
	private final int QUESTION_ELEMENT = 0;
	private final int ANSWER_ELEMENT   = 1;
	private final int ANSWER_HEADER    = 2;

	SharedPreferences prefs;
	String            user_id;

	public AnswerPageAdapter(Context context, ArrayList<Object> values) {
		super(context, R.layout.question_feed_row, values);
		this.context = context;
		this.mValues = values;

		getPreferences();
	}

	private void getPreferences() {
		prefs = context.getSharedPreferences(
				"com.canal5.studyroom", Context.MODE_PRIVATE);

		user_id = prefs.getString("user_id", "");
	}

	@Override
	public int getCount() {
		return mValues.size();
	}

	@Override
	public int getViewTypeCount() {
		return 3;

	}

	@Override
	public int getItemViewType(int position) {

		Object obj = mValues.get(position);
		if (obj.getClass() == Question.class && position == 0) {
			return QUESTION_ELEMENT;
		} else if (obj.getClass() == Answer.class && position != 0) {
			return ANSWER_ELEMENT;
		} else if (obj.getClass() == String.class && position != 0) {
			return ANSWER_HEADER;
		}
		return -1;
	}

	static class QuestionViewHolder {
		TextView content;
		TextView username;
		TextView tvDate;
		TextView tvPoints;
		Button   bVoteUp;
		Button   bVoteDown;
		Button   bShowComments;
		Button   bDetails;
		int      position;
	}

	static class AnswerViewHolder {
		TextView    content;
		TextView    username;
		TextView    tvDate;
		TextView    tvPoints;
		Button      bVoteUp;
		Button      bVoteDown;
		Button      bShowComments;
		Button      bDetails;
		ImageButton bMarkCorrect;
		int         position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = convertView;
		QuestionViewHolder questionHolder = new QuestionViewHolder();
		AnswerViewHolder answerHolder = new AnswerViewHolder();

		if (row == null) {

			if (getItemViewType(position) == QUESTION_ELEMENT) {

				row = inflater.inflate(R.layout.answer_page_question_row,
				                       parent, false);

				questionHolder.content = (TextView) row
						.findViewById(R.id.tvAnswerContent);

				questionHolder.username = (TextView) row
						.findViewById(R.id.user);

				questionHolder.tvDate = (TextView) row
						.findViewById(R.id.tvQuestionFeedRowDate);
				questionHolder.tvPoints = (TextView) row
						.findViewById(R.id.tvQuestionFeedRowPointsValue);

				ViewGroup ml = (ViewGroup) row.findViewById(R.id.layout);

				questionHolder.bVoteUp = (Button) row
						.findViewById(R.id.bAnswerPageQuestionRowVoteUpButton);

				questionHolder.bVoteDown = (Button) row
						.findViewById(R.id.bAnswerPageQuestionRowVoteDownButton);

				questionHolder.bShowComments = (Button) row
						.findViewById(R.id.bAnswerPageQuestionRowComments);

				questionHolder.bDetails = (Button) row
						.findViewById(R.id.bAnswerPageQuestionRowDetails);

				if (((Question) mValues.get(position)).getComments().size() > 0) {
					questionHolder.bShowComments.setTextColor(this.getContext()
							                                          .getResources().getColor(R.color.studyroom_purple));

					if ((((Question) mValues.get(position)).getComments()
							.size() == 1)) {
						questionHolder.bShowComments
								.setText(((Question) mValues.get(position))
										         .getComments().size() + " Comment");
					} else {
						questionHolder.bShowComments
								.setText(((Question) mValues.get(position))
										         .getComments().size() + " Comments");
					}
				}

				row.setTag(questionHolder);

			} else if (getItemViewType(position) == ANSWER_HEADER) {
				row = inflater.inflate(
						R.layout.answer_page_answer_section_header, parent,
						false);

			} else {

				row = inflater.inflate(R.layout.answer_page_answer_row, parent,
				                       false);

				answerHolder.content = (TextView) row
						.findViewById(R.id.tvAnswerContent);

				answerHolder.username = (TextView) row.findViewById(R.id.user);

				answerHolder.tvDate = (TextView) row
						.findViewById(R.id.tvQuestionFeedRowDate);
				answerHolder.tvPoints = (TextView) row
						.findViewById(R.id.tvQuestionFeedRowPointsValue);

				ViewGroup ml = (ViewGroup) row.findViewById(R.id.layout);

				answerHolder.bVoteUp = (Button) row
						.findViewById(R.id.bAnswerPageAnswerRowVoteUpButton);

				answerHolder.bVoteDown = (Button) row
						.findViewById(R.id.bAnswerPageAnswerRowVoteDownButton);

				answerHolder.bShowComments = (Button) row
						.findViewById(R.id.bAnswerPageAnswerRowComments);

				answerHolder.bDetails = (Button) row
						.findViewById(R.id.bAnswerPageAnswerRowDetails);
				answerHolder.bMarkCorrect = (ImageButton) row
						.findViewById(R.id.bMarkAnswerCorrect);

				if (((Question) mValues.get(0)).getAuthorID() == Integer.parseInt(user_id)) {

					if (((Answer) mValues.get(position)).getIsCorrect()) {
						answerHolder.bMarkCorrect.setEnabled(false);
						answerHolder.bMarkCorrect
								.setImageResource(R.drawable.marked_correct_up_background);
					} else if (((Question) mValues.get(0)).hasCorrectAnswer == true) {
						answerHolder.bMarkCorrect.setVisibility(View.GONE);
					}
				} else {
					answerHolder.bMarkCorrect.setVisibility(View.GONE);
				}

				if (((Answer) mValues.get(position)).getComments().size() > 0) {
					answerHolder.bShowComments.setTextColor(this.getContext()
							                                        .getResources().getColor(R.color.studyroom_purple));

					if ((((Answer) mValues.get(position)).getComments().size() == 1)) {
						answerHolder.bShowComments.setText(((Answer) mValues
								.get(position)).getComments().size()
								                                   + " Comment");
					} else {
						answerHolder.bShowComments.setText(((Answer) mValues
								.get(position)).getComments().size()
								                                   + " Comments");
					}
				}

				row.setTag(answerHolder);
			}

		}

		if (getItemViewType(position) == QUESTION_ELEMENT) {

			questionHolder = (QuestionViewHolder) row.getTag();

			questionHolder.content.setText(((Question) mValues.get(position))
					                               .getDescription());
			questionHolder.username.setText(((Question) mValues.get(position))
					                                .getAuthor());

			questionHolder.tvDate.setText(((Question) mValues.get(position))
					                              .getDateTime());
			questionHolder.tvPoints.setText(((Question) mValues.get(position))
					                                .getTotalPoints());
			questionHolder.bVoteUp.setTag(mValues.get(position));
			questionHolder.bVoteDown.setTag(mValues.get(position));
			questionHolder.bShowComments.setTag(mValues.get(position));
			questionHolder.bDetails.setTag(mValues.get(position));

		} else if (getItemViewType(position) == ANSWER_ELEMENT) {

			answerHolder = (AnswerViewHolder) row.getTag();

			answerHolder.content.setText(((Answer) mValues.get(position))
					                             .getDescription());
			answerHolder.username.setText(((Answer) mValues.get(position))
					                              .getAuthor());

			answerHolder.tvDate.setText(((Answer) mValues.get(position))
					                            .getDateTime());
			answerHolder.tvPoints.setText(Integer.toString(((Answer) mValues
					.get(position)).getTotalPoints()));

			answerHolder.bVoteUp.setTag(mValues.get(position));
			answerHolder.bVoteDown.setTag(mValues.get(position));
			answerHolder.bShowComments.setTag(mValues.get(position));
			answerHolder.bDetails.setTag(mValues.get(position));
			answerHolder.bMarkCorrect.setTag(mValues.get(position));
		}
		return row;
	}
}
