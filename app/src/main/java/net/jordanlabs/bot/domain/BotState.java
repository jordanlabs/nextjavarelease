package net.jordanlabs.bot.domain;

import java.time.LocalDateTime;

public class BotState {
    /*  - last date and time the bot tweeted
        - jdk version the tweet was about
        - release date used, whether it was proposed, estimate or accepted
        - last percentage of progress
     */

    private LocalDateTime lastTweetDateTime;
    private String jdkVersion;
    private ReleaseDate releaseDate;
    private int progressPercentage;
}
