package com.mobiroller.core.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mobiroller.core.activities.base.AveActivity;
import com.mobiroller.core.helpers.LegacyToolbarHelper;
import com.mobiroller.core.interfaces.ActivityComponent;
import com.mobiroller.core.R;

import butterknife.ButterKnife;

public class AuthorizationActivity extends AveActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorization_view);
        ButterKnife.bind(this);
        setToolbarContext();
    }

    @Override
    public AppCompatActivity injectActivity(ActivityComponent component) {
        component.inject(this);
        return this;
    }

    public void setToolbarContext() {
        LegacyToolbarHelper.setToolbar(this, findViewById(R.id.toolbar_top));
        getSupportActionBar().setTitle("Twitter");
    }
}
