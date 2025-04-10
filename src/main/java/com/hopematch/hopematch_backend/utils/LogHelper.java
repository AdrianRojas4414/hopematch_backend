package com.hopematch.hopematch_backend.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogHelper {
    private static final Logger logger = LoggerFactory.getLogger(LogHelper.class);

    public static void info(String msg) {
        logger.info(msg);
    }

    public static void warn(String msg) {
        logger.warn(msg);
    }

    public static void error(String msg) {
        logger.error(msg);
    }

    public static void debug(String msg) {
        logger.debug(msg);
    }

}
