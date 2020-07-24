package com.example.androidudpserver;


import android.content.Context;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class WorkFile {
    public byte [] allReceivePacket;
    public int numberFile;
    public int numberPacket;
    public int nuberOfPackets;
    public int sizePacket;
    public byte [] fileByte;
    public IdFile file;
    public Map<Integer, IdFile> idFiles;
    Context context;

    public WorkFile(){}

    public WorkFile(byte [] allReceivePacket, Context context){
        this.allReceivePacket = allReceivePacket;
        createFileList();
        this.context = context;
    }

    public class IdFile {
        public int numberFile;
        public IdPacket[] packet;

        public IdFile(int numberFile){
            this.numberFile = numberFile;
        }

        public void createArrayPackets(){
            packet = new IdPacket[nuberOfPackets];
        }
    }

    public class IdPacket{
        public int numberPacket;
        public byte [] file;

        public IdPacket(int numberPacket, byte [] file){
            this.file = file;
            this.numberPacket = numberPacket;
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setAllReceivePacket(byte [] allReceivePacket){
        this.allReceivePacket = allReceivePacket;
    }

    public void createFileList(){
        idFiles = new TreeMap<>();
    }

    public static final int byteArrayToInt(byte[] value) {
        return (value[0] << 24) + (value[1] << 16) + (value[2] << 8) + value[3];
    }

    public void divis(){
        byte [] temp = new byte[4];
        int [] tempArray = new int [4];
        for (int i = 0, j = 0; i < 16; i += 4, j++){
            System.arraycopy(allReceivePacket, i, temp, 0, 4);
            tempArray[j] = byteArrayToInt(temp); //((temp[0] & 0xFF) << 24) + ((temp[1] & 0xFF) << 16) + ((temp[2] & 0xFF) << 8) + (temp[3] & 0xFF);
        }
        numberFile = tempArray[0];
        numberPacket = tempArray[1];
        nuberOfPackets = tempArray[2];
        sizePacket = tempArray[3];
        fileByte = new byte[allReceivePacket.length - 16];
        System.arraycopy(allReceivePacket, 16, fileByte, 0, fileByte.length);
        IdPacket  idPacket = new IdPacket(numberPacket, fileByte);
        file = new IdFile(numberFile);
        if (idFiles.get(file.numberFile) == null){
            idFiles.put(numberFile, file);
            idFiles.get(numberFile).createArrayPackets();
            idFiles.get(numberFile).packet[numberPacket] = idPacket;
            if(isFullFile(file)){
                save(file);
            }
        }
        else {
            idFiles.get(numberFile).packet[numberPacket] = idPacket;
            if(isFullFile(file)){
                save(file);
            }
        }
    }

    public void save(IdFile idFile){
        String filename = "src\\androidTest\\java\\video";
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            for (int i = 0; i < nuberOfPackets; i++){
                outputStream.write(idFile.packet[i].file);
            }
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean isFullFile(IdFile idFile){
        for(int i = 0; i < nuberOfPackets; i++){
            if(idFile.packet[i] == null) return false;
        }
        return true;
    }

}
