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
public class Client implements java.io.Serializable {

    private String name;
    private final String cedula;
    private String address;
    private String email;
    private String cellphone;

    private ArrayList<Invoice> purchases = new ArrayList<>();

    public Client(String name, String cedula, String address, String email, String cellphone) {
        this.name = name;
        this.cedula = cedula;
        this.address = address;
        this.email = email;
        this.cellphone = cellphone;
    }

    //GETTERS SETTERS
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return cedula;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public ArrayList<Invoice> getPurchases() {
        return purchases;
    }

    public void setPurchases(ArrayList<Invoice> purchases) {
        this.purchases = purchases;
    }

}
