package com.bsrdigicoin.estore1.recyclerView.shopsRecyclerView;

public class Shop {

    private String name = "";
    private String cat = "";

    public Shop(String name, String cat) {
        this.name = name;
        this.cat = cat;
    }

    public String getName() {
        return name;
    }

    public String getCat() {
        return cat;
    }
    public String toString() {
        return this.name + " " + this.cat;
    }
}
