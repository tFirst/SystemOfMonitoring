package com.systemofmonitoring.connecttoserver;

import org.json.*;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class ConnectWithServer {

    public JSONObject SendMessage(JSONObject jsonObject) throws JSONException {
        String line = null;
        int serverPort = 3333; // port
        String address = "192.168.1.6"; // server's ip-address
        try {
            InetAddress ipAddress = InetAddress.getByName(address);
            System.out.println("Any of you heard of a socket with IP address " + address + " and port " + serverPort + "?");
            Socket socket = new Socket(ipAddress, serverPort);
            System.out.println("Yes! I just got hold of the program.");

            InputStream inputStream = socket.getInputStream();
            System.out.println("Started input stream...");
            OutputStream outputStream = socket.getOutputStream();
            System.out.println("Started output stream...");

            DataInputStream dataInputStream = new DataInputStream(inputStream);
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            System.out.println("Type in something and press enter. Will send it to the server and tell ya what it thinks.");
            System.out.println();

            while (true) {
                System.out.println("Sending this line to the server...");
                System.out.println(jsonObject.toString());
                dataOutputStream.writeUTF(jsonObject.toString());
                dataOutputStream.flush();

                line = dataInputStream.readUTF();

                System.out.println("The server was very polite. It sent me this: " + line);
                System.out.println("Looks like the server is pleased with us. Go ahead and enter more lines.");
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject(line);
    }
}
