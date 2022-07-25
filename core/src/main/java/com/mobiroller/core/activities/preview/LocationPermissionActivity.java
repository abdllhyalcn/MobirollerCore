package com.mobiroller.core.activities.preview;

import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mobiroller.core.models.events.MapUserLocationEvent;
import com.mobiroller.core.R;

import org.greenrobot.eventbus.EventBus;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class LocationPermissionActivity extends AppCompatActivity {

    public static String sMapUserLocation = "MapUserLocation";
    private boolean mMapUserLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().hasExtra(sMapUserLocation))
        mMapUserLocation = true;
        LocationPermissionActivityPermissionsDispatcher.askLocationPermissionWithPermissionCheck(this);
    }

    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void askLocationPermission() {
        if (mMapUserLocation)
            EventBus.getDefault().post(new MapUserLocationEvent());
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        LocationPermissionActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    @OnShowRationale({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void showRationaleForStorage(final PermissionRequest request) {
        request.proceed();
    }

    @OnPermissionDenied({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void showDeniedForStorage() {
        finish();
        if (mMapUserLocation)
            Toast.makeText(this, R.string.permission_location_denied_map_user_location, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void showNeverAskForStorage() {
        finish();
        if (mMapUserLocation)
            Toast.makeText(this, R.string.permission_location_denied_map_user_location_never, Toast.LENGTH_SHORT).show();
    }
}
