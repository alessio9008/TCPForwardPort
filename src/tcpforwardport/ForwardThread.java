/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tcpforwardport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/** 

 * ForwardThread handles the TCP forwarding between a socket 

 * input stream (source) and a socket output stream (dest). 

 * It reads the input stream and forwards everything to the 

 * output stream. If some of the streams fails, the forwarding 

 * stops and the parent is notified to close all its sockets. 

 */ 

public class ForwardThread implements Runnable { 
 
    private InputStream mInputStream; 

    private OutputStream mOutputStream; 

    private ClientThread mParent; 

 

    /** 

     * Creates a new traffic redirection thread specifying 

     * its parent, input stream and output stream. 

     */ 

    public ForwardThread(ClientThread aParent, InputStream aInputStream, OutputStream aOutputStream) { 
        mParent = aParent; 

        mInputStream = aInputStream; 

        mOutputStream = aOutputStream; 

    } 

 

    /** 

     * Runs the thread. Continuously reads the input stream and 

     * writes the read data to the output stream. If reading or 

     * writing fail, exits the thread and notifies the parent 

     * about the failure. 

     */ 

    public void run() {
        Utility utility=Utility.getInstanceUtility();

        byte[] buffer = new byte[Integer.parseInt(utility.getProperty(KeysConnection.BUFFER_SIZE.getValue()))]; 

        try { 

            while (true) { 

                int bytesRead = mInputStream.read(buffer); 

                if (bytesRead == -1) 

                    break; // End of stream is reached --> exit 

                mOutputStream.write(buffer, 0, bytesRead); 

                mOutputStream.flush(); 

            } 

        } catch (IOException e) { 

            // Read/write failed --> connection is broken 

        } 

 

        // Notify parent thread that the connection is broken 

        mParent.connectionBroken(); 

    } 

}