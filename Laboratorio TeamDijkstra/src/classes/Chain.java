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
public class Chain implements java.io.Serializable {

    private String name;
    private String owner;

    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<Point> points = new ArrayList<>();
    private ArrayList<Client> clients = new ArrayList<>();
    private ArrayList<Invoice> sales = new ArrayList<>();

    private ArrayList<String> productRecord = new ArrayList<>();

    public Chain(String name, String owner) {
        this.name = name;
        this.owner = owner;
    }

    //GETTERS SETTERS
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
    }

    public ArrayList<Invoice> getSales() {
        return sales;
    }

    public void setSales(ArrayList<Invoice> sales) {
        this.sales = sales;
    }

    public ArrayList<String> getProductRecord() {
        return productRecord;
    }

    public void setProductRecord(ArrayList<String> productRecord) {
        this.productRecord = productRecord;
    }
}
