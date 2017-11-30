package com.nguyenvukhanhuygmail.shoppingonline.ultil;

/**
 * Created by toannq on 11/5/2017.
 */

public class Server {
    private static final String mIP = "192.168.229.1";
    public static final String category_path = "http://" + mIP + "/server/getCategoryProduct.php";
    public static final String adv_path = "http://" + mIP + "/server/getAdvs.php";
    public static final String new_product_path = "http://" + mIP + "/server/getNewProduct.php";
    public static final String popular_product_path = "http://" + mIP + "/server/getPopularProduct.php";
    public static final String sale_product_path = "http://" + mIP + "/server/getSaleProduct.php";
    public static final String main_product_path = "http://" + mIP + "/server/getProductByCategory.php?page=";
}
