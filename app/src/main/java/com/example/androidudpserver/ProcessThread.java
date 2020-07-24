package com.example.androidudpserver;

import java.util.ArrayDeque;

public class ProcessThread extends Thread {

    ArrayDeque<byte[]> arrayDeque;
    boolean running;

    public ProcessThread(ArrayDeque<byte[]> arrayDeque){
        this.arrayDeque = arrayDeque;
        running = true;
        start();
    }

    public void finish(){
        running = false;
    }

    @Override
    public void run() {
        WorkFile workFile = new WorkFile();
        while(running){

            if(!arrayDeque.isEmpty()){
                workFile.setAllReceivePacket(arrayDeque.removeFirst());
                workFile.createFileList();
                workFile.divis();
                if(workFile.isFullFile(workFile.file)) finish();
            }
        }
    }
}
