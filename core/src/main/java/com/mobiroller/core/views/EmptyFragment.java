package com.mobiroller.core.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.mobiroller.core.R;

@SuppressLint("NewApi") 
public class EmptyFragment extends Fragment {
	
	Context context;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//Pass your layout xml to the inflater and assign it to rootView.
		View rootView = inflater.inflate(R.layout.empty_fragment, container, false); 
        context = rootView.getContext(); // Assign your rootView to context
        return rootView;
	}

}
