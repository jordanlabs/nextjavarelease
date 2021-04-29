package net.jordanlabs.bot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class App {
    private static final Logger log = LogManager.getLogger(App.class);

    public static void main(String[] args)  {
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());

        final var nextJavaRelease = NextJavaReleaseFactory.createNextJavaRelease();
        try {
            nextJavaRelease.runTwitterBot();
        } catch (IOException ex) {
           log.error("Failed to post tweet", ex);
        }
    }

    public static class ShutdownHook extends Thread {
        @Override
        public void run() {
            // stop any running threads
            // write down current state
            // release any resources
        }
    }
}
