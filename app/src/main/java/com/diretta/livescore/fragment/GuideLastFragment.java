package com.diretta.livescore.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.diretta.livescore.GuideActivity;
import com.diretta.livescore.ProfileActivity;
import com.diretta.livescore.R;

public class GuideLastFragment extends Fragment {

	//	DownloadAsyncTask downloadAsyncTask;
	private View rootView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_last_guide, container, false);
		ImageView imgGuide = (ImageView)rootView.findViewById(R.id.imgGuide);
		ImageView imgClose = (ImageView)rootView.findViewById(R.id.imgClose);
		imgClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), ProfileActivity.class);
				getContext().startActivity(intent);
			}
		});
		return rootView;
	}
}
