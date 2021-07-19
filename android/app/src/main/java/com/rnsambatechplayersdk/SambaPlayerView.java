package com.rnsambatechplayersdk;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.google.gson.Gson;
import com.sambatech.player.SambaPlayer;
import com.sambatech.player.event.SambaEvent;
import com.sambatech.player.event.SambaEventBus;
import com.sambatech.player.model.SambaPlayerError;
import com.sambatech.player.offline.SambaDownloadManager;
import com.sambatech.player.offline.listeners.SambaDownloadRequestListener;
import com.sambatech.player.offline.model.DownloadState;
import com.sambatech.player.offline.model.SambaDownloadRequest;
import com.sambatech.player.offline.model.SambaTrack;

import java.util.Arrays;
import java.util.List;

public class SambaPlayerView extends SambaPlayer {
    public static final String REACT_CLASS = "SambaPlayerView";
    public static final Integer DEFAULT_INDEX = -1;

    private boolean hasStopped = false;

    private String mediaIdOrLiveChannelId;
    private String projectHash;
    private String accessToken;
    private String mediaTitle;
    private Integer startAtIndex = DEFAULT_INDEX;
    private MySambaPlayerListener sambaPlayerListener;
    private MySambaDownloadListener sambaDownloadListener;

    //Eventos que são encaminhados para o JS Code.
    public enum Events {
        ON_LOAD("topOnLoad"),
        ON_PLAY("topOnPlay"),
        ON_PAUSE("topOnPause"),
        ON_PROGRESS("topOnProgress"),
        ON_STOP("topOnStop"),
        ON_FINISH("topOnFinish"),
        ON_DESTROY("topOnDestroy"),
        ON_FULLSCREEN("topOnFullscreen"),
        ON_FULLSCREENEXIT("topOnFullscreenExit"),
        ON_CASTCONNECT("topOnCastConnect"),
        ON_CASTDISCONNECT("topOnCastDisconnect"),
        ON_CASTPLAY("topOnCastPlay"),
        ON_CASTPAUSE("topOnCastPause"),
        ON_CASTFINISH("topOnCastFinish"),
        ON_DOWNLOAD_PREPARE("topOnDownloadRequestPrepared"),
        ON_DOWNLOAD_PREPARE_FAILED("topOnDownloadRequestPreparedFailed"),
        ON_DOWNLOAD_STATE_CHANGED("topOnDownloadStateChanged")
        ;

        private final String mName;

        Events(final String name) {
            mName = name;
        }

        @Override
        public String toString() {
            return mName;
        }
    }

    public SambaPlayerView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.setReactNativeActivity(MainActivity.getInstance());
        MainActivity.setPlayerInstance(this);
        this.setSambaCast(MainActivity.getSambaCastInstance());
    }

    void registerPlayerListerner() {
        SambaEventBus.subscribe(this.sambaPlayerListener);

    }

    void registerDownloadListerner(){
        //Listerner para progresso de download
        SambaDownloadManager.getInstance().addDownloadListener(this.sambaDownloadListener);
    }

    @Override
    public void play() {
        if(this.sambaPlayerListener == null) {
            this.sambaPlayerListener = new MySambaPlayerListener(this);
            registerPlayerListerner();
        }
        if(this.sambaDownloadListener == null) {
            this.sambaDownloadListener = new MySambaDownloadListener(this);
            registerDownloadListerner();
        }
        super.play();
    }

    @Override
    public void destroy(SambaPlayerError error) {
        if(this.sambaPlayerListener != null) {
            SambaEventBus.unsubscribe(this.sambaPlayerListener);
        }
        if(this.sambaDownloadListener != null) {
            SambaDownloadManager.getInstance().removeDownloadListener(this.sambaDownloadListener);
        }
        this.sambaPlayerListener = null;
        this.sambaDownloadListener = null;
        super.destroy(error);
    }

    /** Propriedades para uso interno */

    public void setHasStopped(boolean hasStopped) {
        this.hasStopped = hasStopped;
    }

    public boolean hasStopped() {
        return hasStopped;
    }

    /** Propriedades do controle */

    public String getMediaIdOrLiveChannelId() {
        return mediaIdOrLiveChannelId;
    }

    public void setMediaIdOrLiveChannelId(String mediaIdOrLiveChannelId) {
        this.mediaIdOrLiveChannelId = mediaIdOrLiveChannelId;

    }

    public String getProjectHash() {
        return projectHash;
    }

    public void setProjectHash(String projectHash) {
        this.projectHash = projectHash;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setMediaTitle(String mediaTitle) {
        this.mediaTitle = mediaTitle;
    }

    public String getMediaTitle() {
        return mediaTitle;
    }

    public Integer getStartAtIndex() {
        return startAtIndex;
    }

    public void setStartAtIndex(Integer startAtIndex) {
        this.startAtIndex = startAtIndex;
    }

    @Override
    public void stop() {
        super.stop();
        //Necessário invocar o destroy, pois, por algum motivo, quando chamados o método stop e depois o play, o video não volta a tocar.
        //Apenas voltar a funcionar depois do destroy. Essa propriedade é utlizada no método SambaPlayerViewManager.receiveCommand

        this.hasStopped = true;
    }

    /**
     * Dispara o evento para o JS code.
     * @param eventName
     * @param e
     */
    void dispatchNativeSambaEvent(String eventName, SambaEvent e) {
        WritableMap event = Arguments.createMap();
        Gson parser = new Gson();
        if(e.getData() != null && !eventName.equals(Events.ON_LOAD.toString())) {
            event.putString("data", parser.toJson(e.getData()));
        }
        if(e.getDataAll() != null && e.getDataAll().length > 0 && !eventName.equals(Events.ON_LOAD.toString())) {
            event.putString("dataAll", parser.toJson(e.getDataAll()));
        }
        if(eventName.equals(Events.ON_LOAD.toString())){
            Log.i(REACT_CLASS, eventName);
        }
        if(e.getType() != null) {
            event.putString("type", parser.toJson(e.getType()));
        }
        ReactContext reactContext = (ReactContext)getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                getId(),
                eventName,
                event
        );
    }

    /**
     * Registra o listenet no componente Android do SambaPlayer para propagar os eventos para o React-Native
      */



    //Sem o código abaixo, o player fica preto no react-native app
    @Override
    public void requestLayout() {
        super.requestLayout();
        post(measureAndLayout); //Render video surface
    }

    private final Runnable measureAndLayout = new Runnable() {
        @Override
        public void run() {
            measure(
                    MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
            layout(getLeft(), getTop(), getRight(), getBottom());
        }
    };


    /** TRATAMENTO PARA DOWNNLOAD DO VIDEO - MODO OFFLINE**/

    /**
     * Realiza o prepareDownload do SambaPlayer.
     */
    public void prepareDownload(SambaDownloadRequest actualDownloadRequest) {
        SambaDownloadManager.getInstance().prepareDownload(actualDownloadRequest, new SambaDownloadRequestListener() {
            @Override
            public void onDownloadRequestPrepared(SambaDownloadRequest sambaDownloadRequest) {
                dispatchOnDownloadRequestPrepared(new SambaPlayerDownloadsAvailable(sambaDownloadRequest));
            }

            @Override
            public void onDownloadRequestFailed(Error error, String msg) {
                dispatchOnDownloadRequestFailed(error,msg);
            }
        });
    }

    /**
     * Dispara evento de onPrepareDownload passando as informações sobre o video, qualidade, legendas, etc.
     * Com isso, o código JS pode apresentar para o usuário as opções de download.
     * @param e
     */
    public void dispatchOnDownloadRequestPrepared(SambaPlayerDownloadsAvailable e){
        WritableMap event = Arguments.createMap();
        Gson parser = new Gson();
        if(e != null) {
            event.putString("data", parser.toJson(e));
        }

        ReactContext reactContext = (ReactContext)getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                getId(),
                "topOnDownloadRequestPrepared",
                event
        );
    }

    /**
     * Dispara evento de onPrepareDownloadFailed passando as informações erro.
     * Esse evento é disparado quando dá erro na tentativa de pegar as informações para download.
     * @param error
     */
    public void dispatchOnDownloadRequestFailed(Error error, String msg) {
        WritableMap event = Arguments.createMap();
        Gson parser = new Gson();
        if(error != null) {
            event.putString("data", parser.toJson(error));
        }

        event.putString("message", msg);

        ReactContext reactContext = (ReactContext)getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                getId(),
                "topOnDownloadRequestPreparedFailed",
                event
        );
    }

    /**
     * Realiza o download do video para uso em offline.
     * @param selectedVideoTrackIndex
     */
    public void performDownload(SambaDownloadRequest actualDownloadRequest, int selectedVideoTrackIndex){
        List<SambaTrack> selectedTrack = Arrays.asList(actualDownloadRequest.getSambaVideoTracks().get(selectedVideoTrackIndex));
        actualDownloadRequest.setSambaTracksForDownload(selectedTrack);
        actualDownloadRequest.enableAllSubtitlesForDownload(); // Habilita o download de todas legendas existentes
        SambaDownloadManager.getInstance().performDownload(actualDownloadRequest);
    }


    public void dispatchNativeEventOnDownloadStateChanged(DownloadState downloadState) {
        WritableMap event = Arguments.createMap();
        Gson parser = new Gson();
        Log.i(REACT_CLASS, parser.toJson(downloadState));
        if(downloadState != null) {
            event.putString("data", parser.toJson(downloadState));
        }


        ReactContext reactContext = (ReactContext)getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                getId(),
                "topOnDownloadStateChanged",
                event
        );
    }





}
