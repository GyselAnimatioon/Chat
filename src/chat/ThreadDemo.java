package chat;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadDemo {

    static class Zaehlen implements Runnable {

        @Override
        public void run() {

            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + "Zählen: " + i);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ThreadDemo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

    }

    public static void main(String[] args) {

        try {
            System.out.println("Start");
            Thread t1 = new Thread(new Zaehlen());
            Thread t2 = new Thread(new Zaehlen());
            
            
            // Threads benennen
            t1.setName("Erster");
            t2.setName("Zweiter");
            
            // Threads starten
            t1.start();
            t2.start();
            
            // auf Threads warten
            t1.join();
            t2.join();
            
            System.out.println("Ende");
        } catch (InterruptedException ex) {
            Logger.getLogger(ThreadDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
