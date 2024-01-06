package com.techelevator.view;
public class Item {
    private String slotId;
    private String name;
    private double price;
    private String category;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private int quantity;


    public Item(String slotId, String name, double price, String category) {
        this.slotId = slotId;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Item(String[] itemInfo){
        this(itemInfo[0], itemInfo[1], Double.parseDouble(itemInfo[2]), itemInfo[3]);
    }

    public String getSlotId() {
        return slotId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }
}
