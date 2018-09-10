package vtcc.lalazen.com.demovtcc.speedtotextcyberspace.myservice.text2bot2speech;

import android.media.AudioTrack;

import vtcc.lalazen.com.demovtcc.speedtotextcyberspace.listeners.ChangeAdapterListener;


/**
 * Created by Toan on 4/4/2018.
 */

public abstract class TTSService {
    protected ChangeAdapterListener changeAdapterListener;
    protected AudioTrack player;

    public void setChangeAdapterListener(ChangeAdapterListener changeAdapterListener) {
        this.changeAdapterListener = changeAdapterListener;
    }
    public abstract void sendText2Bot(String message);

}
