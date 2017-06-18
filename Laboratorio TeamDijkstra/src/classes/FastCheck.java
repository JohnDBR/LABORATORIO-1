/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.File;

/**
 *
 * @author djbarbosa
 */
public class FastCheck {

    private static String searchFileToLoad(String date) {
        File folder = new File("./database");
        File[] listOfFiles = folder.listFiles();
        String[] dateFields = date.split("\\-");
        for (File file : listOfFiles) {
            String[] nameFields = file.getName().split("\\,");
            if (nameFields[0].equals("load")) {
                String[] date1 = nameFields[1].split("\\-");
                try {
                    String[] date2 = nameFields[2].split("\\-");
                    System.out.println(date2[2].substring(0, date2[2].length() - 4));
                    if (Integer.valueOf(date1[2]) <= Integer.valueOf(dateFields[2]) && Integer.valueOf(date2[2].substring(0, date2[2].length() - 4)) >= Integer.valueOf(dateFields[2])) {//cambiar a 5
                        if ((Integer.valueOf(date1[1]) <= Integer.valueOf(dateFields[1]) || Integer.valueOf(date1[2])) && Integer.valueOf(date2[1]) >= Integer.valueOf(dateFields[1])) {
                            if (Integer.valueOf(date1[0]) <= Integer.valueOf(dateFields[0]) && Integer.valueOf(date2[0]) > Integer.valueOf(dateFields[0])) {
                                return file.getName();
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Hi!");
                    if (Integer.valueOf(date1[2]) <= Integer.valueOf(dateFields[2])) {
                        if (Integer.valueOf(date1[1]) <= Integer.valueOf(dateFields[1])) {
                            if (Integer.valueOf(date1[0]) <= Integer.valueOf(dateFields[0])) {
                                return file.getName();
                            }
                        }
                    }
                }
            }
        }
        return "NONE";
    }

    public static void main(String[] args) {
        System.out.println(searchFileToLoad("05-05-2017"));
    }

}
