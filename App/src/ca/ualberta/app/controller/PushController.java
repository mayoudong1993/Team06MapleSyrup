/*
 * Copyright 2014 Anni Dai
 * Copyright 2014 Bicheng Yan
 * Copyright 2014 Liwen Chen
 * Copyright 2014 Liang Jingjing
 * Copyright 2014 Xiaocong Zhou
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.ualberta.app.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionList;
import ca.ualberta.app.models.Reply;

/**
 * Will be completed in the next part of the project
 */
public class PushController {
	public Map<Long, Question> waitingListMap_Question;
	public ArrayList<Long> waitingListId_Question;
	private String WAITMAP_Q = "waitMap_Question.sav";
	private String WAITID_Q = "waitId_Question.sav";
	public Map<Long, Answer> waitingListMap_Answer;
	public ArrayList<Long> waitingListId_Answer;
	private String WAITMAP_A = "waitMap_Answer.sav";
	private String WAITID_A = "waitId_Answer.sav";
	public Map<Long, Reply> waitingListMap_Reply;
	public ArrayList<Long> waitingListId_Reply;
	private String WAITMAP_R = "waitMap_Reply.sav";
	private String WAITID_R = "waitId_Reply.sav";

	/**
	 * The constructor of the class load question map from local file
	 * 
	 * @param mcontext
	 *            the context.
	 */
	public PushController(Context mcontext) {
		waitingListMap_Question = loadMapFromFile(mcontext, WAITMAP_Q);
		waitingListId_Question = loadIdFromFile(mcontext, WAITID_Q);
	}

	/**
	 * Return the question map of the waitingList question from local file
	 * 
	 * @param mcontext
	 *            the context.
	 * 
	 * @return the question map of the waitingList question from the local file.
	 */
	public Map<Long, Question> getWaitingListMap(Context mcontext) {
		waitingListMap_Question = loadMapFromFile(mcontext, WAITMAP_Q);
		return waitingListMap_Question;

	}

	/**
	 * Return the question ID of the waitingList question from local file
	 * 
	 * @param mcontext
	 *            the context.
	 * 
	 * @return the question ID of the waitingList question from the local file.
	 */
	public ArrayList<Long> getWaitingListId(Context mcontext) {
		waitingListId_Question = loadIdFromFile(mcontext, WAITID_Q);
		return waitingListId_Question;

	}

	/**
	 * Return whether the local file for local question contain the question.
	 * 
	 * @param mcontext
	 *            the context.
	 * @param question
	 *            the question.
	 * 
	 * @return true if the local file for favorite question has the question,
	 *         false if not.
	 */
	public boolean hasWaited(Context mcontext, Question question) {
		waitingListMap_Question = loadMapFromFile(mcontext, WAITMAP_Q);
		if (waitingListMap_Question.get(question.getID()) == null)
			return false;
		return true;
	}
	
	/**
	 * Return whether the local file for local question contain the question.
	 * 
	 * @param mcontext
	 *            the context.
	 * @param question
	 *            the question.
	 * 
	 * @return true if the local file for favorite question has the question,
	 *         false if not.
	 */
	public boolean hasWaited_A(Context mcontext, Answer answer) {
		waitingListMap_Answer = loadMapFromFile_A(mcontext, WAITMAP_A);
		if (waitingListMap_Answer.get(answer.getID()) == null)
			return false;
		return true;
	}
	

	/**
	 * Save a question into the the local file of the WaitingList questions.
	 * 
	 * @param mcontext
	 *            the context.
	 * @param question
	 *            the question.
	 */
	public void addWaitngListQuestions(Context mcontext, Question question) {
		waitingListMap_Question = loadMapFromFile(mcontext, WAITMAP_Q);
		waitingListId_Question = loadIdFromFile(mcontext, WAITID_Q);
		if (!hasWaited(mcontext, question)) {
			waitingListMap_Question.put(question.getID(), question);
			waitingListId_Question.add(question.getID());
			saveInFile(mcontext, waitingListMap_Question, WAITMAP_Q);
			saveInFile(mcontext, waitingListId_Question, WAITID_Q);
		}
	}

	/**
	 * Save a question into the the local file of the WaitingList questions.
	 * 
	 * @param mcontext
	 *            the context.
	 * @param question
	 *            the question.
	 */
	public void addWaitngListAnswers(Context mcontext, Answer answer,
			String questionTitle) {
		waitingListMap_Answer = loadMapFromFile_A(mcontext, WAITMAP_A);
		waitingListId_Answer = loadIdFromFile(mcontext, WAITID_A);
		if (!hasWaited_A(mcontext, answer)) {
			waitingListMap_Answer.put(answer.getID(), answer);
			waitingListId_Answer.add(answer.getID());
			saveInFile_A(mcontext, waitingListMap_Answer, WAITMAP_A);
			saveInFile(mcontext, waitingListId_Answer, WAITID_A);
		}
	}

	/**
	 * Remove a question into the the local file of the WaitingList questions.
	 * 
	 * @param mcontext
	 *            the context.
	 * @param question
	 *            the question.
	 */
	public void removeWaitingListQuestions(Context mcontext, Question question) {
		waitingListMap_Question = loadMapFromFile(mcontext, WAITMAP_Q);
		waitingListId_Question = loadIdFromFile(mcontext, WAITID_Q);
		waitingListMap_Question.remove(question.getID());
		for (int i = 0; i < waitingListId_Question.size(); i++) {
			if (waitingListId_Question.get(i) == question.getID()) {
				waitingListId_Question.remove(i);
				break;
			}
		}
		saveInFile(mcontext, waitingListMap_Question, WAITMAP_Q);
		saveInFile(mcontext, waitingListId_Question, WAITID_Q);
	}

	/**
	 * Update a question in the the local file of the WaitingList questions.
	 * 
	 * @param mcontext
	 *            the context.
	 * @param question
	 *            the question.
	 */
	public void updateWaitingListQuestions(Context mcontext, Question question) {
		waitingListMap_Question = loadMapFromFile(mcontext, WAITMAP_Q);
		if (waitingListMap_Question.get(question.getID()) != null) {
			waitingListMap_Question.remove(question.getID());
			waitingListMap_Question.put(question.getID(), question);
			saveInFile(mcontext, waitingListMap_Question, WAITMAP_Q);
		}
	}

	/**
	 * Load and return the WaitingList question list
	 * 
	 * @return the local question list.
	 */
	public QuestionList getWaitingQuestionList(Context mcontext) {
		waitingListMap_Question = loadMapFromFile(mcontext, WAITMAP_Q);
		QuestionList questionList = new QuestionList();
		questionList.getCollection().addAll(
				this.waitingListMap_Question.values());
		return questionList;
	}

	/**
	 * Clear all MAP's in the local files
	 */
	public void clear() {
		waitingListMap_Question.clear();
	}

	/**
	 * Load the question ID's from the file with given name.
	 * 
	 * @param context
	 *            The context.
	 * @param FILENAME
	 *            The name of the local file.
	 * 
	 * @return the ID of the question(s).
	 */
	public ArrayList<Long> loadIdFromFile(Context context, String FILENAME) {
		ArrayList<Long> questionId = null;
		try {
			FileInputStream fis = context.openFileInput(FILENAME);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			Gson gson = new Gson();
			// Following line from
			// https://sites.google.com/site/gson/gson-user-guide 2014-09-23
			Type listType = new TypeToken<ArrayList<Long>>() {
			}.getType();
			questionId = gson.fromJson(in, listType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (questionId == null)
			return questionId = new ArrayList<Long>();
		return questionId;
	}

	/**
	 * Load the question Map's from the file with given name.
	 * 
	 * @param context
	 *            The context.
	 * @param FILENAME
	 *            The name of the local file.
	 * 
	 * @return the Map of the question(s).
	 */
	public Map<Long, Question> loadMapFromFile(Context context, String FILENAME) {
		Map<Long, Question> questionMap = null;
		try {
			FileInputStream fis = context.openFileInput(FILENAME);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			Gson gson = new Gson();
			// Following line from
			// https://sites.google.com/site/gson/gson-user-guide 2014-09-23
			Type listType = new TypeToken<Map<Long, Question>>() {
			}.getType();
			questionMap = gson.fromJson(in, listType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (questionMap == null)
			return questionMap = new HashMap<Long, Question>();
		return questionMap;
	}
	
	/**
	 * Load the question Map's from the file with given name.
	 * 
	 * @param context
	 *            The context.
	 * @param FILENAME
	 *            The name of the local file.
	 * 
	 * @return the Map of the question(s).
	 */
	public Map<Long, Answer> loadMapFromFile_A(Context context, String FILENAME) {
		Map<Long, Answer> answerMap = null;
		try {
			FileInputStream fis = context.openFileInput(FILENAME);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			Gson gson = new Gson();
			// Following line from
			// https://sites.google.com/site/gson/gson-user-guide 2014-09-23
			Type listType = new TypeToken<Map<Long, Question>>() {
			}.getType();
			answerMap = gson.fromJson(in, listType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (answerMap == null)
			return answerMap = new HashMap<Long, Answer>();
		return answerMap;
	}
	

	/**
	 * save question map to local file
	 * 
	 * @param context
	 *            The context.
	 * @param questionMap
	 *            The question map.
	 * @param FILENAME
	 *            The name of the file.
	 */
	public void saveInFile(Context context, Map<Long, Question> questionMap,
			String FILENAME) {
		try {
			FileOutputStream fos = context.openFileOutput(FILENAME, 0);
			Gson gson = new Gson();
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(questionMap, osw);
			osw.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * save question map to local file
	 * 
	 * @param context
	 *            The context.
	 * @param questionMap
	 *            The question map.
	 * @param FILENAME
	 *            The name of the file.
	 */
	public void saveInFile_A(Context context, Map<Long, Answer> answerMap,
			String FILENAME) {
		try {
			FileOutputStream fos = context.openFileOutput(FILENAME, 0);
			Gson gson = new Gson();
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(answerMap, osw);
			osw.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * save question id to local file
	 * 
	 * @param context
	 *            The context.
	 * @param questionMap
	 *            The question id.
	 * @param FILENAME
	 *            The name of the file.
	 */
	public void saveInFile(Context context, ArrayList<Long> questionId,
			String FILENAME) {
		try {
			FileOutputStream fos = context.openFileOutput(FILENAME, 0);
			Gson gson = new Gson();
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(questionId, osw);
			osw.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
