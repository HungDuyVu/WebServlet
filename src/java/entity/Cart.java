/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author Admin
 */
public class Cart {
    private int id;
    private int userId;
    private Date dateCreated;
    private Map<Integer, CartItem> items; // Sử dụng Map để lưu trữ các sản phẩm trong giỏ hàng, với khóa là productId

    public Cart() {
        items = new HashMap<>();
    }

    public Cart(int id, int userId, Date dateCreated) {
        this.id = id;
        this.userId = userId;
        this.dateCreated = dateCreated;
        this.items = new HashMap<>();
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Map<Integer, CartItem> getItems() {
        return items;
    }

    public void setItems(Map<Integer, CartItem> items) {
        this.items = items;
    }

    // Thêm sản phẩm vào giỏ hàng
    public void addItem(CartItem item) {
        int productId = item.getProductId();
        if (items.containsKey(productId)) {
            CartItem existingItem = items.get(productId);
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
        } else {
            items.put(productId, item);
        }
    }

    // Tính tổng số lượng sản phẩm trong giỏ hàng
    public int getTotalQuantity() {
        int total = 0;
        for (CartItem item : items.values()) {
            total += item.getQuantity();
        }
        return total;
    }

    // Tính tổng giá trị giỏ hàng
    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : items.values()) {
            total += item.getQuantity() * item.getPrice();
        }
        return total;
    }

    
}
