package com.mobiroller.core.activities.preview;

import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mobiroller.core.models.events.ScreenshotEvent;
import com.mobiroller.core.R;

import org.greenrobot.eventbus.EventBus;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class StoragePermissionActivity extends AppCompatActivity {

    public static String sScreenshot = "Screenshot";
    private boolean mIsScreenshot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().hasExtra(sScreenshot))
            mIsScreenshot = true;
        StoragePermissionActivityPermissionsDispatcher.askLocationPermissionWithPermissionCheck(this);
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void askLocationPermission() {
        if (mIsScreenshot)
            EventBus.getDefault().post(new ScreenshotEvent());
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        StoragePermissionActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    @OnShowRationale({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void showRationaleForStorage(final PermissionRequest request) {
        request.proceed();
    }

    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void showDeniedForStorage() {
        finish();
        if (mIsScreenshot)
            Toast.makeText(this, R.string.permission_storage_denied_ss, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void showNeverAskForStorage() {
        finish();
        if (mIsScreenshot)
            Toast.makeText(this, R.string.permission_storage_denied_ss_never, Toast.LENGTH_SHORT).show();
    }
}
