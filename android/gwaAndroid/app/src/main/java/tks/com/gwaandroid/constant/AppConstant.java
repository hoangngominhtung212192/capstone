package tks.com.gwaandroid.constant;

/**
 * Created by Tung Hoang Ngo Minh on 11/14/2018.
 */

public class AppConstant {

    public static final String checkEmail = "^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";

    public static final String checkString = "[A-Za-z\\s]+";

    public static final String checkUsername = "[A-Za-z]+";
    public static final String BASE_URL = "http://192.168.1.5:8080/gwa/";
    public static final String HOST_NAME = "192.168.1.5";

    //SortType
    public static final int SORT_DATE_DESC = 1;
    public static final int SORT_DATE_ASC = 2;
    public static final int SORT_PRICE_DESC = 4;
    public static final int SORT_PRICE_ASC = 3;
    //TradeType
    public static final String TYPE_SELL = "sell";
    public static final String TYPE_BUY = "buy";
    public static final String TYPE_ALL = "all";
    public static final int INT_TYPE_BUY = 2;
    public static final int INT_TYPE_SELL = 1;

    //Trade Condition
    public static final int INT_TRADE_CONDITION_NEW = 1;
    public static final int INT_TRADE_CONDITION_USED = 2;
}
