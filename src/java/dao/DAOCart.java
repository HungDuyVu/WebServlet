package dao;

import context.DBContext;
import entity.Cart;
import entity.CartItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DAOCart {

    // Lấy giỏ hàng của người dùng từ cơ sở dữ liệu
    public Cart getCartByUserId(int userId) throws Exception {
        Cart cart = null;
        try (Connection conn = new DBContext().getConnection()) {
            String sql = "SELECT * FROM Cart WHERE user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cart = new Cart(rs.getInt("id"), rs.getInt("user_id"), rs.getDate("date_created"));
                cart.setItems(getCartItems(cart.getId(), conn));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cart;
    }

    // Tạo giỏ hàng mới cho người dùng
    public Cart createCart(int userId) throws Exception {
        Cart cart = null;
        try (Connection conn = new DBContext().getConnection()) {
            String sql = "INSERT INTO Cart (user_id) VALUES (?)";
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, userId);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                cart = new Cart(rs.getInt(1), userId, new java.util.Date());
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cart;
    }

    // Thêm sản phẩm vào giỏ hàng
    public void addProductToCart(int cartId, CartItem item) throws Exception {
        try (Connection conn = new DBContext().getConnection()) {
            String sql = "SELECT * FROM CartItem WHERE cart_id = ? AND product_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cartId);
            ps.setInt(2, item.getProductId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int quantity = rs.getInt("quantity") + item.getQuantity();
                sql = "UPDATE CartItem SET quantity = ? WHERE cart_id = ? AND product_id = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, quantity);
                ps.setInt(2, cartId);
                ps.setInt(3, item.getProductId());
                ps.executeUpdate();
            } else {
                sql = "INSERT INTO CartItem (cart_id, product_id, price, quantity, size) VALUES (?, ?, ?, ?, ?)";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, cartId);
                ps.setInt(2, item.getProductId());
                ps.setDouble(3, item.getPrice());
                ps.setInt(4, item.getQuantity());
                ps.setString(5, item.getSize());
                ps.executeUpdate();
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy danh sách sản phẩm trong giỏ hàng
    private Map<Integer, CartItem> getCartItems(int cartId, Connection conn) {
        Map<Integer, CartItem> items = new HashMap<>();
        try {
            String sql = "SELECT * FROM CartItem WHERE cart_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int productId = rs.getInt("product_id");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String size = rs.getString("size");
                CartItem item = new CartItem(id, productId, price, quantity, size);
                items.put(productId, item);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    // Lấy số lượng sản phẩm trong giỏ hàng
    public int getCartSize(int cartId) throws Exception {
        int total = 0;
        try (Connection conn = new DBContext().getConnection()) {
            String sql = "SELECT SUM(quantity) AS total FROM CartItem WHERE cart_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, cartId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    total = rs.getInt("total");
                }
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
}
