package net.jordanlabs.bot.provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

@Singleton
public class PropertiesLoader {
    private static final String PROPERTIES_DIR = "conf";
    private static final String DEFAULT_PROPERTIES_FILE = "application.properties";
    private static final Logger logger = LogManager.getLogger(PropertiesLoader.class);

    private final Properties emptyProperties = new Properties();

    @Inject
    public PropertiesLoader() {
    }

    public Properties loadProperties() {
        final String workingDirectory = System.getProperty("user.dir");
        final Path propertiesFile = Path.of(workingDirectory, PROPERTIES_DIR, DEFAULT_PROPERTIES_FILE);
        logger.info("Loading properties file: {}", propertiesFile);

        try (InputStream input = Files.newInputStream(propertiesFile)) {
            final Properties properties = new Properties();
            properties.load(input);
            return properties;
        } catch (IOException exception) {
            logger.error("Failed to load properties file {}", propertiesFile, exception);
        }
        return emptyProperties;
    }
}
