package com.kairosive.kairosive;

import static com.kairosive.utils.KSTimeUtils.isAM;
import static com.kairosive.utils.KSTimeUtils.isValid;
import static com.kairosive.utils.KSTimeUtils.parseHours;
import static com.kairosive.utils.KSTimeUtils.parseMinutes;
import static com.kairosive.utils.KSTimeUtils.time24ToString;
import static com.kairosive.utils.KSTimeUtils.to24HourClock;
import static com.kairosive.utils.KSTimeUtils.formatTime;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kairosive.database.ActivityPojo;
import com.kairosive.utils.Constants;
import com.kairosive.utils.KSTimeUtils.Format;

public class DetailsActivity extends FragmentActivity {

	public static final int RESULT_DELETE = 7;
	public static final int RESULT_UPDATE = 8;

	private int receivedPosition;
	private ActivityPojo receivedActivity;
	private ActivityPojo updatedActivity;

	private Spinner activitySpinner;
	private TextView startDate;
	private TextView startTimeTv;
	private TextView endDate;
	private TextView endTimeTv;
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

	/**
	 * @param view
	 */
	public void onEditClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.edit_start_time:
		case R.id.start_time_label_textView:

			DialogFragment newFragment = new TimePickerFragment();
			((TimePickerFragment) newFragment).init(startTimeTv,
					updatedActivity,
					parseHours(updatedActivity.getStart_time()),
					parseMinutes(updatedActivity.getStart_time()),
					isAM(updatedActivity.getStart_time()));
			newFragment.show(getSupportFragmentManager(), "TimePicker");
			break;

		case R.id.edit_end_time:
		case R.id.end_time_label_textView:
			DialogFragment newFragment2 = new TimePickerFragment();
			((TimePickerFragment) newFragment2).init(endTimeTv,
					updatedActivity, parseHours(updatedActivity.getEnd_time()),
					parseMinutes(updatedActivity.getEnd_time()),
					isAM(updatedActivity.getEnd_time()));
			newFragment2.show(getSupportFragmentManager(), "TimePicker");
			break;

		case R.id.edit_details:
		case R.id.details_label_textView:
			editDetails();
			break;
		}

	}

	private void populateTextViews() {

		startDate = (TextView) findViewById(R.id.start_date_textView);
		startTimeTv = (TextView) findViewById(R.id.start_time_textView);
		endDate = (TextView) findViewById(R.id.end_date_textView);
		endTimeTv = (TextView) findViewById(R.id.end_time_textView);
		details = (TextView) findViewById(R.id.details_textView);

		startDate.setText(receivedActivity.getStart_date());
		startTimeTv.setText(formatTime(Format.REMOVE_SECONDS,
				receivedActivity.getStart_time()));
		endDate.setText(receivedActivity.getEnd_date());
		endTimeTv.setText(formatTime(Format.REMOVE_SECONDS,
				receivedActivity.getEnd_time()));
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

		if (!isValid(updatedActivity.getStart_time(),
				updatedActivity.getEnd_time())) {
			
			System.out.println(updatedActivity.getStart_time());
			System.out.println(updatedActivity.getEnd_time());
			
			Toast.makeText(getApplicationContext(),
					"Update Failed: End Time must be later than Start Time!",
					Toast.LENGTH_SHORT).show();
			return;
		}

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

		private int initial24Hour;
		private int initialMinute;
		private TextView timeTvToEdit;
		private ActivityPojo updatedActivity;

		public void init(TextView timeTextView, ActivityPojo receivedActivity,
				int current12Hour, int currentMinute, boolean isAM) {

			this.timeTvToEdit = timeTextView;
			this.updatedActivity = receivedActivity;
			initial24Hour = to24HourClock(current12Hour, isAM);
			this.initialMinute = currentMinute;

		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			return new TimePickerDialog(getActivity(), this, initial24Hour,
					initialMinute, false);
		}

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			if (timeTvToEdit.getId() == R.id.start_time_textView) {
				updatedActivity
						.setStart_time(time24ToString(hourOfDay, minute));
			} else if (timeTvToEdit.getId() == R.id.end_time_textView) {
				updatedActivity.setEnd_time(time24ToString(hourOfDay, minute));
			}
			timeTvToEdit.setText(formatTime(Format.REMOVE_SECONDS, time24ToString(hourOfDay, minute)));
		}
	}

	private void editDetails() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Enter Details");

		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);

		builder.setPositiveButton("Save",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						details.setText(input.getText().length() == 0 ? "none"
								: input.getText());
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
