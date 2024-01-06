package com.techelevator.view;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OperationalLogger extends Logger{
    private double endingBalance;
    private double beginningBalance;
    private LocalDateTime logDateTime;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss a");
    private NumberFormat currFormat = NumberFormat.getCurrencyInstance();
    private String dateString;

    public OperationalLogger(String logType, String logName, double beginningBalance, double endingBalance){
        this(logType, logName);
        this.beginningBalance = beginningBalance;
        this.endingBalance = endingBalance;
    }

    public OperationalLogger(String logType, String logName) {
        super(logType, logName);
    }

    @Override
    public void makeLogMessage() {
        logDateTime = LocalDateTime.now();
        dateString = formatter.format(logDateTime);
        super.setLogMessage(dateString + " " + this.getLogName() + ": " + currFormat.format(beginningBalance) + " " + currFormat.format(endingBalance));
    }

}
