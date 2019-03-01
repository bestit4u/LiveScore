package com.diretta.livescore.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diretta.livescore.R;

public class PremiumGuide6Fragment extends Fragment {

	private View rootView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_premium_guide6, container, false);
		return rootView;
	}
}
