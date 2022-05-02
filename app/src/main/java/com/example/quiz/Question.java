package com.example.quiz;

public class Question {
	private int mTextResID;
	private boolean mCorrectAnswer = true;

	Question(int textResID) {
		mTextResID = textResID;
	}

	Question(int textResID, boolean correctAnswer) {
		mTextResID = textResID;
		mCorrectAnswer = correctAnswer;
	}

	public int getTextResID() {
		return mTextResID;
	}

	public void setTextResID(int textResID) {
		mTextResID = textResID;
	}

	public boolean isCorrectAnswer() {
		return mCorrectAnswer;
	}

	public void setCorrectAnswer(boolean correctAnswer) {
		mCorrectAnswer = correctAnswer;
	}
}
