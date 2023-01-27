package com.example.doannhom11;

public class Drinks {
    private String name,size,topping;
    private int ban;

    public Drinks(String name, String size, String topping, int ban) {
        this.name = name;
        this.size = size;
        this.topping = topping;
        this.ban = ban;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public String getTopping() {
        return topping;
    }
    public void setTopping(String topping) {
        this.topping = topping;
    }
    public int getBan() {
        return ban;
    }
    public void setBan(int ban) {
        this.ban = ban;
    }
}
