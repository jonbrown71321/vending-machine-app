package com.techelevator;

import com.techelevator.view.Item;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class VendingMachineTest {
    private VendingMachine test;

    @Before
    public void setUp(){
        test = new VendingMachine("VendoMatic" , "800");
    }

    @Test
    public void parseInventoryTest() throws FileNotFoundException {
        Map<String, Item> expectedInventory = new HashMap<>();

        expectedInventory.put("B4", new Item("B4", "Crunchie", 1.75, "Candy"));
        test.parseInventory();
        Map<String, Item> actualInventory = test.inventory;

        Item expectedItemB4 = expectedInventory.get("B4");
        Item actualItemB4 = expectedInventory.get("B4");

        Assert.assertEquals(expectedItemB4.getSlotId(), actualItemB4.getSlotId());
        Assert.assertEquals(expectedItemB4.getName(), actualItemB4.getName());
        Assert.assertEquals(expectedItemB4.getPrice(), actualItemB4.getPrice(), 0.0001);
        Assert.assertEquals(expectedItemB4.getCategory(), actualItemB4.getCategory());
    }

    @Test
    public void finishTransactionTest(){
        test.finishTransaction(12);
        double actual = test.getBalance();
        double expected = 0;
        Assert.assertEquals(expected,actual, 0);
    }

    @Test
    public void testFeedMoneyValidInput() {
        double initialBalance = test.getBalance();

        String input = "5\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        test.feedMoney(new Scanner(System.in));

        double actualBalance = test.getBalance();
        double expectedBalance = initialBalance + 5.0;

        Assert.assertEquals(expectedBalance, actualBalance, 0.01);
    }

    @Test
    public void testFeedMoneyInvalidInput() {
        double initialBalance = test.getBalance();

        String input = "7\n5\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        test.feedMoney(new Scanner(System.in));

        double actualBalance = test.getBalance();
        double expectedBalance = initialBalance + 5.0;

        Assert.assertEquals(expectedBalance, actualBalance, 0.01);
    }


    @Test
    public void testFeedMoneyInvalidInputBeingNegative() {
        double initialBalance = test.getBalance();

        String input = "-7\n5\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        test.feedMoney(new Scanner(System.in));

        double actualBalance = test.getBalance();
        double expectedBalance = initialBalance + 5.0;

        Assert.assertEquals(expectedBalance, actualBalance, 0.01);
    }


    @Test
    public void testSelectingAnItem() throws FileNotFoundException {
        test.parseInventory();
        String input = "5\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        test.feedMoney(new Scanner(System.in));
        test.selectItem("B3");
        double expectedBalance = 3.50;
        Assert.assertEquals(expectedBalance,test.getBalance(), 0);

    }

    @Test
    public void testSelectingAnItemWithInvalidSelection() throws FileNotFoundException {
        test.parseInventory();
        String input = "5\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        test.feedMoney(new Scanner(System.in));
        test.selectItem("B9");
        double expectedBalance = 5.00;
        Assert.assertEquals(expectedBalance,test.getBalance(), 0);

    }

    @Test
    public void testSelectingAnItemWithInvalidBalance() throws FileNotFoundException {
        test.parseInventory();
        String input = "1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        test.feedMoney(new Scanner(System.in));
        test.selectItem("B3");
        double expectedBalance = 1.00;
        Assert.assertEquals(expectedBalance,test.getBalance(), 0);

    }



}