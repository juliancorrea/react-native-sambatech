package com.rnsambatechplayersdk;

import android.util.Log;

import com.sambatech.player.event.SambaEvent;
import com.sambatech.player.event.SambaPlayerListener;

public class MySambaPlayerListener extends SambaPlayerListener {

    public static final String REACT_CLASS = "SambaPlayerView";
    SambaPlayerView playerView = null;

    public MySambaPlayerListener(SambaPlayerView playerView){
        this.playerView = playerView;
    }

    private void dispatchNativeSambaEvent(String eventName, SambaEvent e){
        this.playerView.dispatchNativeSambaEvent(eventName, e);
    }

    @Override
    public void onLoad(SambaEvent e) {
        Log.i(REACT_CLASS, "onLoad");
        dispatchNativeSambaEvent("topOnLoad", e);
    }

    @Override
    public void onPlay(SambaEvent e) {
        Log.i(REACT_CLASS, "onPlay");
        dispatchNativeSambaEvent("topOnPlay", e);

    }

    @Override
    public void onPause(SambaEvent e) {
        Log.i(REACT_CLASS, "onPause");
        dispatchNativeSambaEvent("topOnPause", e);
    }

    @Override
    public void onStop(SambaEvent e) {
        Log.i(REACT_CLASS, "onStop");
        dispatchNativeSambaEvent("topOnStop", e);
    }

    @Override
    public void onProgress(SambaEvent e) {
        Log.i(REACT_CLASS, "onProgress");
        dispatchNativeSambaEvent("topOnProgress", e);
    }

    @Override
    public void onFinish(SambaEvent e) {
        Log.i(REACT_CLASS, "onFinish");
        dispatchNativeSambaEvent("topOnFinish", e);
    }

    @Override
    public void onFullscreen(SambaEvent e) {
        Log.i(REACT_CLASS, "onFullscreen");
        dispatchNativeSambaEvent("topOnFullscreen", e);
    }

    @Override
    public void onFullscreenExit(SambaEvent e) {
        Log.i(REACT_CLASS, "onFullscreenExit");
        dispatchNativeSambaEvent("topOnFullscreenExit", e);
    }

    @Override
    public void onDestroy(SambaEvent e) {
        Log.i(REACT_CLASS, "onDestroy");
        dispatchNativeSambaEvent("topOnDestroy", e);
    }

    @Override
    public void onCastConnect(SambaEvent e) {
        Log.i(REACT_CLASS, "onCastConnect");
        dispatchNativeSambaEvent("topOnCastConnect", e);
    }

    @Override
    public void onCastDisconnect(SambaEvent e) {
        Log.i(REACT_CLASS, "onCastDisconnect");
        dispatchNativeSambaEvent("topOnCastDisconnect", e);
    }

    @Override
    public void onCastPlay(SambaEvent e) {
        Log.i(REACT_CLASS, "onCastPlay");
        dispatchNativeSambaEvent("topOnCastPlay", e);
    }

    @Override
    public void onCastPause(SambaEvent e) {
        Log.i(REACT_CLASS, "onCastPause");
        dispatchNativeSambaEvent("topOnCastPause", e);
    }

    @Override
    public void onCastFinish(SambaEvent e) {
        Log.i(REACT_CLASS, "onCastFinish");
        dispatchNativeSambaEvent("topOnCastFinish", e);
    }

}