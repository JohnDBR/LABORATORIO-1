/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author djbarbosa
 */
public abstract class Validation {

    //VALIDATIONS
    public boolean validateNonSpecialCharacters(String string) {
        if (!string.isEmpty()) {
            Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(string);
            return m.find();
        }
        return false;
    }

    public boolean validateFloat(String string) {
        try {
            float fl = Float.valueOf(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateInt(String string) {
        try {
            int integer = Integer.valueOf(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateWord(String string) {
        if (!string.isEmpty()) {
            return string.chars().allMatch(Character::isLetter);
        }
        return false;
    }

    public boolean validateNumber(String string) {
        if (!string.isEmpty()) {
            return string.chars().allMatch(Character::isDigit);
        }
        return false;
    }

    public boolean validateLength(String s, int range) {
        if (!s.isEmpty()) {
            if (s.length() >= range) {
                return true;
            }
        }
        return false;
    }

    public boolean validateCellphone(String string) {
        if (validateNumber(string) && validateLength(string, 10)) {
            return true;
        }
        return false;
    }

    public boolean validateEmailFormat(String string) {
        if (string.contains("@")) {
            return true;
        }
        return false;
    }

    public boolean validateDateFormat(String string) {
        try {
            String[] fields = string.split("\\-");
            if (fields.length != 3) {
                return false;
            }
            if (fields[0].length() != 2  || fields[1].length() != 2 || fields[2].length() != 4) {
                return false;
            }
            if (!validateInt(fields[0]) || !validateInt(fields[1]) || !validateInt(fields[2])) {
                return false;
            }
            
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
