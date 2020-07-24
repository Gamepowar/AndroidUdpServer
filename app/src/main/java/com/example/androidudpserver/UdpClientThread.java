package com.example.androidudpserver;
import android.content.Context;
import android.os.Message;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class UdpClientThread extends Thread{

    String dstAddress;
    int dstPort;
    private boolean running;
    MainActivity.UdpClientHandler handler;
    DatagramSocket socket;
    InetAddress address;

    public byte [] buf;

    public UdpClientThread(String addr, int port, MainActivity.UdpClientHandler handler) {
        super();
        dstAddress = addr;
        dstPort = port;
        this.handler = handler;
    }

    public void setRunning(boolean running){
        this.running = running;
    }

    private void sendState(String state){
        handler.sendMessage(
                Message.obtain(handler,
                        MainActivity.UdpClientHandler.UPDATE_STATE, state));
    }
    public byte [] myStart(){
        run();
        return buf;
    }

    @Override
    public void run() {
        sendState("connecting...");
        running = true;
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName(dstAddress);
            buf = new byte[1 * 1024 + 4 * 4];
            System.err.println("test1");
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, dstPort);
            System.out.println(address.toString() + "\n" + dstPort);
            socket.send(packet);
//            sendState("connected");
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
      //      buf = new byte[60*1024];
       //     sendState("...");
            System.err.println("test2");
      //      sendState("create file...");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(socket != null){
                socket.close();
                handler.sendEmptyMessage(MainActivity.UdpClientHandler.UPDATE_END);
            }
        }

    }
}