package com.techelevator;

import com.techelevator.view.Item;
import com.techelevator.view.Logger;
import com.techelevator.view.OperationalLogger;
import com.techelevator.view.TransactionalLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class VendingMachine {
    private String name;
    private String model;

    public double getBalance() {
        return balance;
    }

    private void setBalance(double balance) {
        this.balance = balance;
    }

    private double balance;
    private NumberFormat currFormat = NumberFormat.getCurrencyInstance();
    Map<String, Item> inventory = new HashMap<>();

    private static final String DEFAULT_INVENTORY_FILE = "vendingmachine.csv";
    private Logger logger;

    public VendingMachine(String name, String model) {
        this.name = name;
        this.model = model;
        balance = 0;
    }

    public void parseInventory() throws FileNotFoundException {
        File inventoryFile = new File(DEFAULT_INVENTORY_FILE);
        Scanner inventoryScanner = new Scanner(inventoryFile);
        int slotIDIndex = 0;

        while (inventoryScanner.hasNextLine()){
            String inventoryLine = inventoryScanner.nextLine();
            String[] itemInfo = inventoryLine.split("\\|");
            Item item = new Item(itemInfo);
            item.setQuantity(5);
            inventory.put(itemInfo[slotIDIndex], item);
        }

    }

    public void displayItems(String menuType){
        displayHeader(menuType);
        for(Map.Entry<String, Item> entry: inventory.entrySet()){

            String quantityString = "";
            if(entry.getValue().getQuantity() == 0){
                quantityString = "SOLD OUT";
            } else {
                quantityString = String.valueOf(entry.getValue().getQuantity());
            }
            if(menuType.equalsIgnoreCase("main")) {
                System.out.printf("%5s %17s %25s %11s\n", entry.getKey(), currFormat.format(entry.getValue().getPrice()), entry.getValue().getName(), quantityString);
            } else if (menuType.equalsIgnoreCase("purchase")){
                System.out.printf("%s %20s %10s\n", entry.getKey(), entry.getValue().getName(), currFormat.format(entry.getValue().getPrice()));
            }
        }
    }private void displayHeader(String menuType) {

        switch (menuType.toLowerCase()){
            case "main":
                System.out.format("%s %15s %20s %20s\n", "Slot ID", "Price", "Name", "Quantity");
                break;
            case "purchase":
                System.out.printf("%s %13s %15s\n", "Slot", "Item", "Price");
                break;
            default:
                System.out.println();
        }
    }

    public void giveChange (double amountToReturn){
        int quarter = 0;
        int dime = 0;
        int nickle = 0;
        int penny = 0;


        double cents = Math.round(amountToReturn * 100);

        while(cents -25 >= 0){
            cents = cents - 25;
            quarter++;
        }
        while(cents - 10 >= 0){
            cents = cents - 10;
            dime++;
        }
        while(cents - 5 >= 0){
            cents = cents - 5;
            nickle++;
        }
        while(cents - 5 >= 0){
            cents = cents - 5;
            nickle++;
        }
        while(cents - 1 >= 0){
            cents = cents - 1;
            penny++;
        }

        System.out.println("Quarters:" + quarter + " Dimes:" + dime + " Nickles:" + nickle + " Pennies:" + penny);
        System.out.format("Total change returned: %s", currFormat.format(amountToReturn));

    }

    public void selectItem(String slotID){
        if(isItemExists(slotID)){
            Item vendingMachineItem = inventory.get(slotID);
            if(isItemEmpty(vendingMachineItem)){
                System.out.println("SOLD OUT");
                return;
            }
            if (vendingMachineItem.getPrice()>getBalance()){
                System.out.println("Not enough money to purchase: " + vendingMachineItem.getName());
                System.out.println("Please add more money to your balance.");
                return;
            };

                // Log transaction
               double endingBalance = getBalance() - vendingMachineItem.getPrice();
               logger = new TransactionalLogger("Transactional", "ITEM SELECTION", vendingMachineItem, endingBalance);
               logger.makeLogMessage();
               Logger.logOperation(logger);

                // deduct from balance
            deductFromBalance(vendingMachineItem.getPrice());

            //Dispense product with message
            System.out.println(vendingMachineItem.getName() + " " + currFormat.format(vendingMachineItem.getPrice()) + " " +
                    currFormat.format(balance));
            vendingMachineItem.setQuantity(vendingMachineItem.getQuantity() - 1);
            System.out.println(dispensingMessage(vendingMachineItem.getCategory()));
        } else {
            System.out.println("ITEM NOT FOUND");
            return;
        }
    }

    private boolean isItemEmpty(Item vendingMachineItem) {
        return this.isItemEmpty(vendingMachineItem.getSlotId());
    }

    private boolean isItemExists(String slotID){
        return inventory.containsKey(slotID);
    }

    private boolean isItemEmpty(String slotID){
        Item item = inventory.get(slotID);
        return item.getQuantity() <= 0;
    }
    private String dispensingMessage(String type){
        if(type.equals("Chip")){
            return "Crunch Crunch, Yum!";
        }
        else if(type.equals("Candy")){
            return "Munch Munch, Yum!";
        }
        else if(type.equals("Drink")){
            return "Glug Glug, Yum!";
        }
        else if(type.equals("Gum")){
            return "Chew Chew, Yum!";
        }
        else{
            return "Type does not match.";
        }
    }

    public void feedMoney(Scanner in){
        double currentBalance = balance;
        System.out.println("Please feed $1, $5, $10, $20 in the machine: ");
        double moneyFed = in.nextDouble();
        while(!(moneyFed == 1) && !(moneyFed == 5) && !(moneyFed == 10) && !(moneyFed == 20)){
            System.out.println("Incorrect bill given. Please feed $1, $5, $10, $20 in the machine: ");
            moneyFed = in.nextDouble();
        }
        balance += moneyFed;
        System.out.println("Your current balance is: " + currFormat.format(balance));
        setBalance(balance);
        logger = new OperationalLogger("Operational", "FEED MONEY", currentBalance, getBalance());
        logger.makeLogMessage();
        Logger.logOperation(logger);
        in.nextLine();
    }





    private void deductFromBalance(double moneyProvided){
        this.balance -= moneyProvided;
    }

    public void finishTransaction(double remainingBalance){
        giveChange(remainingBalance);
        setBalance(0);

        //Log Operation
        logger = new OperationalLogger("Operational", "GIVE CHANGE", remainingBalance, getBalance());
        logger.makeLogMessage();
        Logger.logOperation(logger);


    }

}
