package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    public static void main(String[] args) {
        while (true) {
            try {
                System.out.println("Server wird gestartet...");
                ServerSocket server = new ServerSocket(5678);
                System.out.println("Server wurde gestartet!");

                System.out.println("Warte auf Client...");
                Socket client = server.accept();
                System.out.println("Client verbunden!");

                Thread t1 = new Thread(new Handler(client));
                t1.start();
                
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
