package com.bsrdigicoin.estore1.recyclerView.ordersRecycerView;

public class Orders {

    private String order_name;// it would be product name + " " storename
    private String order_amt;
    private String promocode;
    private String order_status;
    private String final_amt;



    public Orders(String order_name, String order_amt, String promocode, String order_status, String final_amt) {
        this.order_name = order_name;
        this.order_amt = order_amt;
        this.promocode = promocode;
        this.order_status = order_status;
        this.final_amt = final_amt;
    }

    public void setOrder_name(String order_name) {
        this.order_name = order_name;
    }


    public void setOrder_amt(String order_amt) {
        this.order_amt = order_amt;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }

    public void setFinal_amt(String final_amt) {
        this.final_amt = final_amt;
    }

    public String getOrder_name() {
        return order_name;
    }

    public String getOrder_amt() {
        return order_amt;
    }

    public String getPromocode() {
        return promocode;
    }

    public String getFinal_amt() {
        return final_amt;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }
}
