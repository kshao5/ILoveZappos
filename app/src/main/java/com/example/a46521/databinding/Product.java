package com.example.a46521.databinding;

/**
 * Created by 46521 on 2017/1/24.
 */

public class Product {
    private  String originalPrice;
    private  String  brandName;
    private String productName;
    private String productURL;
    private String pictureURL;

    public Product(String originalPrice, String brandName, String productName, String productURL, String pictureURL) {
        this.originalPrice = originalPrice;
        this.brandName = brandName;
        this.productName = productName;
        this.productURL = productURL;
        this.pictureURL = pictureURL;
    }

    public String getProductURL(){
        return this.productURL;
    }

    public String getProductName(){
        return this.productName;
    }

    public String getOriginalPrice(){
        return this.originalPrice;
    }

    public String getBrandName(){
        return this.brandName;
    }

    public String getPictureURL(){
        return this.pictureURL;
    }

    public void setOriginalPrice(String currPrice){
        this.originalPrice = currPrice;
    }

    public void setBrandName(String currBrand){
        this.brandName = currBrand;
    }

    public void setProductName(String currProductName){
        this.productName = currProductName;
    }

    public void setProductURL(String currProductURL){
        this.productURL = currProductURL;
    }

    public void setPictureURL(String currPictureURL){
        this.pictureURL = currPictureURL;
    }


}
