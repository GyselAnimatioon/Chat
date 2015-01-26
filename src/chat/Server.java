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

        try {
            System.out.println("Server wird gestartet...");
            ServerSocket server = new ServerSocket(5678);
            System.out.println("Server wurde gestartet!");

            System.out.println("Warte auf Client...");
            Socket client = server.accept();
            System.out.println("Client verbunden!");
            
            // Streams
            OutputStream out = client.getOutputStream();
            PrintWriter writer = new PrintWriter(out);
            InputStream in = client.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String s = null;

            while ((s = reader.readLine()) != null) {
                writer.write(s + "\n");
                writer.flush();
                System.out.println("Empfangen vom Client: " + s);
            }

            writer.close();
            reader.close();

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
