package com.rnsambatechplayersdk;

import android.util.Log;

import com.sambatech.player.offline.listeners.SambaDownloadListener;
import com.sambatech.player.offline.model.DownloadState;

public class MySambaDownloadListener implements SambaDownloadListener {

    public static final String REACT_CLASS = "SambaPlayerView";
    SambaPlayerView playerView = null;

    public MySambaDownloadListener(SambaPlayerView playerView){
        this.playerView = playerView;
    }


    @Override
    public void onDownloadStateChanged(DownloadState downloadState) {
        Log.i(REACT_CLASS, "onDownloadStateChanged");
        playerView.dispatchNativeEventOnDownloadStateChanged(downloadState);
    }
}
