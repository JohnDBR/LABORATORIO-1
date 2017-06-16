/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author djbarbosa
 */
public class FastCheck {

    public static void main(String[] args) {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher("I am a string");
        boolean b = m.find();

        if (b) {
            System.out.println("There is a special character in my string");
        }else{
            System.out.println("There is NO special character in my string");
        }
    }

}
