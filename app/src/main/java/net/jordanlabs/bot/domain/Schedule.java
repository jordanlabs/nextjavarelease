package net.jordanlabs.bot.domain;

public class Schedule {
    private final boolean isProposed;
    private final String scheduleUrl;

    public Schedule(final boolean isProposed, final String scheduleUrl) {
        this.isProposed = isProposed;
        this.scheduleUrl = scheduleUrl;
    }

    public Schedule() {
        this(false, "");
    }

    public boolean isProposed() {
        return isProposed;
    }

    public String scheduleUrl() {
        return scheduleUrl;
    }
}
