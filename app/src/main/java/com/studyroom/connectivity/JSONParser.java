package com.studyroom.connectivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.studyroom.activity.EnrollClassActivity;
import com.studyroom.activity.LeaderboardsActivity;
import com.studyroom.activity.MyQuestionsActivity;
import com.studyroom.activity.QuestionFeedActivity;
import com.studyroom.activity.SignUpActivity;
import com.studyroom.model.*;
import com.studyroom.model.Error;

public class JSONParser {

	public static ArrayList<Object> setClassAdapterValues(String s,
			String responseType) {
		ArrayList<Object> returndata = new ArrayList<Object>();

		if (responseType == "profile") {

			JSONObject data;
			try {
				data = ((JSONObject) new JSONObject(s.toString()))
						.getJSONObject("data");
				Log.i("JSONParser", "data => " + data);

				JSONObject u = (JSONObject) new JSONObject(
						data.getString("User"));

				Log.i("JSONParser", "User => " + u);

				String name;
				String createdAt;
				String totalPoints;
				String questionCount;
				String answerCount;
				String correctAnswerCount;
				String percentCorrectAnswerCount;
				String commentCount;

				name = u.get("name").toString();
				createdAt = u.get("created_at").toString();
				totalPoints = u.get("total_points").toString();
				questionCount = u.get("question_count").toString();
				answerCount = u.get("answer_count").toString();
				correctAnswerCount = u.get("correct_answer_count").toString();

				if ((!(correctAnswerCount.equals("null")))
						&& (!(answerCount.equals("null")))
						&& (!(answerCount.equals("0")))) {
					percentCorrectAnswerCount = Integer.toString((Integer
							.parseInt(correctAnswerCount) / Integer
							.parseInt(answerCount)) * 100);
				} else {
					percentCorrectAnswerCount = "0";
				}

				commentCount = u.get("comment_count").toString();

				System.out.println(name);
				System.out.println(createdAt);
				System.out.println(totalPoints);
				System.out.println(questionCount);
				System.out.println(answerCount);
				System.out.println(correctAnswerCount);
				System.out.println(percentCorrectAnswerCount);
				System.out.println(commentCount);

				User user = new User(name, createdAt, totalPoints,
						questionCount, answerCount, correctAnswerCount,
						percentCorrectAnswerCount, commentCount);

				returndata.add(user);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if (responseType == "class_relationships") {

			JSONObject data;
			try {
				data = ((JSONObject) new JSONObject(s.toString()))
						.getJSONObject("data");

				JSONArray users = (JSONArray) data.get("Classes");

				QuestionFeedActivity.classes.clear();
				QuestionFeedActivity.classes.add(0, "");
				for (int i = 0; i < users.length(); i++) {
					returndata.add(users.get(i).toString());

					QuestionFeedActivity.classes
							.add((String) returndata.get(i));

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (responseType == "answerPage") {

			JSONObject data;
			Question q = null;
			Answer a;
			String title;
			String description;
			int question_id;
			String class_name;
			String dateTime;
			String totalPoints;
			int answer_id;
			String correct;
			String author;
			ArrayList<Object> comments = new ArrayList<Object>();
			ArrayList<Object> question_comments = new ArrayList<Object>();
			String comment_description;
			String comment_author;
			String datetime;
			int authorId;
			JSONObject user;
			JSONObject comment;

			try {

				data = ((JSONObject) new JSONObject(s.toString()))
						.getJSONObject("data");
				// Log.i("JSONParser", "data => " + data);

				JSONObject question = new JSONObject(data.getString("Question"));

				title = question.get("title").toString();
				description = question.get("description").toString();
				class_name = question.get("class_name").toString();
				dateTime = question.get("formatted_created_at").toString();
				totalPoints = question.get("total_points").toString();
				question_id = Integer.parseInt(question.get("id").toString());

				JSONObject u = new JSONObject(question.getJSONObject("user")
						.toString());

				author = u.get("name").toString();
				authorId = Integer.parseInt(u.getString("id"));
				// author = "hardcoded author";

				JSONArray question_post_comments = (JSONArray) new JSONArray(
						question.getString("post_comments"));

				Log.i("JSONParser", "Question Comments => "
						+ question_post_comments);

				for (int j = 0; j < question_post_comments.length(); j++) {
					comment = question_post_comments.getJSONObject(j);
					user = new JSONObject(comment.getJSONObject("user")
							.toString());
					comment_description = comment.getString("description");
					comment_author = user.getString("name");

					datetime = comment.getString("created_at");
					question_comments.add(new Comment(comment_description,
							comment_author, datetime));
				}

				// the false in the end is for hasCorrect answer, this should be
				// changed once this variable has been added to the server DB
				q = new Question(title, dateTime, description, author,
						class_name, totalPoints, question_id,
						question_comments, authorId, false);
				returndata.add(q);
				Log.i("JSONParser", "Question => " + question);

				JSONArray answers = (JSONArray) new JSONArray(
						question.getString("answers"));

				Log.i("JSONParser", "Answer => " + answers);

				//
				// AnswerPage.question = null;
				// AnswerPage.question = q;

				// comments.clear();

				returndata.clear();
				returndata.add("Answer Title");

				for (int i = 0; i < answers.length(); i++) {
					// returndata.add(users.get(i).toString());
					JSONObject answer = answers.getJSONObject(i);
					comments = new ArrayList<Object>();
					JSONArray comments_a = (JSONArray) new JSONArray(
							answer.getString("post_comments"));

					for (int j = 0; j < comments_a.length(); j++) {
						comment = comments_a.getJSONObject(j);
						user = new JSONObject(comment.getJSONObject("user")
								.toString());
						comment_description = comment.getString("description");
						comment_author = user.getString("name");

						datetime = comment.getString("formatted_created_at");
						comments.add(new Comment(comment_description,
								comment_author, datetime));
					}

					description = answer.get("description").toString();
					dateTime = answer.get("formatted_created_at").toString();
					totalPoints = answer.get("total_points").toString();
					question_id = Integer.parseInt(answer.get("question_id")
							.toString());
					answer_id = Integer.parseInt(answer.get("id").toString());
					correct = answer.get("correct").toString();

					JSONObject u1 = new JSONObject(answer.getJSONObject("user")
							.toString());
					author = u1.get("name").toString();

					a = new Answer(dateTime, description, author, totalPoints,
							answer_id, question_id, correct, comments);

					if (a.getIsCorrect()) {
						q.hasCorrectAnswer = true;

					}

					// AnswerPageActivity.values.add(a);

					returndata.add(a);
					Log.i("JSONParser", "Answer => " + a);

					Log.i("JSONParser",
							"returndata => " + returndata.toString());

				}
				returndata.add(0, q);

			}

			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if (responseType == "post_comment") {
			Log.i(JSONParser.class.getSimpleName(),
					"---------------- entering post_comments if ----------------");
			JSONArray commentsArray;
			JSONObject comment;
			JSONObject user;
			String comment_description;
			String comment_author;
			String datetime;

			JSONObject data;
			try {
				data = ((JSONObject) new JSONObject(s.toString()))
						.getJSONObject("data");

				commentsArray = (JSONArray) new JSONArray(
						data.getString("PostComments"));

				for (int i = 0; i < commentsArray.length(); i++) {
					comment = commentsArray.getJSONObject(i);
					user = new JSONObject(comment.getJSONObject("user")
							.toString());
					comment_description = comment.getString("description");
					comment_author = user.getString("name");

					datetime = comment.getString("formatted_created_at");
					Comment tempComment = new Comment(comment_description,
							comment_author, datetime);
					returndata.add((Object) tempComment);
					Log.i(JSONParser.class.getSimpleName(),
							"---------------- tempComment => "
									+ tempComment.toString()
									+ " -----------------");
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (responseType == "classes") {

			JSONObject data;
			try {
				data = ((JSONObject) new JSONObject(s.toString()))
						.getJSONObject("data");

				JSONArray users = (JSONArray) data.get("Classes");

				EnrollClassActivity.values2.clear();
				for (int i = 0; i < users.length(); i++) {
					returndata.add(users.get(i).toString());

					EnrollClassActivity.values2.add((String) returndata.get(i));

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if (responseType == "questions") {

			JSONObject data;
			try {
				data = ((JSONObject) new JSONObject(s.toString()))
						.getJSONObject("data");
				Log.i("JSONParser", "data => " + data);
				Question quest;

				JSONArray questions = (JSONArray) new JSONArray(
						data.getString("Questions"));

				Log.i("JSONParser", "Questions => " + questions);

				String title;
				String description;
				String author;
				int question_id;
				String class_name;
				String dateTime;
				String totalPoints;
				int id;

				QuestionFeedActivity.questions.clear();

				for (int i = 0; i < questions.length(); i++) {
					// returndata.add(users.get(i).toString());
					JSONObject q = questions.getJSONObject(i);

					title = q.get("title").toString();
					description = q.get("description").toString();
					class_name = q.get("class_name").toString();
					dateTime = q.get("formatted_created_at").toString();
					totalPoints = q.get("total_points").toString();
					question_id = Integer.parseInt(q.get("id").toString());
					id = q.getInt("id");

					JSONObject u = new JSONObject(q.getJSONObject("user")
							.toString());

					author = u.get("name").toString();

					System.out.println(title);
					System.out.println(description);
					System.out.println(class_name);
					System.out.println(author);
					System.out.println(dateTime);
					System.out.println(totalPoints);
					System.out.println(question_id);

					quest = new Question(title, dateTime, description, author,
							class_name, totalPoints, id);

					QuestionFeedActivity.questions.add(quest);

					/*
					 * JSONObject data = ((JSONObject)new
					 * JSONObject(returnString)).getJSONObject("data");
					 * 
					 * 
					 * JSONArray notes = (JSONArray) data.get("notes");
					 * ArrayList<String> stringNotes = new ArrayList<String>();
					 * ArrayList<String> stringNotesDates = new
					 * ArrayList<String>();
					 * 
					 * for (int i =0; i < notes.length(); i++){ JSONObject note
					 * = new JSONObject(notes.get(i).toString());
					 * stringNotes.add(note.get("text").toString());
					 * stringNotesDates.add(note.get("created_at").toString());
					 * 
					 * } System.out.println("*****************************");
					 * MainActivity.Notes =
					 * stringNotes.toArray(MainActivity.Notes);
					 * MainActivity.NoteDates =
					 * stringNotesDates.toArray(MainActivity.NoteDates);
					 */

					// MainFeed.values2.add(returndata.get(i));

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		/*
		 * JSONObject data = ((JSONObject)new
		 * JSONObject(returnString)).getJSONObject("data");
		 * 
		 * 
		 * JSONArray notes = (JSONArray) data.get("notes"); ArrayList<String>
		 * stringNotes = new ArrayList<String>(); ArrayList<String>
		 * stringNotesDates = new ArrayList<String>();
		 * 
		 * for (int i =0; i < notes.length(); i++){ JSONObject note = new
		 * JSONObject(notes.get(i).toString());
		 * stringNotes.add(note.get("text").toString());
		 * stringNotesDates.add(note.get("created_at").toString());
		 * 
		 * } System.out.println("*****************************");
		 * MainActivity.Notes = stringNotes.toArray(MainActivity.Notes);
		 * MainActivity.NoteDates =
		 * stringNotesDates.toArray(MainActivity.NoteDates);
		 */

		if (responseType == "leaderboards") {
			JSONObject data;

			List<String> listDataHeader = new ArrayList<String>();
			HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();

			try {
				data = ((JSONObject) new JSONObject(s.toString()))
						.getJSONObject("data");
				Log.i(JSONParser.class.getSimpleName(), "data => " + data);
				JSONArray leaderboards = (JSONArray) new JSONArray(
						data.getString("Leaderboards"));
				String leaderboard_name;
				String user_name;
				String user_points;
				JSONObject entry;

				for (int i = 0; i < leaderboards.length(); i++) {
					// returndata.add(users.get(i).toString());
					entry = leaderboards.getJSONObject(i);
					leaderboard_name = entry.getString("leaderboard_name");
					user_name = entry.getString("user_name");
					user_points = entry.getString("points");

					if (!listDataChild.containsKey(leaderboard_name)) {
						listDataHeader.add(leaderboard_name);

						listDataChild.put(leaderboard_name,
								new ArrayList<String>());
						listDataChild.get(leaderboard_name).add(
								user_name + "/" + user_points);
					} else {
						listDataChild.get(leaderboard_name).add(
								user_name + "/" + user_points);
					}

				}
				returndata.add(listDataChild);
				LeaderboardsActivity.listDataHeader = listDataHeader;
				LeaderboardsActivity.listDataChild = listDataChild;

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (responseType == "myQuestions") {

			JSONObject data;
			try {
				data = ((JSONObject) new JSONObject(s.toString()))
						.getJSONObject("data");
				Log.i("JSONParser", "data => " + data);
				Question quest;

				JSONArray questions = (JSONArray) new JSONArray(
						data.getString("Questions"));

				Log.i("JSONParser", "Questions => " + questions);

				String title;
				String description;
				String author;
				int question_id;
				String class_name;
				String dateTime;
				String totalPoints;
				int id;

				MyQuestionsActivity.values.clear();

				for (int i = 0; i < questions.length(); i++) {
					// returndata.add(users.get(i).toString());
					JSONObject q = questions.getJSONObject(i);

					title = q.get("title").toString();
					description = q.get("description").toString();
					class_name = q.get("class_name").toString();
					dateTime = q.get("formatted_created_at").toString();
					totalPoints = q.get("total_points").toString();
					question_id = Integer.parseInt(q.get("id").toString());
					id = q.getInt("id");

					JSONObject u = new JSONObject(q.getJSONObject("user")
							.toString());

					author = u.get("name").toString();

					System.out.println(title);
					System.out.println(description);
					System.out.println(class_name);
					System.out.println(author);
					System.out.println(dateTime);
					System.out.println(totalPoints);
					System.out.println(question_id);

					quest = new Question(title, dateTime, description, author,
							class_name, totalPoints, id);

					MyQuestionsActivity.values.add(quest);

					/*
					 * JSONObject data = ((JSONObject)new
					 * JSONObject(returnString)).getJSONObject("data");
					 * 
					 * 
					 * JSONArray notes = (JSONArray) data.get("notes");
					 * ArrayList<String> stringNotes = new ArrayList<String>();
					 * ArrayList<String> stringNotesDates = new
					 * ArrayList<String>();
					 * 
					 * for (int i =0; i < notes.length(); i++){ JSONObject note
					 * = new JSONObject(notes.get(i).toString());
					 * stringNotes.add(note.get("text").toString());
					 * stringNotesDates.add(note.get("created_at").toString());
					 * 
					 * } System.out.println("*****************************");
					 * MainActivity.Notes =
					 * stringNotes.toArray(MainActivity.Notes);
					 * MainActivity.NoteDates =
					 * stringNotesDates.toArray(MainActivity.NoteDates);
					 */

					// MainFeed.values2.add(returndata.get(i));

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if (responseType == "signin") {
			JSONObject data;
			try {
				data = ((JSONObject) new JSONObject(s.toString()))
						.getJSONObject("data");

				JSONObject error;

				error = (JSONObject) data.optJSONObject("Error");

				if (error != null) {
					Log.e("JSONParser",
							error.getString("Code")
									+ error.getString("Message"));

					Error errorModel = new Error(
							error.getString("Code"), error.getString("Message"));

					returndata.add(errorModel);
				} else {

					JSONObject user = (JSONObject) data.get("User");

					String auth_token = user.getString("auth_token");
					String user_id = user.getString("id");
					Log.i(JSONParser.class.getSimpleName(), "***************"
							+ auth_token);
					Log.i(JSONParser.class.getSimpleName(), "***************"
							+ user_id);
					// SignInActivity.auth_token = auth_token;
					returndata.add(auth_token);
					returndata.add(user_id);
					// StudyRoom.setPreference("auth_token", auth_token);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if (responseType == "signup") {
			JSONObject data;
			try {
				data = ((JSONObject) new JSONObject(s.toString()))
						.getJSONObject("data");

				JSONObject error = (JSONObject) data.getJSONObject("Error");

				if (error != null) {
					Log.e("JSONParser",
							error.getString("Code")
									+ error.getString("Message"));

					Error errorModel = new com.studyroom.model.Error(
							error.getString("Code"), error.getString("Message"));

					returndata.add(errorModel);
				} else {

					JSONObject user = (JSONObject) data.get("User");

					String auth_token = user.getString("auth_token");

					System.out.println("***************" + auth_token);
					SignUpActivity.auth_token = auth_token;
					// StudyRoom.setPreference("auth_token", auth_token);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return returndata;

	}
}
