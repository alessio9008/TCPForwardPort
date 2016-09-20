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
import java.net.Socket;

/** 

 * ClientThread is responsible for starting forwarding between 

 * the client and the server. It keeps track of the client and 

 * servers sockets that are both closed on input/output error 

 * durinf the forwarding. The forwarding is bidirectional and 

 * is performed by two ForwardThread instances. 

 */ 

public class ClientThread implements Runnable{ 

    private Socket mClientSocket; 

    private Socket mServerSocket; 

    private boolean mForwardingActive = false; 

 

    public ClientThread(Socket aClientSocket) { 

        mClientSocket = aClientSocket; 

    } 

 

    /** 

     * Establishes connection to the destination server and 

     * starts bidirectional forwarding ot data between the 

     * client and the server. 

     */ 

    public void run() {
        Utility utility=Utility.getInstanceUtility();

        InputStream clientIn; 

        OutputStream clientOut; 

        InputStream serverIn; 

        OutputStream serverOut; 

        try { 

            // Connect to the destination server 

            mServerSocket = new Socket(utility.getProperty(KeysConnection.DESTINATION_HOST.getValue()), Integer.parseInt(utility.getProperty(KeysConnection.DESTINATION_PORT.getValue()))); 

 

            // Turn on keep-alive for both the sockets 

            mServerSocket.setKeepAlive(true); 

            mClientSocket.setKeepAlive(true); 

 

            // Obtain client & server input & output streams 

            clientIn = mClientSocket.getInputStream(); 

            clientOut = mClientSocket.getOutputStream(); 

            serverIn = mServerSocket.getInputStream(); 

            serverOut = mServerSocket.getOutputStream(); 

        } catch (IOException ioe) { 

            System.err.println("Can not connect to " + utility.getProperty(KeysConnection.DESTINATION_HOST.getValue()) + ":" + utility.getProperty(KeysConnection.DESTINATION_PORT.getValue())); 

            connectionBroken(); 

            return; 

        } 

 

        // Start forwarding data between server and client 

        mForwardingActive = true; 
        
        //clientForward
        utility.getThreadExecutor().execute(new ForwardThread(this, clientIn, serverOut));

        //ServerForward
        utility.getThreadExecutor().execute(new ForwardThread(this, serverIn, clientOut));

        System.out.println("TCP Forwarding " + mClientSocket.getInetAddress().getHostAddress() + ":" + mClientSocket.getPort() + " <--> " + mServerSocket.getInetAddress().getHostAddress() + ":" + mServerSocket.getPort() + " started."); 

    } 

 

    /** 

     * Called by some of the forwarding threads to indicate 

     * that its socket connection is brokean and both client 

     * and server sockets should be closed. Closing the client 

     * and server sockets causes all threads blocked on reading 

     * or writing to these sockets to get an exception and to 

     * finish their execution. 

     */ 

    public synchronized void connectionBroken() { 

        try { 

            mServerSocket.close(); 

        } catch (Exception e) {} 

        try { 

            mClientSocket.close(); } 

        catch (Exception e) {} 

  

        if (mForwardingActive) { 

            System.out.println("TCP Forwarding " + mClientSocket.getInetAddress().getHostAddress() + ":" + mClientSocket.getPort() + " <--> " + mServerSocket.getInetAddress().getHostAddress() + ":" + mServerSocket.getPort() + " stopped."); 

            mForwardingActive = false; 

        } 

    } 

} 
