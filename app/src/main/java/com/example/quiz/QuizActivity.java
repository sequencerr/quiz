package com.example.quiz;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("SetTextI18n")
public class QuizActivity extends AppCompatActivity {
	private static final String TAG = "QuizActivity";
	private static final String KEY_INDEX = "index";
	private final Question[] mQuestions = new Question[]{
			new Question(R.string.question_australia),
			new Question(R.string.question_oceans),
			new Question(R.string.question_mideast, false),
			new Question(R.string.question_africa, false),
			new Question(R.string.question_americas),
			new Question(R.string.question_asia),
	};
	// array index = question id, values: 0 - unanswered; 1 - answered correctly; 2 - answered incorrectly
	private byte[] answeredQuestions = new byte[mQuestions.length];
	private byte mQuestionId = 0;
	private TextView mQuestionTextView;
	private TextView mIndicatorTextView;
	private ImageButton nextButton;
	private ImageButton prevButton;
	private Button mFalseButton;
	private Button mTrueButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate(Bundle) called");
		setContentView(R.layout.activity_main);

		mQuestionTextView = findViewById(R.id.question_text_view);
		mIndicatorTextView = findViewById(R.id.question_indicator);
		nextButton = findViewById(R.id.next_button);
		prevButton = findViewById(R.id.prev_button);
		mFalseButton = findViewById(R.id.false_button);
		mTrueButton = findViewById(R.id.true_button);

		if (savedInstanceState != null) {
			mQuestionId = savedInstanceState.getByte(KEY_INDEX);
			answeredQuestions = savedInstanceState.getByteArray(KEY_INDEX);
		}

		updateQuestionText();
		mFalseButton.setOnClickListener(view -> showAnswer(!mQuestions[mQuestionId].isCorrectAnswer()));
		mTrueButton.setOnClickListener(view -> showAnswer(mQuestions[mQuestionId].isCorrectAnswer()));
		nextButton.setOnClickListener(view -> goNextOrPrevQuestion(true));
		prevButton.setOnClickListener(view -> goNextOrPrevQuestion(false));
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.i(TAG, "onSaveInstanceState() called");
		outState.putByte(KEY_INDEX, mQuestionId);
		outState.putByteArray(KEY_INDEX, answeredQuestions);
	}

	private void showAnswer(boolean isAnswerCorrect) {
		answeredQuestions[mQuestionId] = (byte) (isAnswerCorrect ? 1 : 2);

		Toast toast = Toast.makeText(QuizActivity.this, isAnswerCorrect ? R.string.correct_toast : R.string.incorrect_toast, Toast.LENGTH_SHORT);
		toast.show();

		goNextOrPrevQuestion(true);
	}

	private void goNextOrPrevQuestion(boolean nextOrBack) {
		if (isAllAnswered()) {
			mFalseButton.setVisibility(View.GONE);
			mTrueButton.setVisibility(View.GONE);
			nextButton.setVisibility(View.GONE);
			prevButton.setVisibility(View.GONE);
			mIndicatorTextView.setVisibility(View.GONE);
			int rightAnswers = getRightAnswersCount();
			mQuestionTextView.setText("Right answers " + rightAnswers + " of " + mQuestions.length + " (" + rightAnswers * 100 / mQuestions.length + "%)");
			return;
		}

		if (nextOrBack && mQuestionId == mQuestions.length - 1) mQuestionId = 0;
		else if (!nextOrBack && mQuestionId == 0) mQuestionId = (byte) (mQuestions.length - 1);
		else mQuestionId += nextOrBack ? 1 : -1;

		updateQuestionText();
	}

	private void updateQuestionText() {
		if (answeredQuestions[mQuestionId] != 0) {
			if (mFalseButton.getVisibility() == View.VISIBLE)
				mFalseButton.setVisibility(View.INVISIBLE);
			if (mTrueButton.getVisibility() == View.VISIBLE)
				mTrueButton.setVisibility(View.INVISIBLE);
		} else {
			if (mFalseButton.getVisibility() == View.INVISIBLE)
				mFalseButton.setVisibility(View.VISIBLE);
			if (mTrueButton.getVisibility() == View.INVISIBLE)
				mTrueButton.setVisibility(View.VISIBLE);
		}
		mIndicatorTextView.setText("Question №" + (mQuestionId + 1));
		mQuestionTextView.setText(mQuestions[mQuestionId].getTextResID());
	}

	private int getRightAnswersCount() {
		byte counter = 0;
		for (byte answer : answeredQuestions) if (answer == 1) counter++;
		return counter;
	}

	private boolean isAllAnswered() {
		for (byte answeredQuestion : answeredQuestions) if (answeredQuestion == 0) return false;
		return true;
	}
}
