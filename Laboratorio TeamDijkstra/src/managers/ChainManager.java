/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import classes.Chain;
import classes.Client;
import classes.Invoice;
import classes.Point;
import classes.Product;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import validations.Validation;

/**
 *
 * @author john
 */
public class ChainManager extends Validation {

    private Chain chain;

    private ArrayList<Product> productCar = new ArrayList<>();
    private Client buyer;
    private float totalPrice;

    public ChainManager() {
        read(searchFileToSave());
    }

    //CREATE
    public boolean createChain(String name, String owner) {
        if (validateNonSpecialCharacters(name) && validateNonSpecialCharacters(owner)) {
            chain = new Chain(name, owner);
            return true;
        }
        return false;
    }

    public boolean createPoint(String address, String code) {
        if (validateNumber(code)) {
            chain.getPoints().add(new Point(address, code));
            return true;
        }
        return false;
    }

    public boolean createProduct(String code, String name, String price, String quantity) {
        if (validateNonSpecialCharacters(code) && validateNonSpecialCharacters(name) && validateFloat(price) && validateInt(quantity)) {
            chain.getProducts().add(new Product(code, name, Float.parseFloat(price), Integer.parseInt(quantity)));
            return true;
        }
        return false;
    }

    public boolean createClient(String name, String cedula, String address, String email, String cellphone) {
        if (validateWord(name) && validateNumber(cedula) && validateCellphone(cellphone) && validateEmailFormat(email)) {
            chain.getClients().add(new Client(name, cedula, address, email, cellphone));
            return true;
        }
        return false;
    }

    public boolean createInvoice(String code, ArrayList<Product> products, String date, String totalPrice, String pointCode, String cedula) {
        if (validateNonSpecialCharacters(code) && validateNumber(cedula) && validateFloat(totalPrice) && validateDateFormat(date)) {
            if (getPoint(pointCode) != null) {
                chain.getSales().add(new Invoice(code, products, date, Float.valueOf(totalPrice), getPoint(pointCode), getClient(cedula)));
                return true;
            }
        }
        return false;
    }

    public boolean createProductOnPoint(String pointCode, String code, String name, String price, String quantity) {
        if (validateNonSpecialCharacters(code) && validateNonSpecialCharacters(name) && validateFloat(price) && validateInt(quantity)) {
            if (getPoint(pointCode) != null) {
                getPoint(pointCode).getProducts().add(new Product(code, name, Float.valueOf(price), Integer.valueOf(quantity)));
                return true;
            }
        }
        return false;
    }

    public boolean createInoviceOnPoint(String code, ArrayList<Product> products, String date, String totalPrice, String pointCode, String cedula) {
        if (validateNonSpecialCharacters(code) && validateNumber(cedula) && validateFloat(totalPrice) && validateDateFormat(date)) {
            if (getPoint(pointCode) != null) {
                getPoint(pointCode).getSales().add(new Invoice(code, products, date, Float.valueOf(totalPrice), getPoint(pointCode), getClient(cedula)));
                return true;
            }
        }
        return false;
    }

    public boolean createInvoiceOnClient(String code, ArrayList<Product> products, String date, String totalPrice, String pointCode, String cedula) {
        if (validateNonSpecialCharacters(code) && validateNumber(cedula) && validateFloat(totalPrice) && validateDateFormat(date)) {
            if (getPoint(pointCode) != null) {
                getClient(cedula).getPurchases().add(new Invoice(code, products, date, Float.valueOf(totalPrice), getPoint(pointCode), getClient(cedula)));
                return true;
            }
        }
        return false;
    }

    //READ
    public Product getProduct(String code) {
        for (int i = 0; i < chain.getProducts().size(); i++) {
            if (chain.getProducts().get(i).getCode() == code) {
                return chain.getProducts().get(i);
            }
        }
        return null;
    }

    public Client getClient(String cedula) {
        for (int i = 0; i < chain.getClients().size(); i++) {
            if (chain.getClients().get(i).getCode() == cedula) {
                return chain.getClients().get(i);
            }
        }
        return null;
    }

    public Point getPoint(String code) {
        for (int i = 0; i < chain.getPoints().size(); i++) {
            if (chain.getPoints().get(i).getCode() == code) {
                return chain.getPoints().get(i);
            }
        }
        return null;
    }

    public Invoice getInvoice(String code) {
        for (int i = 0; i < chain.getSales().size(); i++) {
            if (chain.getSales().get(i).getCode() == code) {
                return chain.getSales().get(i);
            }
        }
        return null;
    }

    public Product getProductOnPoint(String code, String pointCode) {
        if (getPoint(pointCode) != null) {
            for (int i = 0; i < getPoint(pointCode).getProducts().size(); i++) {
                if (getPoint(pointCode).getProducts().get(i).getCode() == code) {
                    return getPoint(pointCode).getProducts().get(i);
                }
            }
        }
        return null;
    }

    public Invoice getInvoiceOnPoint(String code, String pointCode) {
        if (getPoint(pointCode) != null) {
            for (int i = 0; i < getPoint(pointCode).getSales().size(); i++) {
                if (getPoint(pointCode).getSales().get(i).getCode() == code) {
                    return getPoint(pointCode).getSales().get(i);
                }
            }
        }
        return null;
    }

    public Invoice getInvoiceOnClient(String code, String cedula) {
        if (getClient(cedula) != null) {
            for (int i = 0; i < getClient(cedula).getPurchases().size(); i++) {
                if (getClient(cedula).getPurchases().get(i).getCode() == code) {
                    return getClient(cedula).getPurchases().get(i);
                }
            }
        }
        return null;
    }

    //UPDATE
    public boolean updateProduct(String productCode, String name, String price, String quantity) {
        if (getProduct(productCode) != null) {
            if (validateNonSpecialCharacters(name) && validateFloat(price) && validateInt(quantity)) {
                getProduct(productCode).setName(name);
                getProduct(productCode).setPrice(Float.valueOf(price));
                getProduct(productCode).setQuantity(Integer.valueOf(quantity));
                return true;
            }
        }
        return false;
    }

    public boolean updatePoint(String pointCode, String address) {
        if (getPoint(pointCode) != null) {
            getPoint(pointCode).setAddress(address);
            return true;
        }
        return false;
    }

    public boolean updateClient(String cedula, String name, String address, String email, String cellphone) {
        if (getClient(cedula) != null) {
            if (validateWord(name) && validateCellphone(cellphone) && validateEmailFormat(email)) {
                getClient(cedula).setName(name);
                getClient(cedula).setAddress(address);
                getClient(cedula).setEmail(email);
                getClient(cedula).setCellphone(cellphone);
                return true;
            }
        }
        return false;
    }

    public boolean updateInvoice(String invoiceCode, ArrayList<Product> products, String date, String totalPrice, String pointCode, String cedula) {
        if (getInvoice(invoiceCode) != null && getPoint(pointCode) != null && getClient(cedula) != null) {
            if (validateFloat(totalPrice) && validateDateFormat(date)) {
                getInvoice(invoiceCode).setProducts(products);
                getInvoice(invoiceCode).setDate(date);
                getInvoice(invoiceCode).setTotalPrice(Float.parseFloat(totalPrice));
                getInvoice(invoiceCode).setPoint(getPoint(pointCode));
                getInvoice(invoiceCode).setBuyer(getClient(cedula));
                return true;
            }
        }
        return false;
    }

    public boolean updateProductOnPoint(String productCode, String pointCode, String name, String price, String quantity) {
        if (getProductOnPoint(productCode, pointCode) != null) {
            if (validateNonSpecialCharacters(name) && validateFloat(price) && validateInt(quantity)) {
                getProductOnPoint(productCode, pointCode).setName(name);
                getProductOnPoint(productCode, pointCode).setPrice(Float.parseFloat(price));
                getProductOnPoint(productCode, pointCode).setQuantity(Integer.valueOf(quantity));
                return true;
            }
        }
        return false;
    }

    public boolean updateInvoiceOnPoint(String invoiceCode, String pointCode, ArrayList<Product> products, String date, String totalPrice, String cedula) {
        if (getInvoiceOnPoint(invoiceCode, pointCode) != null && getClient(cedula) != null) {
            if (validateFloat(totalPrice) && validateDateFormat(date)) {
                getInvoiceOnPoint(invoiceCode, pointCode).setProducts(products);
                getInvoiceOnPoint(invoiceCode, pointCode).setDate(date);
                getInvoiceOnPoint(invoiceCode, pointCode).setTotalPrice(Float.parseFloat(totalPrice));
                getInvoiceOnPoint(invoiceCode, pointCode).setBuyer(getClient(cedula));
            }
        }
        return false;
    }

    public boolean updateInvoiceOnClient(String invoiceCode, String cedula, ArrayList<Product> products, String date, String totalPrice, String pointCode) {
        if (getInvoiceOnClient(invoiceCode, cedula) != null && getPoint(pointCode) != null) {
            if (validateFloat(totalPrice) && validateDateFormat(date)) {
                getInvoiceOnClient(invoiceCode, cedula).setProducts(products);
                getInvoiceOnClient(invoiceCode, cedula).setDate(date);
                getInvoiceOnClient(invoiceCode, cedula).setTotalPrice(Float.parseFloat(totalPrice));
                getInvoiceOnClient(invoiceCode, cedula).setPoint(getPoint(pointCode));
            }
        }
        return false;
    }

    //DELETE 
    public boolean deletePoint(String pointCode) {
        if (getPoint(pointCode) != null) {
            chain.getPoints().remove(getPoint(pointCode));
            return true;
        }
        return false;
    }

    public boolean deleteProduct(String productCode) {
        if (getProduct(productCode) != null) {
            chain.getProducts().remove(getProduct(productCode));
            return true;
        }
        return false;
    }

    public boolean deleteClient(String cedula) {
        if (getClient(cedula) != null) {
            chain.getClients().remove(getClient(cedula));
            return true;
        }
        return false;
    }

    public boolean deleteInvoice(String invoiceCode) {
        if (getInvoice(invoiceCode) != null) {
            chain.getSales().remove(getInvoice(invoiceCode));
            return true;
        }
        return false;
    }

    public boolean deleteProductOnPoint(String productCode, String pointCode) {
        if (getProductOnPoint(productCode, pointCode) != null && getPoint(pointCode) != null) {
            getPoint(pointCode).getProducts().remove(getProductOnPoint(productCode, pointCode));
            return true;
        }
        return false;
    }

    public boolean deleteInvoiceOnPoint(String invoiceCode, String pointCode) {
        if (getInvoice(invoiceCode) != null && getPoint(pointCode) != null) {
            getPoint(pointCode).getSales().remove(getInvoice(invoiceCode));
            return true;
        }
        return false;
    }

    public boolean deleteInvoiceOnClient(String invoiceCode, String cedula) {
        if (getInvoiceOnClient(invoiceCode, cedula) != null && getClient(cedula) != null) {
            getClient(cedula).getPurchases().remove(getInvoiceOnClient(invoiceCode, cedula));
            return true;
        }
        return false;
    }

    //OTHER CHAIN OPERATIONS
    public void sortProducts() {
        int n;
        if ((n = chain.getPoints().size()) > 0 && chain.getProducts().size() > 0) {
            if (n == 1) {
                chain.getPoints().get(0).setProducts(chain.getProducts());
            } else {
                ArrayList<Integer> delivery = new ArrayList<>();
                for (Point point : chain.getPoints()) {
                    delivery.add(0);
                }

                for (Product product : chain.getProducts()) {
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
                    for (Point point : chain.getPoints()) {
                        point.getProducts().add(new Product(product.getCode(), product.getName(), product.getPrice(), delivery.get(i)));
                        delivery.set(i, 0);
                        i++;
                    }
                }
            }
            chain.getProducts().clear();
        }
    }

    public void sortProducts(Product product) {
        int n;
        if ((n = chain.getPoints().size()) > 0 && product.getQuantity() > 0) {
            if (n == 1) {
                chain.getPoints().get(0).setProducts(chain.getProducts());
            } else {
                ArrayList<Integer> delivery = new ArrayList<>();
                for (Point point : chain.getPoints()) {
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
                for (Point point : chain.getPoints()) {
                    point.getProducts().add(new Product(product.getCode(), product.getName(), product.getPrice(), delivery.get(i)));
                    delivery.set(i, 0);
                    i++;
                }
            }
            chain.getProducts().remove(product);
        }
    }

    //STORER
    private void save(String fileRoute) {
        try {
            // Write to disk with FileOutputStream
            FileOutputStream f_out = new FileOutputStream(fileRoute);

            // Write object with ObjectOutputStream
            ObjectOutputStream obj_out = new ObjectOutputStream(f_out);

            // Write object out to disk
            obj_out.writeObject(chain);
        } catch (Exception ex) {
            System.out.println("Datos  no guardados!");
        }
    }

    private void read(String fileRoute) {
        try {
            // Read from disk using FileInputStream
            FileInputStream f_in = new FileInputStream(fileRoute);

            // Read object using ObjectInputStream
            ObjectInputStream obj_in = new ObjectInputStream(f_in);

            // Read an object
            chain = (Chain) obj_in.readObject();
        } catch (Exception ex) {
            System.out.println("No ha sido creada");
            chain = null;
        }
    }

    private String searchFileToSave() {
        File folder = new File("./database");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            //System.out.println(file.getName());
            String[] nameFields = file.getName().split("\\,");
            if (nameFields[0].equals("load")) {
                if (nameFields[2].contains("8.") && nameFields[2].length() < 7) {
                    return file.getName();
                }
            }
        }
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        return "./database/load," + date + ",8.data";
    }

    private String searchFileToLoad(String date) {
        File folder = new File("./database");
        File[] listOfFiles = folder.listFiles();
        String[] dateFields = date.split("\\-");
        for (File file : listOfFiles) {
            String[] nameFields = file.getName().split("\\,");
            if (nameFields[0].equals("load")) {
                String[] date1 = nameFields[1].split("\\-");
                String[] date2 = nameFields[2].split("\\-");
                if (Integer.valueOf(date1[2]) <= Integer.valueOf(dateFields[2]) && Integer.valueOf(date2[2]) >= Integer.valueOf(dateFields[2])) {
                    if (Integer.valueOf(date1[1]) <= Integer.valueOf(dateFields[1]) && Integer.valueOf(date2[1]) >= Integer.valueOf(dateFields[1])) {
                        if (Integer.valueOf(date1[0]) <= Integer.valueOf(dateFields[0]) && Integer.valueOf(date2[0]) >= Integer.valueOf(dateFields[0])) {
                            return file.getName();
                        }
                    }
                }
            }
        }
        return "NONE";
    }

}
