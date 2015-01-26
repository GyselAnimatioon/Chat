package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

    public static void main(String[] args) {
        
        Scanner eingabe = new Scanner(System.in);
        
        try {

            System.out.println("Client wird gestartet...");
            Socket client = new Socket("192.168.1.47", 5678);
            System.out.println("Client wurde gestartet!");

            // Streams
            OutputStream out = client.getOutputStream();
            PrintWriter writer = new PrintWriter(out);
            InputStream in = client.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            
            System.out.print("Eingabe: ");
            String anServer = eingabe.nextLine();
            
            writer.write(anServer + "\n");
            writer.flush();

            String s = null;

            while ((s = reader.readLine()) != null) {
                System.out.println("Empfangen vom Server: " + s);
            }

            writer.close();
            reader.close();

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
