package com.techelevator;

import com.techelevator.view.Item;
import com.techelevator.view.Menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT };

	private static final String PURCHASE_MENU_OPTION_FEED_MONEY = "Feed Money";
	private static final String PURCHASE_MENU_OPTION_SELECT_PRODUCT = "Select Product";
	private static final String PURCHASE_MENU_OPTION_FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] PURCHASE_MENU_OPTIONS = {PURCHASE_MENU_OPTION_FEED_MONEY, PURCHASE_MENU_OPTION_SELECT_PRODUCT, PURCHASE_MENU_OPTION_FINISH_TRANSACTION};
	private Menu menu;
	private Scanner in;

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
		this.in = new Scanner(System.in);
	}

	public void run() throws FileNotFoundException {
		VendingMachine machine = new VendingMachine("Vendo-Matic", "800");
		machine.parseInventory();
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				// display vending machine items
				machine.displayItems("main");
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				// do purchase
				displayPurchaseMenu(machine);
			} else if(choice.equals(MAIN_MENU_OPTION_EXIT)){
				System.exit(0);
			}
		}
	}

	private void displayPurchaseMenu(VendingMachine machine) {
		while(true){
			String choice = (String) menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);
			switch(choice){
				case PURCHASE_MENU_OPTION_FEED_MONEY:
					// feed money into the vending machine
                     machine.feedMoney(in);
					break;
				case PURCHASE_MENU_OPTION_SELECT_PRODUCT:
					//select a product from list of items
					System.out.println("Choose from one of the following options below: ");
					machine.displayItems("purchase");
					System.out.print("Enter your selection here: ");
					String itemSelection = in.nextLine().trim();
					if(machine.getBalance() > 0){
						machine.selectItem(itemSelection);
					} else {
						System.out.println("Your balance must be greater than 0");
						System.out.println("Add more money to continue");
					}

					break;
				case PURCHASE_MENU_OPTION_FINISH_TRANSACTION:
					//finishes interaction with vending machine
					machine.finishTransaction(machine.getBalance());
					return;
				default:
					throw new IllegalArgumentException("A valid choice was not selected.");

			}
		}
	}



	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		try {
			cli.run();
		} catch(FileNotFoundException e){
			System.out.println("FILE NOT FOUND: " + e.getMessage());
		}
	}
}
