/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *
 * @author john
 */
public class Chain extends Master {

    private String name;
    private String owner;

    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<Point> points = new ArrayList<>();
    private ArrayList<Client> clients = new ArrayList<>();
    private ArrayList<Invoice> sales = new ArrayList<>();

    public Chain(String name, String owner) {
        if (validateNonSpecialCharacters(name) && validateNonSpecialCharacters(owner)) {
            this.name = name;
            this.owner = owner;
        } else {
            System.out.println("Invalid Arguments!");
        }
    }

    //CREATE
    public boolean createProduct(String code, String name, String price, String quantity) {
        if (validateNonSpecialCharacters(code) && validateNonSpecialCharacters(name) && validateFloat(price) && validateInt(quantity)) {
            products.add(new Product(code, name, Float.parseFloat(price), Integer.parseInt(quantity)));
            save();
            return true;
        }
        return false;
    }

    public boolean createProductOnPoint(String pointCode, String code, String name, String price, String quantity) {
        if (getPoint(pointCode) != null) {
            boolean b = getPoint(pointCode).createProduct(code, name, price, quantity);
            if (b) {
                save();
                return true;
            }
        }
        return false;
    }

    public boolean createClient(String name, String cedula, String address, String email, String cellphone) {
        if (validateWord(name) && validateNumber(cedula) && validateCellphone(cellphone) && validateEmail(email)) {
            clients.add(new Client(name, cedula, address, email, cellphone));
            save();
            return true;
        }
        return false;
    }

    public boolean createPoint(String address, String code) {
        if (validateNumber(code)) {
            points.add(new Point(address, code));
            save();
            return true;
        }
        return false;
    }

    //READ
    //UPDATE
    //DELETE
    //SEARCH
    public Product getProduct(String code) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getCode() == code) {
                return products.get(i);
            }
        }
        return null;
    }

    public Client getClient(String cedula) {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getCode() == cedula) {
                return clients.get(i);
            }
        }
        return null;
    }

    public Point getPoint(String code) {
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).getCode() == code) {
                return points.get(i);
            }
        }
        return null;
    }

    //BASICS
    public String Start() { //FOR LOADING ALL!!
        return "Ready!";
    }

    public void sortProducts() {
        int n;
        if ((n = points.size()) > 0 && products.size() > 0) {
            if (n == 1) {
                points.get(0).setProducts(products);
            } else {
                ArrayList<Integer> delivery = new ArrayList<>();
                for (Point point : points) {
                    delivery.add(0);
                }

                for (Product product : products) {
                    int quantity = product.getQuantity();
                    int cont = 1;
                    while (quantity != 0) {
                        for (int j = 0; j < delivery.size(); j++) {
                            if (quantity > 0) {
                                delivery.set(j, cont);
                                quantity--;
                            } else {
                                break;
                            }
                        }
                        cont++;
                    }
                    int i = 0;
                    for (Point point : points) {
                        point.getProducts().add(new Product(product.getCode(), product.getName(), product.getPrice(), delivery.get(i)));
                        delivery.set(i, 0);
                        i++;
                    }
                }
            }
            products.clear();
        }
    }

    public void sortProducts(Product product) {
        int n;
        if ((n = points.size()) > 0 && product.getQuantity() > 0) {
            if (n == 1) {
                points.get(0).setProducts(products);
            } else {
                ArrayList<Integer> delivery = new ArrayList<>();
                for (Point point : points) {
                    delivery.add(0);
                }

                int quantity = product.getQuantity();
                int cont = 1;
                while (quantity != 0) {
                    for (int j = 0; j < delivery.size(); j++) {
                        if (quantity > 0) {
                            delivery.set(j, cont);
                            quantity--;
                        } else {
                            break;
                        }
                    }
                    cont++;
                }
                int i = 0;
                for (Point point : points) {
                    point.getProducts().add(new Product(product.getCode(), product.getName(), product.getPrice(), delivery.get(i)));
                    delivery.set(i, 0);
                    i++;
                }
            }
            products.remove(product);
        }
    }

    //STORER
    public void save() {
        try {
            // Write to disk with FileOutputStream
            FileOutputStream f_out = new FileOutputStream("./database/load.data");

            // Write object with ObjectOutputStream
            ObjectOutputStream obj_out = new ObjectOutputStream(f_out);

            // Write object out to disk
            obj_out.writeObject(this);
        } catch (Exception ex) {
            System.out.println("Datos  no guardados!");
            return;
        }
    }

    public void read() {
        try {
            // Read from disk using FileInputStream
            FileInputStream f_in = new FileInputStream("./database/load.data");

            // Read object using ObjectInputStream
            ObjectInputStream obj_in = new ObjectInputStream(f_in);

            // Read an object
            this = (Chain) obj_in.readObject();
        } catch (Exception ex) {
            System.out.println("No ha sido creada");
            this = null;
        }
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

}
