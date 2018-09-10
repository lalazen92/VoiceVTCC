package vtcc.lalazen.com.demovtcc;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import vtcc.lalazen.com.demovtcc.speedtotextcyberspace.MySharePreferenceVoice;
import vtcc.lalazen.com.demovtcc.speedtotextcyberspace.listeners.ChangeAdapterListener;
import vtcc.lalazen.com.demovtcc.speedtotextcyberspace.listeners.StopRecordListener;
import vtcc.lalazen.com.demovtcc.speedtotextcyberspace.myservice.speech2text.STTService;
import vtcc.lalazen.com.demovtcc.speedtotextcyberspace.myservice.speech2text.VoiceClient;

public class MainActivity extends AppCompatActivity implements StopRecordListener, ChangeAdapterListener {

    private STTService voiceClient;
    private static final String FRAGMENT_MESSAGE_DIALOG = "message_dialog";

    private static String NAME_USER_REQUEST;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;
    Button btn_start;
    TextView tv_kq;
    public boolean isRecording = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_kq = findViewById(R.id.txt_1);
        btn_start = findViewById(R.id.btn_1);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED) {
                    if (!isRecording) {
                        isRecording = true;
                        startVoiceRecorder();
                        btn_start.setText("STOP");
                    } else {
                        isRecording = false;
                        stopVoiceRecorder();
                        btn_start.setText("START");
                    }
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.RECORD_AUDIO)) {
                    showPermissionMessageDialog();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO},
                            REQUEST_RECORD_AUDIO_PERMISSION);
                }
            }
        });

    }

    private void showPermissionMessageDialog() {
        MessageDialogFragment
                .newInstance(getString(R.string.permission_message))
                .show(getSupportFragmentManager(), FRAGMENT_MESSAGE_DIALOG);
    }

    private void startVoiceRecorder() {
        //   stopVoiceViettelStream();

        voiceClient.startRecognize();
        //keep screen on while recording
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    @Override
    protected void onStart() {
        try {
            voiceClient = null;
            voiceClient = new VoiceClient(MySharePreferenceVoice.getHost(getApplicationContext()),
                    MySharePreferenceVoice.getPort(getApplicationContext()),
                    MySharePreferenceVoice.isParseJson(getApplicationContext()),
                    false,
                    MySharePreferenceVoice.is16kHz(getApplicationContext()));
            voiceClient.setStopRecordListener(this);
            voiceClient.setChangeAdapterListener(this);
        } catch (Exception e) {
            // showDialog("Host or port is wrong. Please check carefully");
        }
        super.onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
        stopVoiceRecorder();
    }

    private void stopVoiceRecorder() {
        if (voiceClient != null)
            voiceClient.stopRecognize();
    }


    @Override
    public void stop() {

    }

    @Override
    public void recorderStart() {

    }

    @Override
    public void volumeChange(float vol) {

    }

    @Override
    public void change(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("duypq3", "change= " + text);
                tv_kq.setText(text);
                tv_kq.setTextColor(Color.RED);
            }
        });
    }

    @Override
    public void finish(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("duypq3", "finish= " + text);
                tv_kq.setText(text);
                tv_kq.setTextColor(Color.BLACK);

            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                Log.v("duypq3", "onActivityResult changer host port 16kHz=" + MySharePreferenceVoice.is16kHz(getApplicationContext()));
                try {
                    voiceClient = null;
                    voiceClient = new VoiceClient(MySharePreferenceVoice.getHost(getApplicationContext()),
                            MySharePreferenceVoice.getPort(getApplicationContext()),
                            MySharePreferenceVoice.isParseJson(getApplicationContext()),
                            false,
                            MySharePreferenceVoice.is16kHz(getApplicationContext()));
                    voiceClient.setStopRecordListener(this);
                    voiceClient.setChangeAdapterListener(this);
                } catch (Exception e) {
                    // showDialog("Host or port is wrong. Please check carefully");
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

}
