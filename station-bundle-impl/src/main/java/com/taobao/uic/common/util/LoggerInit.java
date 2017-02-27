package com.taobao.uic.common.util;

import org.apache.log4j.Logger;

public class LoggerInit {


    static public final Logger LOGGER = Logger.getLogger(LoggerInit.class);
    
    static private final String LOGGER_CACHE_NAME = "com.taobao.uic.common";
    
    static public  Logger LOGGER_CACHE = Logger.getLogger(LOGGER_CACHE_NAME);


}
