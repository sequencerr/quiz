package com.example.quiz;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
	private final Question[] mQuestions = new Question[]{
			new Question(R.string.question_australia),
			new Question(R.string.question_oceans),
			new Question(R.string.question_mideast, false),
			new Question(R.string.question_africa, false),
			new Question(R.string.question_americas),
			new Question(R.string.question_asia),
	};
	private TextView mQuestionTextView;
	private TextView mIndicatorTextView;
	private int mQuestionId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mQuestionTextView = findViewById(R.id.question_text_view);
		mIndicatorTextView = findViewById(R.id.question_indicator);

		updateQuestionText();
		findViewById(R.id.next_button).setOnClickListener(view -> goNextOrPrevQuestion(true));
		findViewById(R.id.prev_button).setOnClickListener(view -> goNextOrPrevQuestion(false));
		findViewById(R.id.false_button).setOnClickListener(view -> showAnswer(!mQuestions[mQuestionId].isCorrectAnswer()));
		findViewById(R.id.true_button).setOnClickListener(view -> showAnswer(mQuestions[mQuestionId].isCorrectAnswer()));
	}

	private void goNextOrPrevQuestion(boolean nextOrBack) {
		if (nextOrBack && mQuestionId == mQuestions.length - 1) return;
		else if (!nextOrBack && mQuestionId == 0) return;

		mQuestionId += nextOrBack ? 1 : -1;
		updateQuestionText();
	}

	@SuppressLint("SetTextI18n")
	private void updateQuestionText() {
		mIndicatorTextView.setText("Question №" + (mQuestionId + 1));
		mQuestionTextView.setText(mQuestions[mQuestionId].getTextResID());
	}

	private void showAnswer(boolean isAnswerCorrect) {
		goNextOrPrevQuestion(true);
		Toast toast = Toast.makeText(MainActivity.this, isAnswerCorrect ? R.string.correct_toast : R.string.incorrect_toast, Toast.LENGTH_SHORT);
		toast.show();
	}
}