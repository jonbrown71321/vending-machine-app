package com.techelevator.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class Logger {
    public Logger(String logType, String logName) {
        this.logType = logType;
        this.logName = logName;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getLogName() {
        return logName;
    }

    public void setTransactionName(String logName) {
        this.logName = logName;
    }

    private String logType;
    private String logName;
    private String logMessage;
    private static final String LOG_PATH = "Log.txt";

    public static void logOperation(Logger logger){
        File logFile = new File(LOG_PATH);
        try(PrintWriter logWriter = new PrintWriter(new FileOutputStream(logFile, true));){
            logWriter.println(logger.getLogMessage());
        }catch (FileNotFoundException e){
            System.out.println("FILE NOT FOUND");
        }
    }

    String getLogMessage() {
        return logMessage;
    }

    public void makeLogMessage() {
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }
}
