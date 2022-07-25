package com.mobiroller.core.fragments.preview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobiroller.core.R2;
import com.mobiroller.core.fragments.BaseFragment;
import com.mobiroller.core.interfaces.FragmentComponent;
import com.mobiroller.core.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotSupportedFragment extends BaseFragment {

    @BindView(R2.id.not_supported_info)
    TextView notSupportedText;

    private int mModuleName = R.string.chat_module;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_preview_not_supported, container, false);
        ButterKnife.bind(this, view);
        if(mModuleName == 0)
        notSupportedText.setText(getString(R.string.info_module_not_supported_on_preview, getString(mModuleName)));
        return view;
    }

    public void setModule(int moduleName)
    {
        mModuleName = moduleName;
    }

    @Override
    public Fragment injectFragment(FragmentComponent component) {
        component.inject(this);
        return this;
    }
}
