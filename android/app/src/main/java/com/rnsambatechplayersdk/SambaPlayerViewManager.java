package com.rnsambatechplayersdk;

import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.react.bridge.AssertionException;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.rnsambatechplayersdk.callbacks.MediaObtainedCallback;
import com.sambatech.player.SambaApi;
import com.sambatech.player.SambaPlayer;
import com.sambatech.player.event.SambaApiCallback;
import com.sambatech.player.model.SambaMedia;
import com.sambatech.player.model.SambaMediaConfig;
import com.sambatech.player.model.SambaMediaRequest;
import com.sambatech.player.offline.SambaDownloadManager;
import com.sambatech.player.offline.model.SambaDownloadRequest;
import com.sambatech.player.plugins.DrmRequest;

import java.util.Map;

import javax.annotation.Nonnull;


public class SambaPlayerViewManager extends SimpleViewManager<SambaPlayerView> {
    public static final String REACT_CLASS = "SambaPlayerView";

    public static final int COMMAND_LOAD_MEDIA = 1;
    public static final int COMMAND_PLAY = 2;
    public static final int COMMAND_PAUSE = 3;
    public static final int COMMAND_STOP = 4;
    public static final int COMMAND_PREPARE_DOWNLOAD = 5;
    public static final int COMMAND_PERFORM_DOWNLOAD = 6;

    private SambaDownloadRequest actualDownloadRequest;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected SambaPlayerView createViewInstance(ThemedReactContext reactContext) {
        Log.i(REACT_CLASS, "createViewInstance ");
        return new SambaPlayerView(reactContext, null);
    }

    @ReactProp(name = "mediaIdOrLiveChannelId")
    public void setMediaIdOrLiveChannelId(SambaPlayerView videoView, String mediaIdOrLiveChannelId) {
        Log.i(REACT_CLASS, "mediaIdOrLiveChannelId = " + mediaIdOrLiveChannelId);
        videoView.setMediaIdOrLiveChannelId(mediaIdOrLiveChannelId);
    }

    @ReactProp(name = "projectHash")
    public void setProjectHash(SambaPlayerView videoView, String projectHash) {
        Log.i(REACT_CLASS, "projectHash = " + projectHash);
        videoView.setProjectHash(projectHash);
    }

    @ReactProp(name = "accessToken")
    public void setAccessToken(SambaPlayerView videoView, String accessToken) {
        Log.i(REACT_CLASS, "accessToken = " + accessToken);
        videoView.setAccessToken(accessToken);
    }

    @ReactProp(name = "autoFullscreenMode", defaultBoolean = false)
    public void setAutoFullscreenMode(SambaPlayerView videoView, boolean autoFullscreenMode) {
        Log.i(REACT_CLASS, "autoFullscreenMode = " + autoFullscreenMode);
        videoView.setAutoFullscreenMode(autoFullscreenMode);
    }

    @ReactProp(name = "enableControls", defaultBoolean = false)
    public void setEnableControls(SambaPlayerView videoView, boolean enableControls) {
        Log.i(REACT_CLASS, "enableControls = " + enableControls);
        videoView.setControlsVisibility(enableControls);
    }

    @ReactProp(name = "mediaTitle")
    public void setMediaTitle(SambaPlayerView videoView, String mediaTitle) {
        Log.i(REACT_CLASS, "mediaTitle = " + mediaTitle);
        videoView.setMediaTitle(mediaTitle);
    }

    @ReactProp(name = "startAtIndex", defaultInt = 0)
    public void setStartAtIndex(SambaPlayerView videoView, Integer startAtIndex) {
        Log.i(REACT_CLASS, "startAtIndex = " + String.valueOf(startAtIndex));
        videoView.setStartAtIndex(startAtIndex);
    }

    @Override
    public void onDropViewInstance(@Nonnull SambaPlayerView view) {
        super.onDropViewInstance(view);
        view.destroy();
    }


    /** Configuração de eventos que são repassados do Player para o JS **/

    @Override
    @Nullable
    public Map getExportedCustomDirectEventTypeConstants() {
        MapBuilder.Builder builder = MapBuilder.builder();
        for (SambaPlayerView.Events event : SambaPlayerView.Events.values()) {
            builder.put(event.toString(), MapBuilder.of("registrationName", event.toString()));
        }
        return builder.build();
    }


    /** Tratamento dos commandos enviados pelo código js para o controle android **/


    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        MapBuilder.Builder builder = MapBuilder.builder();
        builder
                .put("loadMedia",COMMAND_LOAD_MEDIA)
                .put("play",COMMAND_PLAY)
                .put("pause",COMMAND_PAUSE)
                .put("stop",COMMAND_STOP)
                .put("prepareDownload",COMMAND_PREPARE_DOWNLOAD)
                .put("performDownload",COMMAND_PERFORM_DOWNLOAD)
        ;
        return builder.build();
    }

    @Override
    public void receiveCommand(final SambaPlayerView videoView, int commandId, @Nullable ReadableArray args) {
        // This will be called whenever a command is sent from react-native.
        switch (commandId) {
            case COMMAND_LOAD_MEDIA:
                loadMedia(videoView, media -> {
                    //Atribui o title informado via JS. Para ocultar o title, basta setar ""
                    if (videoView.getMediaTitle() != null)
                        media.title = videoView.getMediaTitle();
                    videoView.setMedia(media);

                    //Verifica parâmetro de autoPLay
                    if(args != null && args.getBoolean(0)) {
                        videoView.play();
                        if (videoView.getStartAtIndex() != SambaPlayerView.DEFAULT_INDEX)
                            videoView.seek(videoView.getStartAtIndex());
                    }

                });

                break;

            case COMMAND_PLAY:
                if(!videoView.isPlaying()) {
//                    if(videoView.hasStopped()){
//                        videoView.destroy();
//                    }

                    loadMedia(videoView, media -> {
//                        if(videoView.hasStopped()) {
//                            videoView.registerListerners();
//                        }
                        //Quanto está nulo é pq o comando de PLAY está vindo após um PAUSE, ou seja, já existe midia.
                        if(media != null) {
                            videoView.setMedia(media);
                        }
                        if(MainActivity.getSambaCastInstance() != null && MainActivity.getSambaCastInstance().isCasting()){
                            MainActivity.getSambaCastInstance().playCast();
                        }
                        else {
                            videoView.play();
                            videoView.setHasStopped(false);
                        }
                    });

                }
                break;
            case COMMAND_PAUSE:
                if(MainActivity.getSambaCastInstance() != null && MainActivity.getSambaCastInstance().isCasting()){
                    MainActivity.getSambaCastInstance().pauseCast();
                }
                else {
                    if (videoView.isPlaying())
                        videoView.pause();
                }
                break;
            case COMMAND_STOP:
//                if(videoView.isPlaying()) {
//                    videoView.setHasStopped(true);
//                }
                if(MainActivity.getSambaCastInstance() != null && MainActivity.getSambaCastInstance().isCasting()){
                    MainActivity.getSambaCastInstance().stopCasting();
                }
                videoView.destroy();
                break;
            case COMMAND_PREPARE_DOWNLOAD:
                this.actualDownloadRequest = new SambaDownloadRequest(videoView.getProjectHash(), videoView.getMediaIdOrLiveChannelId());
                videoView.prepareDownload(actualDownloadRequest);
                break;
            case COMMAND_PERFORM_DOWNLOAD:
                if(args != null) {
                    videoView.performDownload(actualDownloadRequest, args.getInt(0));
                }
                break;
        }
    }

    private void loadMedia(final SambaPlayerView videoView, MediaObtainedCallback callback){
        if(videoView.getAccessToken() == null)
            throw new AssertionException("accessToken null");
        if(videoView.getProjectHash() == null)
            throw new AssertionException("projectHash null");
        if(videoView.getMediaIdOrLiveChannelId() == null)
            throw new AssertionException("mediaIdOrLiveChannelId null");

        //Se não tem midia, entao carrega
        if(videoView.getMedia().url == null) {

            SambaPlayer player = videoView;

            String mediaID = videoView.getMediaIdOrLiveChannelId();

            SambaApi api = new SambaApi(videoView.getContext(), videoView.getAccessToken());// Criar uma instância de SambaApi

            // Verifica se a media está baixada
            if (SambaDownloadManager.getInstance().isDownloaded(mediaID)) {

                // Obtém a instância da media offline
                SambaMedia sambaMedia = SambaDownloadManager.getInstance().getDownloadedMedia(mediaID);

                api.prepareOfflineMedia(sambaMedia, new SambaApiCallback() {

                    // Invocado quando a media offline estiver pronta para ser executada
                    @Override
                    public void onMediaResponse(SambaMedia media) {
                        // Neste ponto a media offline está pronta para reprodução
                        callback.accept(media);
                    }

                    @Override
                    public void onMediaResponseError(Exception e, SambaMediaRequest request) { // Invocado caso ocorra algum erro na preparação da media offline
                        super.onMediaResponseError(e, request);
                    }
                });

            }else {
                //Se nao achou midia offline, tenta pegar online
                api.requestMedia(new SambaMediaRequest(videoView.getProjectHash(), videoView.getMediaIdOrLiveChannelId()), new SambaApiCallback() {
                    @Override
                    public void onMediaResponse(SambaMedia media) {
                        callback.accept(media);
                    }
                });
            }
        }

        else{
            //caso contrário chama o callback
            callback.accept(null);
        }
    }
}
