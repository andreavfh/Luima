package io.github.andreavfh.lumia.utils;

import java.util.logging.Logger;

public class LoggerWrapper {

    private final Logger logger;
    private String prefix;

    public LoggerWrapper(Logger logger, String prefix) {
        this.logger = logger;
        this.prefix = translateColors(prefix);
    }

    public void setPrefix(String prefix) {
        this.prefix = translateColors(prefix);
    }

    public void info(String message) {
        logger.info( message);
    }

    public void warning(String message) {
        logger.warning( message);
    }


    public void warningRaw(String message) {
        logger.warning(message);
    }


    private String translateColors(String input) {
        return input == null ? "" : input.replace("&", "§");
    }
}
