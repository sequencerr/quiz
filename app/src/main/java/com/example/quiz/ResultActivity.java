package com.example.quiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
	private static final String EXTRA_RESULT_ACTIVITY =
			"com.example.android.quiz.result_activity";

	public static Intent newIntent(Context packageContext, String result) {
		Intent intent = new Intent(packageContext, ResultActivity.class);
		intent.putExtra(EXTRA_RESULT_ACTIVITY, result);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		((TextView) findViewById(R.id.result_text)).setText(getIntent().getStringExtra(EXTRA_RESULT_ACTIVITY));
		findViewById(R.id.retry_button).setOnClickListener(v -> {
			setResult(RESULT_OK);
			finish();
		});
	}
}