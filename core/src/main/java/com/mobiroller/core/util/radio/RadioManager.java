package com.mobiroller.core.util.radio;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.metadata.MetadataOutput;

import org.greenrobot.eventbus.EventBus;

public class RadioManager {

    private static RadioManager instance = null;

    private static RadioService service;

    private Context context;

    private boolean serviceBound;

    public String streamUrl;

    private String metadata;

    private RadioManager(Context context) {
        this.context = context;
        serviceBound = false;
    }

    public static RadioManager with(Context context) {

        if (instance == null)
            instance = new RadioManager(context);

        return instance;
    }

    public static RadioService getService(){
        return service;
    }

    public void playOrPause(String streamUrl, String screenName){
        this.streamUrl = streamUrl;

        service.playOrPause(streamUrl,screenName);
    }

    public boolean isPlaying() {

        return service.isPlaying();
    }

    public void addListeners(Player.EventListener listener, MetadataOutput metadataOutput) {
        service.addEventListener(listener);
        service.addMetadataListener(metadataOutput);
    }


    public void bind() {

        Intent intent = new Intent(context, RadioService.class);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        if(service != null)
            EventBus.getDefault().post(service.getStatus());
    }

    public void unbind() {

        context.unbindService(serviceConnection);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder binder) {

            service = ((RadioService.LocalBinder) binder).getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

            serviceBound = false;
        }
    };

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
        service.setTitle(metadata);
    }
}
