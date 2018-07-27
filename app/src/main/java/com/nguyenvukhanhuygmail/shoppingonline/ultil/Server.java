package com.nguyenvukhanhuygmail.shoppingonline.ultil;

/**
 * Created by toannq on 11/5/2017.
 */

public class Server {
    public static final String mIP = "192.168.0.101";
    public static final String all_product_path = "http://" + mIP + "/server/getAllProduct.php";
    public static final String category_path = "http://" + mIP + "/server/getCategoryProduct.php";
    public static final String adv_path = "http://" + mIP + "/server/getAdvs.php";
    public static final String new_product_path = "http://" + mIP + "/server/getNewProduct.php";
    public static final String popular_product_path = "http://" + mIP + "/server/getPopularProduct.php";
    public static final String rate_product_path = "http://" + mIP + "/server/getBestRateProduct.php";
//    public static final String new_product_event = "new_product";
//    public static final String popular_product_event = "popular_product";
//    public static final String rate_product_event = "best_rate_product";
    public static final String main_product_path = "http://" + mIP + "/server/getProductByCategory.php?page=";
}
