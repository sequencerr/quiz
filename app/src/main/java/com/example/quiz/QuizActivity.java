package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
	private static final String TAG = "QuizActivity";
	private static final String QUESTION_ID_INDEX = "questionID";
	private static final String ANSWERED_INDEX = "answeredQuestions";
	private static final String IS_CHEATER_INDEX = "isCheater";
	private final Question[] mQuestions = {
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
	private boolean isCheater;
	private TextView mQuestionTextView;
	private TextView mIndicatorTextView;
	private Button mCheatButton;
	// onActivityResult, startActivityForResult, requestPermissions, and onRequestPermissionsResult are deprecated. https://stackoverflow.com/q/62671106
	// There are no request codes
	ActivityResultLauncher<Intent> cheatActivityLauncher = registerForActivityResult(
			new ActivityResultContracts.StartActivityForResult(),
			result -> {
				if (result.getResultCode() == RESULT_OK && result.getData() != null) {
					Intent data = result.getData();
					answeredQuestions[mQuestionId] = 2;
					isCheater = CheatActivity.wasAnswerShown(data);
					mCheatButton.setVisibility(View.GONE);
				}
			});

	private static String format(String s, Object... args) {
		return String.format(Locale.ENGLISH, s, args);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate(Bundle) called");
		setContentView(R.layout.activity_main);

		if (isAllAnswered()) {
			showRetryActivity();
			return;
		}

		mQuestionTextView = findViewById(R.id.question_text_view);
		mIndicatorTextView = findViewById(R.id.question_indicator);
		mCheatButton = findViewById(R.id.cheat_button);

		if (savedInstanceState != null) {
			mQuestionId = savedInstanceState.getByte(QUESTION_ID_INDEX);
			answeredQuestions = savedInstanceState.getByteArray(ANSWERED_INDEX);
			isCheater = savedInstanceState.getBoolean(IS_CHEATER_INDEX, false);
		}

		updateQuestionText();

		findViewById(R.id.next_button).setOnClickListener(v -> switchQuestion(true));
		findViewById(R.id.prev_button).setOnClickListener(v -> switchQuestion(false));
		findViewById(R.id.false_button).setOnClickListener(v -> showAnswer(!mQuestions[mQuestionId].isCorrectAnswer()));
		findViewById(R.id.true_button).setOnClickListener(v -> showAnswer(mQuestions[mQuestionId].isCorrectAnswer()));

		mCheatButton.setOnClickListener(v -> {
			Intent intent = CheatActivity.newIntent(this, mQuestions[mQuestionId].isCorrectAnswer());
			cheatActivityLauncher.launch(intent);
		});
	}


	@Override
	protected void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.i(TAG, "onSaveInstanceState() called");
		outState.putByte(QUESTION_ID_INDEX, mQuestionId);
		outState.putByteArray(ANSWERED_INDEX, answeredQuestions);
		outState.putBoolean(IS_CHEATER_INDEX, isCheater);
	}

	private void showAnswer(boolean isAnswerCorrect) {
		if (!isCheater) answeredQuestions[mQuestionId] = (byte) (isAnswerCorrect ? 1 : 2);

		int messageResId = isCheater ? R.string.judgment_toast : isAnswerCorrect ? R.string.correct_toast : R.string.incorrect_toast;
		Toast.makeText(QuizActivity.this, messageResId, Toast.LENGTH_SHORT)
				.show();

		switchQuestion(true);
	}

	private void switchQuestion(boolean nextOrBack) {
		if (isAllAnswered()) {
			showRetryActivity();
			return;
		}

		if (nextOrBack && mQuestionId == mQuestions.length - 1) mQuestionId = 0;
		else if (!nextOrBack && mQuestionId == 0) mQuestionId = (byte) (mQuestions.length - 1);
		else mQuestionId += nextOrBack ? 1 : -1;

		updateQuestionText();
	}

	private void showRetryActivity() {
		int rightAnswers = getRightAnswersCount();
		String resultText = format("Right answers %d of %d (%d%%)", rightAnswers, mQuestions.length, rightAnswers * 100 / mQuestions.length);
		Intent intent = ResultActivity.newIntent(this, resultText);
		finish();
		startActivity(getIntent());
		startActivity(intent);
		return;
	}

	private void updateQuestionText() {
		boolean isCurrentQuestionAnswered = answeredQuestions[mQuestionId] != 0;
		mCheatButton.setVisibility(isCurrentQuestionAnswered ? View.GONE : View.VISIBLE);
		isCheater = false;
		findViewById(R.id.buttons_layout).setVisibility(isCurrentQuestionAnswered ? View.GONE : View.VISIBLE);
		mIndicatorTextView.setText(format("Question №%d", mQuestionId + 1));
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
