package com.example.androidudpserver;


import android.content.Context;
import java.io.FileOutputStream;

public class WorkFile {
    public byte [] allReceivePacket;
    public int numberFile;
    public int numberPacket;
    public int nuberOfPackets;
    public int sizePacket;
    public byte [] fileByte;
    public IdFile file;

    public class IdFile implements Comparable<IdFile>{
        public int numberFile;
        public IdPacket[] packet;

        public IdFile(int numberFile, IdPacket [] packet){
            this.numberFile = numberFile;
            this.packet = packet;
        }
        public int compareTo(IdFile idFile){
            return new Integer(numberPacket).compareTo(numberFile);
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

    public void setAllReceivePacket(byte [] allReceivePacket){
        this.allReceivePacket = allReceivePacket;
    }

    public IdFile divis(){
        byte [] temp = new byte[4];
        int [] tempArray = new int [4];
        for (int i = 0, j = 0; i < 16; i += 4, j++){
            System.arraycopy(allReceivePacket, i, temp, 0, 4);
            tempArray[j] = ((temp[0] & 0xFF) << 24) + ((temp[1] & 0xFF) << 16) + ((temp[2] & 0xFF) << 8) + (temp[3] & 0xFF);
        }
        numberFile = tempArray[0];
        numberPacket = tempArray[1];
        nuberOfPackets = tempArray[2];
        sizePacket = tempArray[3];
        fileByte = new byte[allReceivePacket.length - 16];
        System.arraycopy(allReceivePacket, 16, fileByte, 0, fileByte.length);
        IdPacket [] idPacket = new IdPacket[nuberOfPackets];
        file = new IdFile(numberFile, idPacket);
        return file;
    }
    public void save(IdFile idFile, Context context){
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
}
