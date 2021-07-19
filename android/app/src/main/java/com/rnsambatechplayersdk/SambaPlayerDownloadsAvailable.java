package com.rnsambatechplayersdk;

import com.sambatech.player.offline.model.SambaDownloadRequest;
import com.sambatech.player.offline.model.SambaTrack;

import java.util.ArrayList;
import java.util.List;

public class SambaPlayerDownloadsAvailable {

    private String mediaIdOrLiveChannelId;
    private String projectHash;
    private List<VideoTrack> videotracks = new ArrayList<>();


    public SambaPlayerDownloadsAvailable(SambaDownloadRequest downloadRequest){
        this.projectHash = downloadRequest.getProjectHash();
        this.mediaIdOrLiveChannelId = downloadRequest.getMediaId();

        for (SambaTrack vt : downloadRequest.getSambaVideoTracks()) {
            videotracks.add(new VideoTrack(vt, downloadRequest.getSambaVideoTracks().indexOf(vt)));
        }

    }

    public String getMediaIdOrLiveChannelId() {
        return mediaIdOrLiveChannelId;
    }

    public String getProjectHash() {
        return projectHash;
    }

    public List<VideoTrack> getVideotracks() {
        return videotracks;
    }

    class VideoTrack {

        public int index = -1;
        public String title = null;


        public VideoTrack(SambaTrack track, int trackIndex){
            index = trackIndex;
            title = track.toString();
        }
    }
}
