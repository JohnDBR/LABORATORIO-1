/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import managers.ChainManager;

/**
 *
 * @author djbarbosa
 */
public class FastCheck {

    public static void main(String[] args) {
        ChainManager cm = new ChainManager();
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
    }
}
