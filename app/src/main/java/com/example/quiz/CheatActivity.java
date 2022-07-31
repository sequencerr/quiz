package com.example.quiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class CheatActivity extends AppCompatActivity {
	private static final String TAG = "QuizActivity";
	private static final String KEY_INDEX = "index";
	private static final String EXTRA_ANSWER_IS_TRUE =
			"com.example.android.quiz.answer_is_true";
	private static final String EXTRA_ANSWER_SHOWN =
			"com.example.android.quiz.answer_shown";
	private boolean isCheated = false;

	public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
		Intent intent = new Intent(packageContext, CheatActivity.class);
		intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
		return intent;
	}

	public static boolean wasAnswerShown(Intent result) {
		return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cheat);

		findViewById(R.id.go_back_button).setOnClickListener(view -> finish());

		if (savedInstanceState != null) if (savedInstanceState.getBoolean(KEY_INDEX)) {
			showAnswer();
			return;
		}

		findViewById(R.id.show_answer_button).setOnClickListener(view -> showAnswer());
	}

	private void showAnswer() {
		isCheated = true;
		int text = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false) ? R.string.true_button : R.string.false_button;
		((TextView) findViewById(R.id.answer_text_view)).setText(text);
		setResultIsShown();
	}

	@Override
	protected void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.i(TAG, "onSaveInstanceState() called");
		outState.putBoolean(KEY_INDEX, isCheated);
	}

	private void setResultIsShown() {
		Intent data = new Intent();
		data.putExtra(EXTRA_ANSWER_SHOWN, true);
		setResult(RESULT_OK, data);
	}
}