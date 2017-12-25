package com.example.maxterminatorx.ramilevyhelpapp;

/**
 * Created by maxterminatorx on 04-Dec-17.
 */

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import serverredweb.Child;

public class ServerConnectorSocket {


    private String serverIp;
    private int serverPort;

    public ServerConnectorSocket(String serverIp , int port) {
        this.serverIp = serverIp;
        this.serverPort = port;
    }



    public void checkSim(String simCode,Handler sender,android.content.SharedPreferences.Editor edit){
        if(sender instanceof EntryActivity.EntryHandler)
            ((EntryActivity.EntryHandler)sender).activateWait();
        Thread runningThread = new Thread(){
            @Override
            public void run(){
                try(Socket client = new Socket(serverIp,serverPort);
                    OutputStream os = client.getOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(os);
                    InputStream is = client.getInputStream();
                    ObjectInputStream ois = new ObjectInputStream(is)){
                    client.setSoTimeout(5000);
                    os.write(ServerCommandsAndKeys.CMD_CHECK_IS_PARENT_EXIST);
                    oos.writeObject(AppMaster.simCode.substring(0,19));
                    int result = is.read();
                    if(result == 1) {
                        synchronized(edit) {
                            edit.putInt("parent_id", result);
                            edit.commit();
                        }
                    }
                    Log.i("maxim",String.valueOf(result));
                    sender.sendEmptyMessage(result);
                }catch(IOException ioex){
                    String msg = ioex.toString();
                    Log.i("Error: ",msg);
                    //Log.i("Error: ",ioex.getMessage());
                    if(msg.contains("(Connection refused)"))
                        sender.sendEmptyMessage(-1);
                    else
                        sender.sendEmptyMessage(-2);
                }
            }
        };

        runningThread.start();

    }

    public void sendNewUser(String simCode,Handler sender){
        if(sender instanceof EntryActivity.EntryHandler)
            ((EntryActivity.EntryHandler)sender).activateWait();
        new Thread(){

            @Override
            public void run(){
                try(Socket client = new Socket(serverIp,serverPort);
                    ObjectOutputStream os = new ObjectOutputStream(client.getOutputStream());
                    ObjectInputStream is = new ObjectInputStream(client.getInputStream())){
                      client.setSoTimeout(5000);
                      os.writeByte(ServerCommandsAndKeys.CMD_ADD_NEW_PARENT);
                      os.writeObject(AppMaster.simCode.substring(0,19));
                      sender.sendEmptyMessage(is.readByte());

                  }catch(IOException ioex){
                        String msg = ioex.toString();
                        Log.i("Error: ",ioex.toString());
                        if(msg.contains("(Connection refused)"))
                            sender.sendEmptyMessage(-1);
                        else
                            sender.sendEmptyMessage(-2);
                  }
            }

        }.start();

    }


    public void checkChildExist(String childHash,Handler sender){

        final String finalChildHash = childHash.toLowerCase();


        new Thread(){

            @Override
            public void run(){
                try(Socket client = new Socket(serverIp,serverPort);
                    OutputStream os = client.getOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(os);
                    InputStream is = client.getInputStream();
                    ObjectInputStream ois = new ObjectInputStream(is)){
                    client.setSoTimeout(5000);
                    os.write(ServerCommandsAndKeys.CMD_CHECK_IS_CHILD_EXIST);
                    oos.writeObject(childHash);


                    int result = is.read();
                    Log.i("result",String.valueOf(result));
                    sender.sendEmptyMessage(result);

                }catch(IOException ioex){
                    String msg = ioex.toString();
                    Log.i("Error: ",ioex.toString());
                    if(msg.contains("(Connection refused)"))
                        sender.sendEmptyMessage(-1);
                    else
                        sender.sendEmptyMessage(-2);
                }
            }

        }.start();
    }

    public void addChild(String name,Handler sender){
        new Thread(){
            @Override
            public void run(){
                try(Socket client = new Socket(serverIp,serverPort);
                    ObjectOutputStream os = new ObjectOutputStream(client.getOutputStream());
                    ObjectInputStream is = new ObjectInputStream(client.getInputStream())){

                    os.writeByte(ServerCommandsAndKeys.CMD_ADD_NEW_CHILD);
                    os.writeObject(AppMaster.simCode);
                    os.writeObject(name);


                    Child c = (Child)is.readObject();
                    //sender.sendEmptyMessage();


                }catch(IOException ioex){

                }catch(ClassNotFoundException cnfex){

                }
            }
        }.start();
    }


}
