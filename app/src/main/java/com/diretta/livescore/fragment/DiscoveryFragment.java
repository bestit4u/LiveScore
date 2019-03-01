package com.diretta.livescore.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.diretta.livescore.R;

public class DiscoveryFragment extends Fragment {

	public int index;
	private View rootView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_discovery, container, false);
		ImageView imgGuide = (ImageView)rootView.findViewById(R.id.imgGuide);
		TextView txtTitle = (TextView)rootView.findViewById(R.id.txtTitle);
		switch (index){
			case 0:
				imgGuide.setImageDrawable(getContext().getResources().getDrawable(R.drawable.soccer_bg_discover));
				txtTitle.setText(getContext().getResources().getText(R.string.soccer));
				break;
			case 1:
				imgGuide.setImageDrawable(getContext().getResources().getDrawable(R.drawable.tennis_bg_discover));
				txtTitle.setText("Tennis");
				break;
			case 2:
				imgGuide.setImageDrawable(getContext().getResources().getDrawable(R.drawable.basketball_bg_discover));
				txtTitle.setText("BasketBall");
				break;
			case 3:
				imgGuide.setImageDrawable(getContext().getResources().getDrawable(R.drawable.cricket_bg_discover));
				txtTitle.setText("Cricket");
				break;
			default:
				break;
		}
		return rootView;
	}
}
