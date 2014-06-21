package com.kairosive.kairosive;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class PieActivity extends Activity {
	/** Colors to be used for the pie slices. */
	private static int[] COLORS = new int[] { Color.parseColor("#C44D58"),
			Color.parseColor("#FF8B6B"), Color.parseColor("#FABC2A"),
			Color.parseColor("#A5EF4A"), Color.parseColor("#0087CE"),
			Color.parseColor("#015F8A") };
	/** The main series that will include all the data. */
	private CategorySeries mSeries = new CategorySeries("");
	/** The main renderer for the main dataset. */
	private DefaultRenderer mRenderer = new DefaultRenderer();
	/** The chart view that displays the data. */
	private GraphicalView mChartView;

	@Override
	protected void onRestoreInstanceState(Bundle savedState) {
		super.onRestoreInstanceState(savedState);
		mSeries = (CategorySeries) savedState.getSerializable("current_series");
		mRenderer = (DefaultRenderer) savedState
				.getSerializable("current_renderer");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("current_series", mSeries);
		outState.putSerializable("current_renderer", mRenderer);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xy_chart);
//		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setStartAngle(180);
//		mRenderer.setDisplayValues(true);
		setupActionBar();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mChartView == null) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
			mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
			mRenderer.setClickEnabled(true);
			
			mRenderer.setLegendHeight(40);
			mRenderer.setLegendTextSize(40);
			mRenderer.setFitLegend(true);
			
			mChartView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SeriesSelection seriesSelection = mChartView
							.getCurrentSeriesAndPoint();
					if (seriesSelection == null) {
						// Toast.makeText(PieActivity.this,
						// "No chart element selected", Toast.LENGTH_SHORT)
						// .show();
					} else {
						for (int i = 0; i < mSeries.getItemCount(); i++) {
							mRenderer.getSeriesRendererAt(i).setHighlighted(
									i == seriesSelection.getPointIndex());
						}
						mChartView.repaint();
						// Toast.makeText(
						// PieActivity.this,
						// "Chart data point index "
						// + seriesSelection.getPointIndex()
						// + " selected" + " point value="
						// + seriesSelection.getValue(),
						// Toast.LENGTH_SHORT).show();
					}
				}
			});
			layout.addView(mChartView, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		} else {
			mChartView.repaint();
		}

		if (mSeries.getItemCount() < 6) {

			mSeries.add(
					"Personal",
					getIntent().getIntExtra(
							"com.jamescho.kairosive.personalSeconds", 1));
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			renderer.setColor(COLORS[0]);
			mRenderer.addSeriesRenderer(renderer);
			mChartView.repaint();

			mSeries.add(
					"Family",
					getIntent().getIntExtra(
							"com.jamescho.kairosive.familySeconds", 1));
			SimpleSeriesRenderer renderer2 = new SimpleSeriesRenderer();
			renderer2.setColor(COLORS[1]);
			mRenderer.addSeriesRenderer(renderer2);
			mChartView.repaint();

			mSeries.add(
					"Work",
					getIntent().getIntExtra(
							"com.jamescho.kairosive.workSeconds", 1));
			SimpleSeriesRenderer renderer3 = new SimpleSeriesRenderer();
			renderer3.setColor(COLORS[2]);
			mRenderer.addSeriesRenderer(renderer3);
			mChartView.repaint();

			mSeries.add(
					"Social",
					getIntent().getIntExtra(
							"com.jamescho.kairosive.socialSeconds", 1));
			SimpleSeriesRenderer renderer4 = new SimpleSeriesRenderer();
			renderer4.setColor(COLORS[3]);
			mRenderer.addSeriesRenderer(renderer4);
			mChartView.repaint();

			mSeries.add(
					"Faith",
					getIntent().getIntExtra(
							"com.jamescho.kairosive.faithSeconds", 1));
			SimpleSeriesRenderer renderer5 = new SimpleSeriesRenderer();
			renderer5.setColor(COLORS[4]);
			mRenderer.addSeriesRenderer(renderer5);
			mChartView.repaint();

			mSeries.add(
					"Misc.",
					getIntent().getIntExtra(
							"com.jamescho.kairosive.miscSeconds", 1));
			SimpleSeriesRenderer renderer6 = new SimpleSeriesRenderer();
			renderer6.setColor(COLORS[5]);
			mRenderer.addSeriesRenderer(renderer6);
			mChartView.repaint();
		}

	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

}
