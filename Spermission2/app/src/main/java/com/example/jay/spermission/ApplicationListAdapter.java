package com.example.jay.spermission;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ApplicationListAdapter extends BaseAdapter {
	
	private final Activity activity;
	private final List<ApplicationListItem> items;
	
	public ApplicationListAdapter(Activity activity, List<ApplicationListItem> items) {
		this.activity = activity;
		this.items = items;
	}

	public int getCount() {
		return items.size();
	}

	public ApplicationListItem getItem(int position) {
		return items.get(position);
	}

	public long getItemId(int position) {
		return items.get(position).getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater layoutInflater = activity.getLayoutInflater();
			convertView = layoutInflater.inflate(R.layout.application_list_item, null);
		}
		TextView textViewName = (TextView)convertView.findViewById(R.id.listviewapplicationname);
		textViewName.setText(items.get(position).getText());
		ImageView imageView = (ImageView)convertView.findViewById(R.id.listviewapplicationnameimage);
		imageView.setImageDrawable(items.get(position).getIcon());
		
		return convertView;
	}

}
