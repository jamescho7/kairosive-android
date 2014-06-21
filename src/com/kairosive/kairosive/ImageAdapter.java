package com.kairosive.kairosive;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;

	public ImageAdapter(Context c) {
		mContext = c;
	}

	public int getCount() {
		return imageIds.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return -1;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;

		if (convertView == null) {
			imageView = new ImageView(mContext);
		} else {
			imageView = (ImageView) convertView;
		}

		float parentWidth = parent.getWidth();
		float parentHeight = parent.getHeight();

		// If landscape
		if (parentWidth > parentHeight) {
			int[] dimensions = determineOptimalIconSize(true, parentWidth
					/ parentHeight);

			((GridView) parent).setColumnWidth(parent.getWidth()
					/ dimensions[0]);
			imageView.setLayoutParams(new GridView.LayoutParams(parent
					.getWidth() / dimensions[0], parent.getWidth()
					/ dimensions[0]));

		} else {
			int[] dimensions = determineOptimalIconSize(false, parentHeight
					/ parentWidth);
			((GridView) parent).setColumnWidth(parent.getWidth()
					/ dimensions[1]);
			imageView.setLayoutParams(new GridView.LayoutParams(parent
					.getWidth() / dimensions[1], parent.getWidth()
					/ dimensions[1]));
		}

		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setImageResource(imageIds[position]);
		// imageView.setBackgroundColor(Color.parseColor(getColor(position)));

		return imageView;
	}

	private int[] determineOptimalIconSize(boolean landscape, float ratio) {
		int[] result = new int[2];

		if (landscape == false) {

			if (ratio >= 7 / 3) {
				result[0] = 7;
				result[1] = 3;
			} else if (ratio >= 1.5) {
				result[0] = 6;
				result[1] = 4;
			} else {
				result[0] = 5;
				result[1] = 5;
			}
		} else {

			if (ratio >= 1.5) {
				result[0] = 7;
				result[1] = 3;
			} else if (ratio > 1) {
				result[0] = 6;
				result[1] = 4;
			} else {
				result[0] = 5;
				result[1] = 5;
			}
		}

		return result;
	}

//	private String getColor(int category) {
//		if (category >= 0 && category < 8) {
//			return "#C44D58";
//		} else if (category >= 8 && category < 12) {
//			return "#FF6B6B";
//		} else if (category >= 12 && category < 14) {
//			return "#FABC2A";
//		} else if (category >= 14 && category < 17) {
//			return "#A5EF4A";
//		} else if (category >= 17 && category < 20) {
//			return "#0087CE";
//		} else {
//			return "#015F8A";
//		}
//	}

	private Integer[] imageIds = { R.drawable.icon01, R.drawable.icon02,
			R.drawable.icon03, R.drawable.icon04, R.drawable.icon05,
			R.drawable.icon06, R.drawable.icon07, R.drawable.icon08,
			R.drawable.icon09, R.drawable.icon10, R.drawable.icon11,
			R.drawable.icon12, R.drawable.icon13, R.drawable.icon14,
			R.drawable.icon15, R.drawable.icon16, R.drawable.icon17,
			R.drawable.icon18, R.drawable.icon19, R.drawable.icon20,
			R.drawable.icon21 };
}