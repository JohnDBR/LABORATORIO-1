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
public class Invoice extends Master {

    private final String code;
    private ArrayList<Product> products;
    private String date;
    private float totalPrice;
    private Point point;
    private Client buyer;

    public Invoice(String code, ArrayList<Product> products, String date, float totalPrice, Point point, Client buyer) {
        this.code = code;
        this.products = products;
        this.date = date;
        this.totalPrice = totalPrice;
        this.point = point;
        this.buyer = buyer;
    }

    //GETTERS SETTERS
    public String getCode() {
        return code;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Client getBuyer() {
        return buyer;
    }

    public void setBuyer(Client buyer) {
        this.buyer = buyer;
    }

}
