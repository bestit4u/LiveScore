package com.diretta.livescore.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.diretta.livescore.R;
import com.diretta.livescore.GuideActivity;

public class GuideNormalFragment extends Fragment {

	public int index;
	private View rootView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_normal_guide, container, false);
		ImageView imgGuide = (ImageView)rootView.findViewById(R.id.imgGuide);
		switch (index){
			case 0:
				imgGuide.setImageDrawable(getContext().getResources().getDrawable(R.drawable.guide1));
				break;
			case 1:
				imgGuide.setImageDrawable(getContext().getResources().getDrawable(R.drawable.guide2));
				break;
			case 2:
				imgGuide.setImageDrawable(getContext().getResources().getDrawable(R.drawable.guide3));
				break;
			default:
				break;
		}
		return rootView;
	}
}
