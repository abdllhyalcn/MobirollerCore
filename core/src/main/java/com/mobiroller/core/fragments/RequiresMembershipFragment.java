package com.mobiroller.core.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobiroller.core.R;

/**
 * Created by ealtaca on 9/4/17.
 */

public class RequiresMembershipFragment extends Fragment {

    TextView errorText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requires_membership, container, false);
        errorText = view.findViewById(R.id.empty_view_text);
        if (getArguments()!=null && getArguments().containsKey("invalidAccount"))
            errorText.setText(R.string.chat_action_invalid_account);
        return view;
    }
}
