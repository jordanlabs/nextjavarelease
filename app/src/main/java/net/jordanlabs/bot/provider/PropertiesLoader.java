package net.jordanlabs.bot.provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Singleton
public class PropertiesLoader {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    public PropertiesLoader() {
    }

    public Properties loadProperties(final String propertiesFile) {
        try (InputStream input = PropertiesLoader.class.getClassLoader().getResourceAsStream(propertiesFile)) {
            final Properties properties = new Properties();
            properties.load(input);
            return properties;
        } catch (IOException exception) {
            logger.error("Failed to load properties file {}", propertiesFile, exception);
        }
        return null;
    }
}
