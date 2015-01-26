package chat;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadDemo {

    static class Zaehlen implements Runnable {

        @Override
        public void run() {

            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + " zählt: " + i);
                try {
                    Thread.sleep(6);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ThreadDemo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

    }

    public static void main(String[] args) {

        try {
            System.out.println("Start");

            ExecutorService executor = Executors.newFixedThreadPool(2);
            Thread t1 = new Thread(new Zaehlen());
            Thread t2 = new Thread(new Zaehlen());

            // Threads benennen
            t1.setName("Erster");
            t2.setName("Zweiter");

            // Threads starten
            //t1.start();
            //t2.start();

            executor.execute(t1);
            executor.execute(t2);
            executor.shutdown();
            
            // auf Threads warten
            //t1.join();
            //t2.join();
            while(!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                System.out.println("Das zählen läuft noch.");
            }
            
            System.out.println("Ende");
        } catch (InterruptedException ex) {
            Logger.getLogger(ThreadDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
