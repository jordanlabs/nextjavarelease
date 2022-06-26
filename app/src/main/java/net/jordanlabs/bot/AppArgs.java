package net.jordanlabs.bot;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class AppArgs {
    @Parameter(names = {"--dry-run", "-n"})
    boolean dryRun;

    private AppArgs() {
    }

    public boolean isDryRun() {
        return dryRun;
    }

    public static AppArgs parseArgs(final String... args) {
        final AppArgs appArgs = new AppArgs();
        JCommander.newBuilder()
            .addObject(appArgs)
            .build()
            .parse(args);
        return appArgs;
    }
}
