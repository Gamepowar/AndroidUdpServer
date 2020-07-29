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
    public int numberOfPackets;
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

        public void createArrayPackets(int numberOfPackets){
            packet = new IdPacket[numberOfPackets];
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
        return ((value[0] & 0xFF) << 24) + ((value[1] & 0xFF) << 16) + ((value[2] & 0xFF) << 8) + (value[3] & 0xFF);
    }

    public void divis(){
        byte [] temp = new byte[4];
        int [] tempArray = new int [4];
        for (int i = 0, j = 0; i < 16; i += 4, j++){
            System.arraycopy(allReceivePacket, i, temp, 0, 4);
            tempArray[j] = byteArrayToInt(temp); //(
            System.out.println(">>>>>>>>>>>>>>>>>" + tempArray[j]);
        }
        numberFile = tempArray[0];
        numberPacket = tempArray[1];
        numberOfPackets = tempArray[2];
        sizePacket = tempArray[3];
        fileByte = new byte[allReceivePacket.length - 16];
        System.arraycopy(allReceivePacket, 16, fileByte, 0, fileByte.length);
       // IdPacket  idPacket =;
        file = new IdFile(numberFile);
        if (idFiles.get(file.numberFile) == null){ // if file not create
          //  file.createArrayPackets();
            idFiles.put(numberFile, file);
            System.out.println("Количество пакетов: " + numberOfPackets);
            idFiles.get(numberFile).createArrayPackets(numberOfPackets);
            idFiles.get(numberFile).packet[numberPacket] =  new IdPacket(numberPacket, fileByte);
            if(isFullFile(file)){
                System.err.println("Файл сохранён: ");
                save(idFiles.get(numberFile));
             //   new MainActivity().save(idFiles.get(numberFile), numberOfPackets);
            }
        }
        else {
            idFiles.get(numberFile).packet[numberPacket] =  new IdPacket(numberPacket, fileByte);
            if(isFullFile(idFiles.get(numberFile))){
                System.err.println("Файл сохранён: ");
                save(idFiles.get(numberFile));
           //     new MainActivity().save(idFiles.get(numberFile), numberOfPackets);
            }
        }
    }

    public void save(IdFile idFile){
        String filename = "video.mov";
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            for (int i = 0; i < numberOfPackets; i++){
                outputStream.write(idFile.packet[i].file);
            }

            System.out.println(outputStream.getChannel());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean isFullFile(IdFile idFile){
        System.out.println("isFullFile: ");
        for(int i = 0; i < numberOfPackets; i++){
            System.out.println("test " + i);
            if(idFile.packet[i] == null) return false;
        }
        return true;
    }

}
