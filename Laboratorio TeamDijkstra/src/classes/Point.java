/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.util.ArrayList;

/**
 *
 * @author john
 */
public class Point extends Master {

    private final String code;
    private String address;
    private ArrayList<Invoice> sales = new ArrayList<>();
    private ArrayList<Product> products = new ArrayList<>();

    public Point(String address, String code) {
        this.code = code;
        this.address = address;
    }

    //GETTERS SETTERS
    public String getCode() {
        return code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<Invoice> getSales() {
        return sales;
    }

    public void setSales(ArrayList<Invoice> sales) {
        this.sales = sales;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

}
