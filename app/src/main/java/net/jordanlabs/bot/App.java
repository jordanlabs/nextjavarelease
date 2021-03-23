package net.jordanlabs.bot;

import com.github.redouane59.twitter.TwitterClient;

import java.io.IOException;

public class App {
    public static void main(String[] args)  {
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
        TwitterClient twitterClient = new TwitterClient();
        final var nextJavaRelease = NextJavaReleaseFactory.createNextJavaRelease();
        try {
            nextJavaRelease.runTwitterBot();
        } catch (IOException ex) {
           System.out.println("Failed");
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
