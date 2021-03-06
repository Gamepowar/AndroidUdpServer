package com.example.androidudpserver;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.util.ArrayDeque;

public class MainActivity extends AppCompatActivity {

    EditText editTextAddress, editTextPort;
    Button buttonConnect;
    TextView textViewState, textViewRx;

    VideoView videoView;

    UdpClientHandler udpClientHandler;
    UdpClientThread udpClientThread;
  /*  public void save(WorkFile.IdFile idFile, int numberOfPackets){
        String filename = "video.mov";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, MODE_PRIVATE);
            for (int i = 0; i < numberOfPackets; i++){
                outputStream.write(idFile.packet[i].file);
            }

            System.out.println(outputStream.getChannel());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextAddress = (EditText) findViewById(R.id.address);
        editTextPort = (EditText) findViewById(R.id.port);
        buttonConnect = (Button) findViewById(R.id.connect);
    //    textViewState = (TextView)findViewById(R.id.state);
        textViewRx = (TextView)findViewById(R.id.received);

        buttonConnect.setOnClickListener(buttonConnectOnClickListener);

        udpClientHandler = new UdpClientHandler(this);
    }

    View.OnClickListener buttonConnectOnClickListener =
            new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    udpClientThread = new UdpClientThread(
                            editTextAddress.getText().toString(),
                            Integer.parseInt(editTextPort.getText().toString()),
                            udpClientHandler, getApplicationContext().getApplicationContext());

                    System.err.println(editTextAddress.getText().toString()+"\n"+ editTextPort.getText().toString());
              //      ArrayDeque<byte[]> arrayDeque = new ArrayDeque<>();
             //       ProcessThread processThread = new ProcessThread(arrayDeque);
                 //   System.err.println("2");
                  //  while(processThread.running) {
               //         System.err.println("3");

                      /*  arrayDeque.addLast*/udpClientThread.start();
                      while (udpClientThread.isAlive()){}
                    setContentView(R.layout.show_video);
                    videoView = (VideoView)findViewById(R.id.videoView);

                    /*videoView.setVideoURI(Uri.fromFile(getFileStreamPath("video.mp4")));*/
                    videoView.setVideoURI(Uri.parse("video/screen.mp4"));

              //          System.err.println("4");
                   // }


                    buttonConnect.setEnabled(false);
                }
            };

  /*  private void updateState(String state){
        textViewState.setText(state);
    }

    private void updateRxMsg(String rxmsg){
        textViewRx.append(rxmsg + "\n");
    }*/

    private void clientEnd(){
        udpClientThread = null;
        textViewState.setText("clientEnd()");
        buttonConnect.setEnabled(true);

    }

    public static class UdpClientHandler extends Handler {
        public static final int UPDATE_STATE = 0;
        public static final int UPDATE_MSG = 1;
        public static final int UPDATE_END = 2;
        private MainActivity parent;

        public UdpClientHandler(MainActivity parent) {
            super();
            this.parent = parent;
        }
/*
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case UPDATE_STATE:
                    parent.updateState((String)msg.obj);
                    break;
                case UPDATE_MSG:
                    parent.updateRxMsg((String)msg.obj);
                    break;
                case UPDATE_END:
                    parent.clientEnd();
                    break;
                default:
                    super.handleMessage(msg);
            }

        }*/
    }
}
