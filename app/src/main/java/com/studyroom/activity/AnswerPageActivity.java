package com.studyroom.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.studyroom.R;
import com.studyroom.adapter.AnswerPageAdapter;
import com.studyroom.adapter.CommentsDialogAdapter;
import com.studyroom.connectivity.PostDispatcher;
import com.studyroom.connectivity.UpdateAdapters;
import com.studyroom.model.Answer;
import com.studyroom.model.Comment;
import com.studyroom.model.Question;

import java.util.ArrayList;
import java.util.HashMap;

public class AnswerPageActivity extends Activity implements UpdateAdapters {

	ListView              listView;
	AnswerPageAdapter     AnswerPageAdapter;
	CommentsDialogAdapter commentsDialogAdapter;
	public static ArrayList<Object> values = new ArrayList<Object>();
	int question_id;
	public        TextView tv_QuestionTitle;
	public        TextView tv_Class;
	public static Question question;
	private       ListView commentsList;
	private       TextView tvNoComments;
	private       EditText etCommentModalPost;
	private String[] detailsListItems = new String[]{"Report as Inappropriate"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.answer_page);
		super.onCreate(savedInstanceState);

		Intent i = getIntent();
		question_id = i.getIntExtra("question_id", 0);

		listView = (ListView) findViewById(R.id.list);
		View header = getLayoutInflater().inflate(R.layout.answer_page_header,
		                                          null);
		listView.addHeaderView(header);

		tv_QuestionTitle = (TextView) findViewById(R.id.Title);
		tv_Class = (TextView) findViewById(R.id.Class);

		new AddToAdapter(this, this).execute("answerPage");

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

	}

	public void markCorrectOnClick(final View view) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Mark Answer Correct");

		// set dialog message
		alertDialogBuilder
				.setMessage(
						"Clicking the Mark Correct button will set this answer as the accepted answer for this question. This cannot be undone")
				.setCancelable(false)
				.setPositiveButton("Mark Correct",
				                   new DialogInterface.OnClickListener() {
					                   public void onClick(DialogInterface dialog, int id) {
						                   // if this button is clicked, close
						                   // current activity

						                   new AddToAdapter(AnswerPageActivity.this,
						                                    AnswerPageActivity.this).execute(
								                   "markCorrect", Integer.toString(((Answer) view.getTag()).id));

					                   }
				                   })
				.setNegativeButton("Cancel",
				                   new DialogInterface.OnClickListener() {
					                   public void onClick(DialogInterface dialog, int id) {
						                   // if this button is clicked, just close
						                   // the dialog box and do nothing
						                   dialog.cancel();
					                   }
				                   });

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	public void detailsOnClick(View view) {

		final Dialog detailsDialog = new Dialog(this);
		final Dialog reportDialog = new Dialog(this);
		reportDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		reportDialog.setContentView(R.layout.post_report_dialog);
		detailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		detailsDialog.setContentView(R.layout.details_modal);
		final String reportable_type;
		final String reportable_id;
		ListView detailsList = (ListView) detailsDialog
				.findViewById(R.id.lvDetailsList);
		detailsList.setAdapter(new ArrayAdapter<String>(this,
		                                                R.layout.details_row, detailsListItems));

		if (view.getTag().getClass() == Question.class) {
			reportable_type = "Question";
			reportable_id = Integer
					.toString(((Question) view.getTag()).getID());
		} else if (view.getTag().getClass() == Answer.class) {
			reportable_type = "Answer";
			reportable_id = Integer.toString(((Answer) view.getTag()).getID());
		} else {
			reportable_type = null;
			reportable_id = null;
		}

		detailsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
			                        int position, long id) {
				// TODO Auto-generated method stub
				detailsDialog.dismiss();
				reportDialog.show();

				Button bCancel = (Button) reportDialog
						.findViewById(R.id.bPostReportCancel);
				Button bPost = (Button) reportDialog
						.findViewById(R.id.bPostReportPost);

				bCancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						reportDialog.dismiss();
					}
				});

				bPost.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						EditText etRepostDescription = (EditText) reportDialog
								.findViewById(R.id.etReportDescription);

						reportDialog.dismiss();

						Toast.makeText(AnswerPageActivity.this,
						               "Thank you, we will look into this",
						               Toast.LENGTH_LONG).show();

						new AddToAdapter(AnswerPageActivity.this,
						                 AnswerPageActivity.this).execute("report",
						                                                  etRepostDescription.getText().toString(),
						                                                  reportable_type, reportable_id);
					}
				});

			}

		});

		detailsDialog.show();

	}

	public void showCommentsOnClick(View view) {
		// ********** THIS WHOLE METHOD NEEDS TO BE CLEANED UP THE CODE REALLY
		// SMELLS *************
		if (view.getTag().getClass() == Question.class) {
			final Dialog commentsDialog = new Dialog(this);
			commentsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			commentsDialog.setContentView(R.layout.comments_dialog);

			final Question question = (Question) view.getTag();
			tvNoComments = (TextView) commentsDialog
					.findViewById(R.id.tvCommentsModalNoComments);
			tvNoComments.setText(randomMessageGenerator());

			if (((Question) view.getTag()).getComments().size() > 0) {
				commentsList = (ListView) commentsDialog
						.findViewById(R.id.lvCommentsList);
				commentsDialogAdapter = new CommentsDialogAdapter(
						AnswerPageActivity.this,
						((Question) view.getTag()).getComments());
				commentsList.setEmptyView(commentsDialog
						                          .findViewById(R.id.tvCommentsModalNoComments));
				commentsList.setAdapter(commentsDialogAdapter);

			}

			Button dialogButton = (Button) commentsDialog
					.findViewById(R.id.bCommentsModalPost);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					// commentsDialog.dismiss();
					etCommentModalPost = (EditText) commentsDialog
							.findViewById(R.id.etCommentsModalPost);
					Comment comment = new Comment(etCommentModalPost.getText()
							                              .toString().trim(), "", "posting...");
					etCommentModalPost.clearFocus();
					etCommentModalPost.setText("");
					ArrayList<Object> newComments = question.getComments();
					newComments.add(comment);
					question.setComments(newComments);

					commentsDialogAdapter = new CommentsDialogAdapter(
							AnswerPageActivity.this, question.getComments());
					commentsList.setAdapter(commentsDialogAdapter);

					new AddToAdapter(
							AnswerPageActivity.this, AnswerPageActivity.this)
							.execute("post_comments",
							         Integer.toString(question.getID()),
							         "Question", comment.getDescription());

				}
			});

			commentsDialog.show();

		} else if (view.getTag().getClass() == Answer.class) {
			final Dialog commentsDialog = new Dialog(this);
			commentsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			commentsDialog.setContentView(R.layout.comments_dialog);

			final Answer answer = (Answer) view.getTag();
			tvNoComments = (TextView) commentsDialog
					.findViewById(R.id.tvCommentsModalNoComments);
			tvNoComments.setText(randomMessageGenerator());

			if (((Answer) view.getTag()).getComments().size() > 0) {
				commentsList = (ListView) commentsDialog
						.findViewById(R.id.lvCommentsList);
				commentsDialogAdapter = new CommentsDialogAdapter(
						AnswerPageActivity.this,
						((Answer) view.getTag()).getComments());
				commentsList.setEmptyView(commentsDialog
						                          .findViewById(R.id.tvCommentsModalNoComments));
				commentsList.setAdapter(commentsDialogAdapter);

			}

			Button dialogButton = (Button) commentsDialog
					.findViewById(R.id.bCommentsModalPost);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					// commentsDialog.dismiss();
					etCommentModalPost = (EditText) commentsDialog
							.findViewById(R.id.etCommentsModalPost);
					Comment comment = new Comment(etCommentModalPost.getText()
							                              .toString().trim(), "", "posting...");
					etCommentModalPost.clearFocus();
					etCommentModalPost.setText("");
					ArrayList<Object> newComments = answer.getComments();
					newComments.add(comment);
					answer.setComments(newComments);

					commentsDialogAdapter = new CommentsDialogAdapter(
							AnswerPageActivity.this, answer.getComments());
					commentsList.setAdapter(commentsDialogAdapter);

					new AddToAdapter(
							AnswerPageActivity.this, AnswerPageActivity.this)
							.execute("post_comments",
							         Integer.toString(answer.getID()), "Answer",
							         comment.getDescription());

				}
			});

			commentsDialog.show();
		}
	}

	private String randomMessageGenerator() {
		// TODO Auto-generated method stub
		String[] messages = {"First!", "No Comments",
				"Claim your rightful place at the top of the commenters list!",
				"Quick! before someone else comments first!"};

		return messages[0 + (int) (Math.random() * ((messages.length - 1 - 0) + 1))];
	}

	public void postAnswer(View view) {
		Button postAnswerButton = (Button) findViewById(R.id.new_answer_answer_button);
		EditText et = (EditText) findViewById(R.id.new_answer_edit_text);

		postAnswerButton.setEnabled(false);
		et.setEnabled(false);

		HashMap<String, String> postMap = new HashMap<String, String>();
		postMap.put("description", et.getText().toString());
		postMap.put("question_id", Integer.toString(question_id));
		postMap.put("correct", "false");

		PostDispatcher postQuestionDispatcher = new PostDispatcher(
				"answer", "answer", postMap,
				getString(R.string.answersURL),
				AnswerPageActivity.this, AnswerPageActivity.this);
		postQuestionDispatcher.execute();

		new AddToAdapter(AnswerPageActivity.this,
		                 AnswerPageActivity.this).execute("answerPage");


		postAnswerButton.setEnabled(true);
		et.setText("");
		et.setEnabled(true);

	}

	public void VoteUpOnClick(View view) {
		if (view.getTag().getClass() == Question.class) {
			Toast.makeText(this, ((Question) view.getTag()).getTitle(),
			               Toast.LENGTH_LONG).show();
			Log.i(this.getClass().getSimpleName(),
			      "--------------------vote up question----------------------");

			Question question = ((Question) view.getTag());
			new AddToAdapter(this, this).execute("voteUp",
			                                     Integer.toString(question.getID()), "Question",
			                                     ((Question) values.get(0)).getClassName().toString());

		} else if (view.getTag().getClass() == Answer.class) {
			Log.i(this.getClass().getSimpleName(),
			      "--------------------vote up answer----------------------");

			Answer answer = ((Answer) view.getTag());
			new AddToAdapter(this, this).execute("voteUp",
			                                     Integer.toString(answer.getID()), "Answer",
			                                     ((Question) values.get(0)).getClassName().toString());
		}
	}

	public void VoteDownOnClick(View view) {
		if (view.getTag().getClass() == Question.class) {
			Log.i(this.getClass().getSimpleName(),
			      "--------------------vote down question ----------------------");

			Question question = ((Question) view.getTag());
			new AddToAdapter(this, this).execute("voteDown",
			                                     Integer.toString(question.getID()), "Question",
			                                     ((Question) values.get(0)).getClassName().toString());

		} else if (view.getTag().getClass() == Answer.class) {
			Toast.makeText(this, ((Answer) view.getTag()).getDescription(),
			               Toast.LENGTH_LONG).show();
			Log.i(this.getClass().getSimpleName(),
			      "--------------------vote down answer----------------------");

			Answer answer = ((Answer) view.getTag());
			new AddToAdapter(this, this).execute("voteDown",
			                                     Integer.toString(answer.getID()), "Answer",
			                                     ((Question) values.get(0)).getClassName().toString());
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				break;
			default:
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	private class AddToAdapter extends
			AsyncTask<String, String, ArrayList<String>> {
		private ProgressDialog pDialog;
		Context        mContext;
		UpdateAdapters ua;

		public AddToAdapter(Context c, UpdateAdapters u) {
			this.mContext = c;
			ua = u;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(AnswerPageActivity.this);
			pDialog.setMessage("Getting Data ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		@Override
		protected ArrayList<String> doInBackground(String... params) {

			System.out.println(params.length + "---------------------------");
			if (params[0] == "answerPage") {
				HashMap<String, String> postMap = null;// = new HashMap<String,
				// String>();
				PostDispatcher postQuestionDispatcher = new PostDispatcher(
						"Question", "answerPage", postMap,
						getString(R.string.questionsURL) + "/"
								+ Integer.toString(question_id), mContext, ua);
				// System.out.println(postMap.toString());
				postQuestionDispatcher.execute();

			} else if (params[0] == "voteUp") {
				HashMap<String, String> postMap = new HashMap<String, String>();

				postMap.put("pointable_id", params[1]);
				postMap.put("pointable_type", params[2]);
				postMap.put("value", "1");
				postMap.put("class_name", params[3]);

				PostDispatcher postQuestionDispatcher = new PostDispatcher(
						"point_transaction", "point_transaction", postMap,
						getString(R.string.voteURL), mContext, ua);
				// System.out.println(postMap.toString());
				postQuestionDispatcher.execute();

			} else if (params[0] == "voteDown") {

				HashMap<String, String> postMap = new HashMap<String, String>();

				postMap.put("pointable_id", params[1]);
				postMap.put("pointable_type", params[2]);
				postMap.put("value", "-1");
				postMap.put("class_name", params[3]);

				PostDispatcher postQuestionDispatcher = new PostDispatcher(
						"point_transaction", "point_transaction", postMap,
						getString(R.string.voteURL), mContext, ua);
				// System.out.println(postMap.toString());
				postQuestionDispatcher.execute();
			} else if (params[0] == "post_comments") {
				HashMap<String, String> postMap = new HashMap<String, String>();

				postMap.put("commentable_id", params[1]);
				postMap.put("commentable_type", params[2]);
				postMap.put("description", params[3]);

				PostDispatcher postQuestionDispatcher = new PostDispatcher(
						"post_comment", "post_comment", postMap,
						getString(R.string.commentURL), mContext, ua);
				// System.out.println(postMap.toString());
				postQuestionDispatcher.execute();
			} else if (params[0] == "report") {
				HashMap<String, String> postMap = new HashMap<String, String>();

				postMap.put("description", params[1]);
				postMap.put("reportable_type", params[2]);
				postMap.put("reportable_id", params[3]);

				PostDispatcher reportDispatcher = new PostDispatcher(
						"report", "report", postMap,
						getString(R.string.reportURL), AnswerPageActivity.this,
						AnswerPageActivity.this);

				reportDispatcher.execute();

			} else if (params[0] == "markCorrect") {
				HashMap<String, String> postMap = new HashMap<String, String>();

				postMap.put("id", params[1]);

				PostDispatcher markCorrectDispatcher = new PostDispatcher(
						"report", "report", postMap,
						getString(R.string.answersURL) + "/" + params[1], AnswerPageActivity.this,
						AnswerPageActivity.this);

				markCorrectDispatcher.execute();

			}
			if (params[0] != "comment" && params[0] != "report") {
				HashMap<String, String> postMap = null;// = new HashMap<String,
				// String>();
				PostDispatcher postQuestionDispatcher = new PostDispatcher(
						"Question", "answerPage", postMap,
						getString(R.string.questionsURL) + "/"
								+ Integer.toString(question_id), mContext, ua);
				// System.out.println(postMap.toString());
				postQuestionDispatcher.execute();
			}
			ArrayList<String> s = new ArrayList<String>();

			return s;
		}

		@Override
		protected void onPostExecute(ArrayList<String> v) {
			pDialog.dismiss();

		}

	}

	@Override
	public void update(ArrayList<Object> s) {
		if (s.size() > 0) {
			AnswerPageActivity.values = s;

			Log.i(this.getClass().getSimpleName(),
			      "---------------- checking AnswerPage update "
					      + s.get(0).getClass().getSimpleName()
					      + " -----------------");

			if (s.get(0).getClass() == Comment.class) {
				commentsDialogAdapter = new CommentsDialogAdapter(
						AnswerPageActivity.this, s);
				commentsList.setAdapter(commentsDialogAdapter);

				Log.i(this.getClass().getSimpleName(), "----------------"
						+ ((Comment) s.get(s.size() - 1)).getDescription()
						+ " -----------------");

				commentsDialogAdapter.notifyDataSetChanged();
			} else {

				AnswerPageAdapter = new AnswerPageAdapter(
						AnswerPageActivity.this, s);
				listView.setAdapter(AnswerPageAdapter);

				Log.i(this.getClass().getSimpleName(),
				      "---------------- ANSWER-PAGE UPDATE -----------------");

				AnswerPageAdapter.notifyDataSetChanged();
				tv_QuestionTitle.setText(((Question) s.get(0)).getTitle());
				tv_Class.setText(((Question) s.get(0)).getClassName());
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		// adapter.notifyDataSetChanged();
		// we don't like this because it creates a new feedadapter!
		// new AddToAdapter(this,this).execute();
	}

}
