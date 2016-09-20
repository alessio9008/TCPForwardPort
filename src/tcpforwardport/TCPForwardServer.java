/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpforwardport;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * *
 * TCPForwardServer is a simple TCP bridging software that * allows a TCP port
 * on some host to be transparently forwarded * to some other TCP port on some
 * other host. TCPForwardServer * continuously accepts client connections on the
 * listening TCP * port (source port) and starts a thread (ClientThread) that *
 * connects to the destination host and starts forwarding the * data between the
 * client socket and destination socket. *
 */
public class TCPForwardServer {


    public static void main(String[] args) {
        Utility utility=Utility.getInstanceUtility(new File(System.getProperty("connection")));
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(Integer.parseInt(utility.getProperty(KeysConnection.SOURCE_PORT.getValue())));
        } catch (IOException ex) {
            System.out.println("impossibile aprire la serversocket in " + utility.getProperty(KeysConnection.SOURCE_PORT.getValue()));
        }

        while (serverSocket != null) {

            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException ex) {
                System.out.println("Errore per accettare i client nella porta " + utility.getProperty(KeysConnection.SOURCE_PORT.getValue()));
            }
            if (clientSocket != null) {
                utility.getThreadExecutor().execute(new ClientThread(clientSocket));
            }
        }

    }
}
