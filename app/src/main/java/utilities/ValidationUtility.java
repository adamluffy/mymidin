package utilities;

import android.support.design.widget.TextInputLayout;

import java.util.regex.Pattern;

public class ValidationUtility {


    public static boolean validatePrice(TextInputLayout priceInputLayout, String price){

        if(price.isEmpty()){
            priceInputLayout.setError("Please input product price");
            return false;
        }else if(!isDouble(price)) {
            priceInputLayout.setError("Please input numerical price");
            return false;
        }else {
            priceInputLayout.setErrorEnabled(false);
        }

        return true;
    }


    public static boolean validateEmail(TextInputLayout emailInputLayout, String email){
        return true;
    }

    public static boolean validateStrongPassword(TextInputLayout passwordInputLayout,TextInputLayout confirmPwdInputLayout ,String password, String confirmPwd){

        /*
            1. password must contain at least one uppercase,one lowercase, one numeric
            2. password must be at least 8-24 character
            3. password field must also match confirm password field
        */

        if(password.isEmpty()){
            passwordInputLayout.setError("Please input your password");
            return false;
        }else if(!Pattern.matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20})",password)){
            passwordInputLayout.setError("Password must be at least one uppercase, one lowercase, one numeric, minimum 8 character");
            return false;
        }else if(!password.equals(confirmPwd)){
            confirmPwdInputLayout.setError("The password enter is not match with the above");
            return false;
        }else {
            passwordInputLayout.setErrorEnabled(false);
            confirmPwdInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    public static boolean validateQuantity(TextInputLayout qtyInputLayout, String quantity){

        if(quantity.isEmpty()){
            qtyInputLayout.setError("Please input product quantity");
            return false;
        }else if(!Pattern.matches("\\d+",quantity)){
            qtyInputLayout.setError("Please input a numeric quantity");
            return false;
        }else {
            qtyInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    public static boolean validateIdentityNumber(TextInputLayout icInputLayout, String ic){
        return true;
    }

    public static boolean validateDate(TextInputLayout dateInputLayout, String date){

        if(date.isEmpty()){
            dateInputLayout.setError("Please Input Date");
            return false;
        }else if(!Pattern.matches("\\d{2}/\\d{2}/\\d{4}",date)){
            dateInputLayout.setError("Please Input a valid date");
            return false;
        }

        return true;

    }

    public static boolean validateString(TextInputLayout inputLayout, String input){

        if(input.isEmpty()){
            inputLayout.setError("Invalid input");
            return false;
        }else{
            inputLayout.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isDouble(String price){
        try {
            double d = Double.parseDouble(price);
        }catch (NumberFormatException | NullPointerException e){
            return false;
        }

        return true;
    }

}
