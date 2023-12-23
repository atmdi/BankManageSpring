package ir.java.up.util;

import org.springframework.data.jpa.support.PageableUtils;

import java.time.Year;

public class AccountUtils {

    public  static  final  String ACCOUNT_EXISTS_CODE="001";

    public  static final  String ACCOUNT_EXISTS_MESSAGE="THIS user already has an account created";

    public static final String ACCOUNT_CREATION_SUCCESS="002";
    public  static  final  String ACCOUNT_CREATION_MESSAGE="ACCOUNT has been successfully created";

    public static final String EMAIL_SEND_SUCCESS="003";

    public static final String EMAIL_SEND_MESSAGE="EMAIL HAS EXEPTION AND NOT SUCCESSFULL";

    public static final String ACCOUNT_NOT_EXIST="004";

    public static final String ACCOUNT_NOT_EXIST_MESSAGE="ACCOUNT NUMBER NOT EXIST";

    public  static  final String ACCOUNT_FOUND_CODE="005";

    public  static  final String ACCOUNT_FOUND_SUCCESS="ACCOUNT NUMBER FOUND";

    public static  final String SUCCESS="SUCCESS";

    public static final String CODE_SUCCESS="006";

    public static  final String FAIL="FAILED";

    public static final String CODE_FAIL="007";



    public static String generateAccountNumber(){

        Year currentYear=Year.now();
    int min=100000;
    int max=999999;

    int randNumber=(int) Math.floor(Math.random()*(max - min +1)+min);

    String year=String.valueOf(currentYear);
    String randomNumber=String.valueOf(randNumber);

    StringBuilder accountNumber=new StringBuilder();

    return  accountNumber.append(year).append(randomNumber).toString();

    }
}
