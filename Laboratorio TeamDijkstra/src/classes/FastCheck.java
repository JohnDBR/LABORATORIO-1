/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author djbarbosa
 */
public class FastCheck {

    private static String createFileToSave() {
        File folder = new File("./database");
        File[] listOfFiles = folder.listFiles();
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        for (File file : listOfFiles) {
            String[] nameFields = file.getName().split("\\,");
            if (nameFields[0].equals("load")) {
                if (nameFields[2].contains("8.") && nameFields[2].length() < 7) {
                    file.renameTo(new File("./database/" + nameFields[0] + "," + nameFields[1] + "," + date + ".data"));
                    /*try {
                        Files.move(file.toPath(), new File(nameFields[0] + "," + nameFields[1] + "," + date + ".data").toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    } catch (Exception ex) {
                        System.out.println("Hola");
                    }*/
                    //new File("loc/xyz1.mp3").renameTo(new File("loc/xyz.mp3"));
                    break;
                }
            }
        }
        return "./database/load," + date + ",8.data";
    }

    public static void main(String[] args) {
        System.out.println(createFileToSave());
    }
}
