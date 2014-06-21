package com.kairosive.kairosive;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.kairosive.kairosive.database.ActivityPojo;
import com.kairosive.kairosive.database.Constants;

public class DetailsActivity extends FragmentActivity {

	public static final int RESULT_DELETE = 7;
	public static final int RESULT_UPDATE = 8;

	private int receivedPosition;
	private ActivityPojo receivedActivity;
	private ActivityPojo updatedActivity;

	// private TextView activityLabel;
	// private TextView startLabel;
	private Spinner activitySpinner;
	private TextView startDate;
	private TextView startTime;
	// private TextView endLabel;
	private TextView endDate;
	private TextView endTime;
	// private TextView detailsLabel;
	private TextView details;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		receivedPosition = getIntent().getIntExtra("SELECTED_POSITION", -1);
		receivedActivity = (ActivityPojo) getIntent().getSerializableExtra(
				"SELECTED_ACTIVITY");
		updatedActivity = receivedActivity.clone();
		populateTextViews();
	}

	public void onEditClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.edit_start_time:
		case R.id.start_time_label_textView:
			DialogFragment newFragment = new TimePickerFragment();
			newFragment.show(getSupportFragmentManager(), "TimePicker");
			break;

		case R.id.edit_end_time:
		case R.id.end_time_label_textView:
			DialogFragment newFragment2 = new TimePickerFragment();
			newFragment2.show(getSupportFragmentManager(), "TimePicker");
			break;

		case R.id.edit_details:
		case R.id.details_label_textView:
			editDetails();
			break;
		}

	}

	private void populateTextViews() {

		// activityLabel = (TextView)
		// findViewById(R.id.activity_details_label_textView);
		// startLabel = (TextView) findViewById(R.id.start_time_label_textView);
		startDate = (TextView) findViewById(R.id.start_date_textView);
		startTime = (TextView) findViewById(R.id.start_time_textView);
		// endLabel = (TextView) findViewById(R.id.end_time_label_textView);
		endDate = (TextView) findViewById(R.id.end_date_textView);
		endTime = (TextView) findViewById(R.id.end_time_textView);
		// detailsLabel = (TextView) findViewById(R.id.details_label_textView);
		details = (TextView) findViewById(R.id.details_textView);

		startDate.setText(receivedActivity.getStart_date());
		startTime.setText(receivedActivity.getStart_time());
		endDate.setText(receivedActivity.getEnd_date());
		endTime.setText(receivedActivity.getEnd_time());
		details.setText(receivedActivity.getDetails().equals("") ? "none"
				: receivedActivity.getDetails());
		populateSpinner();
	}

	private void populateSpinner() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, Constants.category);
		activitySpinner = (Spinner) findViewById(R.id.activity_spinner);
		activitySpinner.setAdapter(adapter);
		activitySpinner.setSelection(receivedActivity.getCategory_id());

	}

	public void onCancel(View v) {
		finish();
	}

	public void onUpdate(View v) {
		updatedActivity.setCategory_id(activitySpinner
				.getSelectedItemPosition());
		receivedActivity = updatedActivity;
		Intent intent = new Intent();
		intent.putExtra("UPDATED_ACTIVITY", receivedActivity);
		setResult(RESULT_UPDATE, intent);
		finish();
	}

	public void onDelete(View v) {
		Intent intent = new Intent();
		intent.putExtra("SELECTED_POSITION", receivedPosition);
		setResult(RESULT_DELETE, intent);
		finish();
	}

	public static class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {

		public static int currentHour24;
		public static int currentMinute;

		public static void setTime(int currentHour, int currentMinute,
				boolean isPM) {

			if (isPM) {
				if (currentHour != 12) {
					currentHour += 12;
				}
			} else {
				if (currentHour == 12) {
					currentHour -= 12;
				}
			}

			TimePickerFragment.currentHour24 = currentHour;
			TimePickerFragment.currentMinute = currentMinute;

		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			return new TimePickerDialog(getActivity(), null, currentHour24,
					currentMinute, false);
		}

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

		}
	}

	private void editDetails() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Enter Details");

		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);

		// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				details.setText(input.getText().length() == 0 ? "none" : input
						.getText());
				updatedActivity.setDetails(input.getText().toString());
			}
		});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		builder.show();
	}

}
