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
    private Client buyer = null;
    private Point sellingPoint = null;
    private float totalPrice;

    public ChainManager() {
        read(searchFileToSave());
    }

    //ESSENTIAL OPERATIONS
    public boolean startSale(String cedula, String pointCode) {
        productCar.clear();
        buyer = getClient(cedula);
        sellingPoint = getPoint(pointCode);
        if (buyer != null && sellingPoint != null) {
            return true;
        }
        return false;
    }

    public void abortSale() {
        productCar.clear();
        buyer = null;
        sellingPoint = null;
        read(searchFileToSave());
    }

    public void finishSale() {
        if (!productCar.isEmpty() && buyer != null && sellingPoint != null) {
            totalPrice = 0;
            for (Product product : productCar) {
                totalPrice = totalPrice + product.getPrice() * product.getQuantity();
            }
            String invoiceCode = generateInvoiceCode();
            String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            Boolean a = createInvoice(invoiceCode, productCar, date, Float.toString(totalPrice), sellingPoint.getCode(), buyer.getCode());
            Boolean b = createInvoiceOnPoint(invoiceCode, productCar, date, Float.toString(totalPrice), sellingPoint.getCode(), buyer.getCode());
            Boolean c = createInvoiceOnClient(invoiceCode, productCar, date, Float.toString(totalPrice), sellingPoint.getCode(), buyer.getCode());
            if (a && b && c) {
                save(searchFileToSave());
            } else {
                System.out.println("Error para crear las facturas!");
            }
        }
    }

    public boolean passProduct(String productCode, String quantity) {
        Product product = getProductOnPoint(productCode, sellingPoint.getCode());
        if (product != null && validateInt(quantity)) {
            int amount = Integer.valueOf(quantity);
            int inventory = product.getQuantity() - amount;
            if (inventory >= 0 && amount > 0) {
                productCar.add(new Product(productCode, product.getName(), product.getPrice(), amount));
                updateProductOnPoint(productCode, sellingPoint.getCode(), product.getName(), Float.toString(product.getPrice()), "" + inventory);
                return true;
            }
        }
        return false;
    }

    public boolean addAmountOfProduct(String productCode, String toPlus) {
        Product product = getProduct(productCode);
        if (product != null && validateInt(toPlus)) {
            int amount = product.getQuantity() + Integer.valueOf(toPlus);
            updateProduct(productCode, product.getName(), Float.toString(product.getPrice()), amount + "");
            save(searchFileToSave());
            return true;
        }
        return false;
    }

    public boolean addAmountOfProductOnPoint(String productCode, String pointCode, String toPlus) {
        Product product = getProductOnPoint(productCode, pointCode);
        if (product != null && validateInt(toPlus)) {
            int amount = product.getQuantity() + Integer.valueOf(toPlus);
            updateProductOnPoint(productCode, pointCode, product.getName(), Float.toString(product.getPrice()), amount + "");
            save(searchFileToSave());
            return true;
        }
        return false;
    }

    public void modifyProduct(String productCode, String name, String price) {
        boolean update = false;
        if (getProduct(productCode) != null || chain.getProductRecord().contains(productCode)) {
            try {
                update = updateProduct(productCode, name, price, Integer.toString(getProduct(productCode).getQuantity()));
            } catch (Exception e) {
            }
            for (int i = 0; i < chain.getPoints().size(); i++) {
                try {
                    if (updateProductOnPoint(productCode, chain.getPoints().get(i).getCode(), name, price, Integer.toString(getProductOnPoint(productCode, "" + i).getQuantity()))) {
                        update = true;
                    }
                } catch (Exception e) {
                }
            }
        }
        if (update) {
            save(createFileToSave());
        } else {
            read(searchFileToSave());
        }
    }

    public void eraseProduct(String productCode) {
        boolean delete = false;
        if (getProduct(productCode) != null || chain.getProductRecord().contains(productCode)) {
            delete = deleteProduct(productCode);
            for (int i = 0; i < chain.getPoints().size(); i++) {
                if (deleteProductOnPoint(productCode, chain.getPoints().get(i).getCode())) {
                    delete = true;
                }
            }
        }
        if (delete) {
            save(createFileToSave());
        } else {
            read(searchFileToSave());
        }
    }

    //CREATE
    public boolean createChain(String name, String owner) {
        if (!validateNonSpecialCharacters(name) && !validateNonSpecialCharacters(owner)) {
            chain = new Chain(name, owner);
            save(searchFileToSave());
            return true;
        }
        return false;
    }

    public boolean createPoint(String address, String code) {
        if (validateNumber(code)) {
            chain.getPoints().add(new Point(address, code));
            save(searchFileToSave());
            return true;
        }
        return false;
    }

    public boolean createProduct(String code, String name, String price, String quantity) {
        if (!validateNonSpecialCharacters(code) && !validateNonSpecialCharacters(name) && validateFloat(price) && validateInt(quantity)) {
            chain.getProducts().add(new Product(code, name, Float.parseFloat(price), Integer.parseInt(quantity)));
            chain.getProductRecord().add(code);
            save(searchFileToSave());
            return true;
        }
        return false;
    }

    public boolean createClient(String name, String cedula, String address, String email, String cellphone) {
        if (validateWord(name) && validateNumber(cedula) && validateCellphone(cellphone) && validateEmailFormat(email)) {
            boolean repeatedCedula = false;
            for (Client client : chain.getClients()) {
                if (cedula.equals(client.getCode())) {
                    repeatedCedula = true;
                    break;
                }
            }
            if (!repeatedCedula) {
                chain.getClients().add(new Client(name, cedula, address, email, cellphone));
                save(searchFileToSave());
                return true;
            }
        }
        return false;
    }

    public boolean createInvoice(String code, ArrayList<Product> products, String date, String totalPrice, String pointCode, String cedula) {
        //System.out.println(validateDateFormat(date) + ", " + date);
        if (!validateNonSpecialCharacters(code) && validateNumber(cedula) && validateFloat(totalPrice) && validateDateFormat(date)) {
            if (getPoint(pointCode) != null) {
                chain.getSales().add(new Invoice(code, products, date, Float.valueOf(totalPrice), getPoint(pointCode), getClient(cedula)));
                save(searchFileToSave());
                return true;
            }
        }
        return false;
    }

    public boolean createProductOnPoint(String pointCode, String code, String name, String price, String quantity) {
        if (!validateNonSpecialCharacters(code) && !validateNonSpecialCharacters(name) && validateFloat(price) && validateInt(quantity)) {
            if (getPoint(pointCode) != null) {
                getPoint(pointCode).getProducts().add(new Product(code, name, Float.valueOf(price), Integer.valueOf(quantity)));
                chain.getProductRecord().add(code);
                save(searchFileToSave());
                return true;
            }
        }
        return false;
    }

    public boolean createInvoiceOnPoint(String code, ArrayList<Product> products, String date, String totalPrice, String pointCode, String cedula) {
        if (!validateNonSpecialCharacters(code) && validateNumber(cedula) && validateFloat(totalPrice) && validateDateFormat(date)) {
            if (getPoint(pointCode) != null) {
                getPoint(pointCode).getSales().add(new Invoice(code, products, date, Float.valueOf(totalPrice), getPoint(pointCode), getClient(cedula)));
                save(searchFileToSave());
                return true;
            }
        }
        return false;
    }

    public boolean createInvoiceOnClient(String code, ArrayList<Product> products, String date, String totalPrice, String pointCode, String cedula) {
        if (!validateNonSpecialCharacters(code) && validateNumber(cedula) && validateFloat(totalPrice) && validateDateFormat(date)) {
            if (getPoint(pointCode) != null) {
                getClient(cedula).getPurchases().add(new Invoice(code, products, date, Float.valueOf(totalPrice), getPoint(pointCode), getClient(cedula)));
                save(searchFileToSave());
                return true;
            }
        }
        return false;
    }

    //READ
    public Product getProduct(String code) {
        for (int i = 0; i < chain.getProducts().size(); i++) {
            if (chain.getProducts().get(i).getCode().equals(code)) {
                return chain.getProducts().get(i);
            }
        }
        return null;
    }

    public Client getClient(String cedula) {
        for (int i = 0; i < chain.getClients().size(); i++) {
            if (chain.getClients().get(i).getCode().equals(cedula)) {
                return chain.getClients().get(i);
            }
        }
        return null;
    }

    public Point getPoint(String code) {
        for (int i = 0; i < chain.getPoints().size(); i++) {
            if (chain.getPoints().get(i).getCode().equals(code)) {
                return chain.getPoints().get(i);
            }
        }
        return null;
    }

    public Invoice getInvoice(String code) {
        for (int i = 0; i < chain.getSales().size(); i++) {
            if (chain.getSales().get(i).getCode().equals(code)) {
                return chain.getSales().get(i);
            }
        }
        return null;
    }

    public Product getProductOnPoint(String code, String pointCode) {
        if (getPoint(pointCode) != null) {
            for (int i = 0; i < getPoint(pointCode).getProducts().size(); i++) {
                if (getPoint(pointCode).getProducts().get(i).getCode().equals(code)) {
                    return getPoint(pointCode).getProducts().get(i);
                }
            }
        }
        return null;
    }

    public Invoice getInvoiceOnPoint(String code, String pointCode) {
        if (getPoint(pointCode) != null) {
            for (int i = 0; i < getPoint(pointCode).getSales().size(); i++) {
                if (getPoint(pointCode).getSales().get(i).getCode().equals(code)) {
                    return getPoint(pointCode).getSales().get(i);
                }
            }
        }
        return null;
    }

    public Invoice getInvoiceOnClient(String code, String cedula) {
        if (getClient(cedula) != null) {
            for (int i = 0; i < getClient(cedula).getPurchases().size(); i++) {
                if (getClient(cedula).getPurchases().get(i).getCode().equals(code)) {
                    return getClient(cedula).getPurchases().get(i);
                }
            }
        }
        return null;
    }

    //UPDATE
    public boolean updateProduct(String productCode, String name, String price, String quantity) {
        if (getProduct(productCode) != null) {
            if (!validateNonSpecialCharacters(name) && validateFloat(price) && validateInt(quantity)) {
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
            save(createFileToSave());
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
                save(createFileToSave());
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
            if (!validateNonSpecialCharacters(name) && validateFloat(price) && validateInt(quantity)) {
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
            save(createFileToSave());
            return true;
        }
        return false;
    }

    public boolean deleteProduct(String productCode) {
        if (getProduct(productCode) != null) {
            chain.getProducts().remove(getProduct(productCode));
            chain.getProductRecord().remove(productCode);
            return true;
        }
        return false;
    }

    public boolean deleteClient(String cedula) {
        if (getClient(cedula) != null) {
            chain.getClients().remove(getClient(cedula));
            save(createFileToSave());
            return true;
        }
        return false;
    }

    public boolean deleteInvoice(String invoiceCode) {
        if (getInvoice(invoiceCode) != null) {
            chain.getSales().remove(getInvoice(invoiceCode));
            save(searchFileToSave());
            return true;
        }
        return false;
    }

    public boolean deleteProductOnPoint(String productCode, String pointCode) {
        if (getProductOnPoint(productCode, pointCode) != null && getPoint(pointCode) != null) {
            getPoint(pointCode).getProducts().remove(getProductOnPoint(productCode, pointCode));
            chain.getProductRecord().remove(productCode);
            return true;
        }
        return false;
    }

    public boolean deleteInvoiceOnPoint(String invoiceCode, String pointCode) {
        if (getInvoice(invoiceCode) != null && getPoint(pointCode) != null) {
            getPoint(pointCode).getSales().remove(getInvoice(invoiceCode));
            save(searchFileToSave());
            return true;
        }
        return false;
    }

    public boolean deleteInvoiceOnClient(String invoiceCode, String cedula) {
        if (getInvoiceOnClient(invoiceCode, cedula) != null && getClient(cedula) != null) {
            getClient(cedula).getPurchases().remove(getInvoiceOnClient(invoiceCode, cedula));
            save(searchFileToSave());
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
            save(searchFileToSave());
        }
    }

    public void sortProducts(String productCode) {
        Product product = getProduct(productCode);
        if (product != null) {
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
                save(searchFileToSave());
            }
        }
    }

    public String generateInvoiceCode() {
        if (!chain.getSales().isEmpty()) {
            int lastInvoiceCode = Integer.valueOf(chain.getSales().get(chain.getSales().size() - 1).getCode()) + 1;
            return Integer.toString(lastInvoiceCode);
        }
        return "0";
    }

    public String generatePointCode() {
        if (!chain.getPoints().isEmpty()) {
            int lastPointCode = Integer.valueOf(chain.getPoints().get(chain.getPoints().size() - 1).getCode()) + 1;
            return Integer.toString(lastPointCode);
        }
        return "0";
    }

    public String generateProductCode() {
        if (!chain.getProductRecord().isEmpty()) {
            int lastProductCode = Integer.valueOf(chain.getProductRecord().get(chain.getProductRecord().size() - 1)) + 1;
            return Integer.toString(lastProductCode);
        }
        return "0";
    }

    //STORER
    public void save(String fileRoute) {
        if (fileRoute.equals("ABORT")) {
            read(searchFileToSave());
        } else {
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
    }

    public void read(String fileRoute) {
        try {
            // Read from disk using FileInputStream
            FileInputStream f_in = new FileInputStream(fileRoute);

            // Read object using ObjectInputStream
            ObjectInputStream obj_in = new ObjectInputStream(f_in);

            // Read an object
            chain = (Chain) obj_in.readObject();
        } catch (Exception ex) {
            System.out.println("No ha sido creado el archivo a cargar");
            chain = null;
        }
    }

    public String createFileToSave() {
        File folder = new File("./database");
        File[] listOfFiles = folder.listFiles();
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        for (File file : listOfFiles) {
            String[] nameFields = file.getName().split("\\,");
            if (nameFields[0].equals("load")) {
                if (nameFields[2].contains("8.") && nameFields[2].length() < 7) {
                    if (!nameFields[1].equals(date)) {
                        boolean success = false;
                        do {
                            success = file.renameTo(new File("./database/" + nameFields[0] + "," + nameFields[1] + "," + date + ".data")); //Problem
                        } while (!success);
                        //try {
                        //   Files.move(file.toPath(), new File("./database/" + nameFields[0] + "," + nameFields[1] + "," + date + ".data").toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        //} catch (Exception ex) {
                        //    System.out.println("No se pudo crear el nuevo archivo");
                        //}
                        //new File("loc/xyz1.mp3").renameTo(new File("loc/xyz.mp3"));
                        break;
                    } else {
                        return "ABORT";
                    }
                }
            }
        }
        return "./database/load," + date + ",8.data";
    }

    public String searchFileToSave() {
        File folder = new File("./database");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            String[] nameFields = file.getName().split("\\,");
            if (nameFields[0].equals("load")) {
                if (nameFields[2].contains("8.") && nameFields[2].length() < 7) {
                    return "./database/" + file.getName();
                }
            }
        }
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        return "./database/load," + date + ",8.data";
    }

    public String searchFileToLoad(String date) {
        File folder = new File("./database");
        File[] listOfFiles = folder.listFiles();
        String[] dateFields = date.split("\\-");
        for (File file : listOfFiles) {
            String[] nameFields = file.getName().split("\\,");
            if (nameFields[0].equals("load")) {
                if (dateisWithinRange(date, nameFields[1], nameFields[2].substring(0, nameFields[2].length() - 5))) {
                    return "./database/" + file.getName();
                }
            }
        }
        return "NONE";
    }

    public boolean dateisWithinRange(String testDate, String startDate, String endDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date dateTest = formatter.parse(testDate);
            Date dateStart = formatter.parse(startDate);
            if (endDate.equals("8")) {
                return !dateTest.before(dateStart);
            } else {
                Date dateEnd = formatter.parse(endDate);
                return !(dateTest.before(dateStart) || dateTest.after(dateEnd)) && !dateTest.equals(dateEnd); //Ideal condition !(testDate.before(startDate) || testDate.after(endDate));
            }
        } catch (Exception ex) {
            return false;
        }
    }

    //GETTERS SETTERS
    public Chain getChain() {
        return chain;
    }

    public void setChain(Chain chain) {
        this.chain = chain;
    }

}
