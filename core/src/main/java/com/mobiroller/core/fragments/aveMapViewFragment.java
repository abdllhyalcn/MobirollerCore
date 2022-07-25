package com.mobiroller.core.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobiroller.core.R2;
import com.mobiroller.core.activities.preview.LocationPermissionActivity;
import com.mobiroller.core.helpers.LegacyProgressViewHelper;
import com.mobiroller.core.coreui.models.FlagModel;
import com.mobiroller.core.models.events.MapUserLocationEvent;
import com.mobiroller.core.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NewApi")
public class aveMapViewFragment extends BaseModuleFragment implements OnMapReadyCallback {

    @BindView(R2.id.mapView)
    MapView mapView;

    @BindView(R2.id.map_layout)
    RelativeLayout mapLayout;

    LegacyProgressViewHelper legacyProgressViewHelper;
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_layout, container, false);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, view);

        legacyProgressViewHelper = new LegacyProgressViewHelper(getActivity());
        if (networkHelper.isConnected()) {
            legacyProgressViewHelper.show();
            try {
                try {

                    mapView.onCreate(savedInstanceState);
                } catch (Exception e) {

                    e.printStackTrace();
                }
                try {
                    MapsInitializer.initialize(getActivity());
                } catch (Exception e) {

                    e.printStackTrace();
                }
                mapView.getMapAsync(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.please_check_your_internet_connection), Toast.LENGTH_LONG).show();
        }
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (mapLayout != null) {
            bannerHelper.addBannerAd(mapLayout, mapView);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (getActivity()!=null && !getActivity().isFinishing() && mMap == null && screenModel != null) {
            //Check user location is active?
            if (screenModel.getShowUserLocation() != null && screenModel.getShowUserLocation().equalsIgnoreCase("YES")) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
                    turnGPSOn();
                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                } else {
                    startActivity(new Intent(getActivity(), LocationPermissionActivity.class).putExtra(LocationPermissionActivity.sMapUserLocation, true));
                }

            }
            //Check and set map type
            if (screenModel.getMapType().equalsIgnoreCase("std"))
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            else if (screenModel.getMapType().equalsIgnoreCase("s"))
                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            else if (screenModel.getMapType().equalsIgnoreCase("h"))
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


            ArrayList<FlagModel> jsonArray = screenModel.getFlags();
            List<Marker> markers = new ArrayList<>();

            LatLng firstLatLong = null;

            if (jsonArray.size() > 0) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    final FlagModel jsonObject = jsonArray.get(i);
                    String markerLat = jsonObject.getLattitude();
                    String markerLong = jsonObject.getLongitude();
                    String title = localizationHelper.getLocalizedTitle(jsonObject.getTitle());
                    String detail = localizationHelper.getLocalizedTitle(jsonObject.getDetail());
                    LatLng location = new LatLng(Double.valueOf(markerLat), Double.valueOf(markerLong));
                    if(i == 0)
                        firstLatLong = location;
                    // Add marker to googleMap
                    Marker marker = googleMap.addMarker(new MarkerOptions().position(location)
                            .title(title).snippet(detail));
                    markers.add(marker);
                }
            }

            //Calculate zoom location and zoom level to show all markers
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            int count = 0;
            for (Marker marker : markers) {
                builder.include(marker.getPosition());
                count++;
            }
            if (count > 1) {
                LatLngBounds bounds = builder.build();
                int padding = 200; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                try {
                    googleMap.animateCamera(cu);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            } else if (count == 1 && firstLatLong != null) {
                CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(firstLatLong, 12.0f);
                try {
                    googleMap.animateCamera(cu);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        } else
            mMap = googleMap;
        legacyProgressViewHelper.dismiss();


    }

    @Override
    public void onStop() {
        turnGPSOff();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        turnGPSOff();
        super.onDestroyView();
    }

    @SuppressLint("MissingPermission")
    @Subscribe
    public void onPostMapUserLocationEvent(MapUserLocationEvent e) {
        if (ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            turnGPSOn();
            mapView.getMapAsync(this);
        }
    }

    private void turnGPSOn(){
        String provider = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            getActivity().sendBroadcast(poke);
        }
    }

    private void turnGPSOff(){
        boolean isGpsEnabled = false;
        if(isGpsEnabled) {
            String provider = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

            if (provider.contains("gps")) { //if gps is enabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                getActivity().sendBroadcast(poke);
            }
        }
    }
}
