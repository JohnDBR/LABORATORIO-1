/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import classes.Client;
import classes.Invoice;
import classes.Point;
import classes.Product;
import classes.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;
import managers.ChainManager;
import managers.UserManager;

/**
 *
 * @author djbarbosa
 */
public class Sample {

    static ChainManager cm = new ChainManager(); //LINE FOR BACK
    static UserManager um = new UserManager(); //LINE FOR BACK

    static void print() {
        System.out.println("Cadena: ");
        System.out.println(cm.getChain().getName() + "," + cm.getChain().getOwner());
        System.out.println("");

        //DEVELOP
        //Products On Chain
        System.out.println("Productos en la Bodega:");
        for (Product product : cm.getChain().getProducts()) {
            System.out.println("Producto " + product.getCode() + ", " + product.getName() + ", " + product.getQuantity() + ", $" + product.getPrice());
        }
        System.out.println("");

        //Points
        System.out.println("Puntos de venta:");
        for (Point point : cm.getChain().getPoints()) {
            System.out.println("Punto " + point.getCode() + ", " + point.getAddress());
            for (Product product : cm.getPoint(point.getCode()).getProducts()) {
                System.out.println("Producto " + product.getCode() + ", " + product.getName() + ", " + product.getQuantity() + ", $" + product.getPrice());
            }
            System.out.println("");
        }
        System.out.println("");

        //Clients
        System.out.println("Clientes: ");
        for (Client client : cm.getChain().getClients()) {
            System.out.println("Cliente " + client.getName() + ", " + client.getCode() + ", " + client.getAddress() + ", " + client.getCellphone() + ", " + client.getEmail());
            //invoices
            //System.out.println("");
        }
        System.out.println("");

        //Usuarios y contraseñas
        System.out.println("Regsitros de Usuario: ");
        for (User user : um.getUsers()) {
            System.out.println(user.getUsername() + ", " + user.getPassword() + ", " + user.getLevel());
        }
    }

    public static void main(String[] args) throws IOException {
        //"FRONT"
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        //DEVELOP
        //ArrayList<Product> car = new ArrayList<>();
        //car.add(new Product("4", "Chicle", (float) 2.99, 10));
        //String totalPrice = Float.toString((float) (2.99*10));
        //System.out.println(cm.createInvoice(cm.generateInvoiceCode(), car, new SimpleDateFormat("dd-MM-yyyy").format(new Date()), totalPrice, "0", "123456789"));
        System.out.println("");
        String result;
        do {
            System.out.print("Username: ");
            String username = br.readLine();
            System.out.print("Password: ");
            String password = br.readLine();
            result = um.logIn(username, password); //LINE FOR BACK
            System.out.println("");
        } while (result.equals("NONE"));

        int level = Integer.valueOf(result);
        switch (level) {
            case 0:
                int op = 0;
                do {
                    System.out.println("1. Vender");
                    System.out.println("2. Crear Usuarios");
                    System.out.println("3. Crear punto");
                    System.out.println("4. Crear producto");
                    System.out.println("5. Abastecer punto");
                    System.out.println("6. Ver facturas");
                    System.out.println("7. Crear cliente");
                    System.out.println("8. Estado de tienda");
                    System.out.println("0. Salir");
                    System.out.print("OPCION: ");
                    op = Integer.valueOf(br.readLine());
                    System.out.println("");

                    switch (op) {
                        case 0:
                            System.out.println("Bye!");
                            break;
                        case 1:
                            String cedula = "",
                             pointCode = "";
                            do {
                                System.out.print("CEDULA: ");
                                cedula = br.readLine();
                                System.out.print("CODIGO PUNTO DE VENTA: ");
                                pointCode = br.readLine();
                            } while (!cm.startSale(cedula, pointCode)); //LINE FOR BACK
                            boolean stay = true;
                            do {
                                System.out.print("CODIGO DE PRODUCTO: ");
                                String productCode = br.readLine();
                                System.out.print("CANTIDAD: ");
                                String quantity = br.readLine();
                                if (!cm.passProduct(productCode, quantity)) { //LINE FOR BACK
                                    System.out.println("Producto invalido!");
                                }
                                System.out.println("¿Seguimos? S-si, N-no, A-abortar compra");
                                String option = br.readLine();
                                if (option.equals("A")) {
                                    cm.abortSale(); //LINE FOR BACK
                                } else if (!option.equals("S")) {
                                    stay = false;
                                    cm.finishSale(); //LINE FOR BACK
                                }
                            } while (stay);

                            System.out.println("");
                            break;
                        case 2:
                            String username = "",
                             password = "";
                            do {
                                System.out.print("USERNAME: ");
                                username = br.readLine();
                                System.out.print("PASSWORD: ");
                                password = br.readLine();
                            } while (um.createUser(username, password, "0")); //LINE FOR BACK
                            break;
                        case 3:
                            System.out.print("ADDRESS: ");
                            String address = br.readLine();
                            cm.createPoint(address, cm.generatePointCode()); //LINE FOR BACK
                            break;
                        case 4:
                            String code = cm.generateProductCode(), //LINE FOR BACK
                             name = "",
                             price = "",
                             quantity = "";
                            do {
                                System.out.print("NOMBRE: ");
                                name = br.readLine();
                                System.out.print("PRECIO: ");
                                price = br.readLine();
                                System.out.print("CANTIDAD: ");
                                quantity = br.readLine();
                            } while (cm.createProduct(code, name, price, quantity)); //LINE FOR BACK
                            cm.sortProducts(code); //LINE FOR BACK
                            break;
                        case 5:
                            pointCode = "";
                            String productCode = "",
                             toPlus = "";
                            do {
                                System.out.print("CODIGO DE PUNTO: ");
                                pointCode = br.readLine();
                                System.out.print("CODIGO DE PRODUCTO: ");
                                productCode = br.readLine();
                                System.out.print("CANTIDAD: ");
                                toPlus = br.readLine();
                            } while (!cm.addAmountOfProductOnPoint(productCode, pointCode, toPlus)); //LINE FOR BACK
                            break;
                        case 6:
                            System.out.println("Facturas: ");
                            for (Invoice sale : cm.getChain().getSales()) {
                                System.out.println(sale.getCode() + ", " + sale.getDate() + ", " + sale.getBuyer().getCode() + ", $" + sale.getTotalPrice());
                            }
                            System.out.println("");

                            String invoiceCode;
                            Invoice invoice;
                            do {
                                System.out.print("CODIGO DE FACTURA: ");
                                invoiceCode = br.readLine();
                                invoice = cm.getInvoice(invoiceCode);
                            } while (invoice == null);

                            System.out.println("\nProductos:");
                            for (Product product : invoice.getProducts()) {
                                System.out.println(product.getCode() + ", " + product.getName() + ", " + product.getPrice() + ", " + product.getQuantity());
                            }
                            System.out.println("");
                            break;
                        case 7:
                            name = "";
                            cedula = "";
                            address = "";
                            String email,
                             cellphone;
                            do {
                                System.out.print("NOMBRE: ");
                                name = br.readLine();
                                System.out.print("CEDULA: ");
                                cedula = br.readLine();
                                System.out.print("DIRECCION: ");
                                address = br.readLine();
                                System.out.print("EMAIL: ");
                                email = br.readLine();
                                System.out.println("CELLPHONE: ");
                                cellphone = br.readLine();
                            } while (cm.createClient(name, cedula, address, email, cellphone));
                            break;
                        case 8:
                            String date;
                            do {
                                System.out.println("Ingrese la fecha en la que quiere ver el estado de la tienda "
                                        + "con el siguiente formato "
                                        + "(DD-MM-AAAA)");
                                System.out.print("DATE: ");
                                date = br.readLine();
                            } while (!cm.validateDateFormat(date));
                            result = cm.searchFileToLoad(date);
                            if (!result.equals("NONE")) {
                                cm.read(result);
                                print();
                                System.out.println("");
                            } else {
                                System.out.println("Rango de fecha invalido!");
                            }
                            break;
                    }
                } while (op != 0);
                break;
        }
    }
}
