package com.techelevator.view;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionalLogger extends Logger{
    private LocalDateTime logDateTime;
    private double endingBalance;
    private Item item;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss a");
    private NumberFormat currFormat = NumberFormat.getCurrencyInstance();
    private String dateString;

    public TransactionalLogger(String logType, String logName, Item item, double endingBalance){
        this(logType, logName, item);
        this.endingBalance = endingBalance;
    }
    public TransactionalLogger(String logType, String logName, Item item){
        this(logType, logName);
        this.item = item;
    }

    public TransactionalLogger(String logType, String logName) {
        super(logType, logName);
    }


    public String getLogMessage(){
        return super.getLogMessage();
    }

    @Override
    public void makeLogMessage() {
        logDateTime = LocalDateTime.now();
        dateString = formatter.format(logDateTime);
        super.setLogMessage(dateString + " " + item.getName() + " " + item.getSlotId() +  " " +
                currFormat.format(item.getPrice()) + " " + currFormat.format(endingBalance));
    }

}
