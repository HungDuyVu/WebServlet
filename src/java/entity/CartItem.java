/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

public class CartItem {
    private int id;
    private int productId;
    private double price;
    private int quantity;
    private String size;
    private int cartId; 

    public CartItem(int id, int productId, double price, int quantity, String size, int cartId) {
        this.id = id;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.size = size;
        this.cartId = cartId;
    }

    public CartItem(int productId, double price, int quantity, String size, int cartId) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.size = size;
        this.cartId = cartId;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSize() {
        return size;
    }

    public int getCartId() {
        return cartId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
}


