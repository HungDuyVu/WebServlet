/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author OS
 */
import java.util.List;

public class Product {
    private int id;
    private String name;
    private double price;
    private String title;
    private String description;
    private int cateID; // ID của danh mục
    private int sellID; // ID của người bán
    private List<String> sizes; // Danh sách kích thước
    private List<String> imageUrls; // Danh sách URL hình ảnh

    // Constructor
    public Product(int id, String name, double price, String title, String description, int cateID, int sellID, List<String> sizes, List<String> imageUrls) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.title = title;
        this.description = description;
        this.cateID = cateID;
        this.sellID = sellID;
        this.sizes = sizes;
        this.imageUrls = imageUrls;
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCateID() {
        return cateID;
    }

    public void setCateID(int cateID) {
        this.cateID = cateID;
    }

    public int getSellID() {
        return sellID;
    }

    public void setSellID(int sellID) {
        this.sellID = sellID;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", name=" + name + ", price=" + price + ", title=" + title + ", description=" + description + ", cateID=" + cateID + ", sellID=" + sellID + ", sizes=" + sizes + ", imageUrls=" + imageUrls + '}';
    }
    
    
}
