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
    
    Connection conn = null;
    // query sql
    PreparedStatement ps = null;
    // nhan ket qua tra ve
    ResultSet rs = null;
    // Lấy giỏ hàng của người dùng từ cơ sở dữ liệu
    public Cart getCartByUserId(int userId) throws Exception {
        Cart cart = null;
        try (Connection conn = new DBContext().getConnection()) {
            String sql = "SELECT * FROM Cart WHERE user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        cart = new Cart(rs.getInt("id"), rs.getInt("user_id"), rs.getDate("date_created"));
                        cart.setItems(getCartItems(cart.getId(), conn));
                    }
                }
            }
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
            try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, userId);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        cart = new Cart(rs.getInt(1), userId, new java.util.Date());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cart;
    }

    // Thêm hoặc cập nhật sản phẩm trong giỏ hàng
    public void addOrUpdateProductInCart(int cartId, CartItem item) throws Exception {
        try (Connection conn = new DBContext().getConnection()) {
            String sql = "SELECT * FROM CartItem WHERE cart_id = ? AND product_id = ?";
//            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cartId);
            ps.setInt(2, item.getProductId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int quantity = rs.getInt("quantity") + item.getQuantity();
                sql = "UPDATE CartItem SET quantity = ?, price = ?, size = ? WHERE cart_id = ? AND product_id = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, quantity);
                ps.setDouble(2, item.getPrice());
                ps.setString(3, item.getSize());
                ps.setInt(4, cartId);
                ps.setInt(5, item.getProductId());
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

    // Sửa thông tin sản phẩm trong giỏ hàng
    public void updateCartItem(int cartId, CartItem item) throws Exception {
        try (Connection conn = new DBContext().getConnection()) {
            String sql = "UPDATE CartItem SET quantity = ?, price = ?, size = ? WHERE cart_id = ? AND product_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, item.getQuantity());
                ps.setDouble(2, item.getPrice());
                ps.setString(3, item.getSize());
                ps.setInt(4, cartId);
                ps.setInt(5, item.getProductId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa sản phẩm khỏi giỏ hàng
    public void removeCartItem(int cartId, int productId) throws Exception {
        try (Connection conn = new DBContext().getConnection()) {
            String sql = "DELETE FROM CartItem WHERE cart_id = ? AND product_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, cartId);
                ps.setInt(2, productId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tăng số lượng sản phẩm trong giỏ hàng
    public void incrementProductQuantity(int cartId, int productId) throws Exception {
        try (Connection conn = new DBContext().getConnection()) {
            String sql = "UPDATE CartItem SET quantity = quantity + 1 WHERE cart_id = ? AND product_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, cartId);
                ps.setInt(2, productId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Giảm số lượng sản phẩm trong giỏ hàng hoặc xóa nếu số lượng bằng 0
    public void decrementProductQuantity(int cartId, int productId) throws Exception {
        try (Connection conn = new DBContext().getConnection()) {
            // Kiểm tra số lượng hiện tại
            String sql = "SELECT quantity FROM CartItem WHERE cart_id = ? AND product_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int currentQuantity = rs.getInt("quantity");
                if (currentQuantity > 1) {
                    // Giảm số lượng sản phẩm
                    sql = "UPDATE CartItem SET quantity = quantity - 1 WHERE cart_id = ? AND product_id = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, cartId);
                    ps.setInt(2, productId);
                    ps.executeUpdate();
                } else {
                    // Xóa sản phẩm nếu số lượng bằng 0 hoặc 1
                    sql = "DELETE FROM CartItem WHERE cart_id = ? AND product_id = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, cartId);
                    ps.setInt(2, productId);
                    ps.executeUpdate();
                }
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
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, cartId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        int productId = rs.getInt("product_id");
                        double price = rs.getDouble("price");
                        int quantity = rs.getInt("quantity");
                        String size = rs.getString("size");
                        CartItem item = new CartItem(id, productId, price, quantity, size);
                        items.put(productId, item);
                    }
                }
            }
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
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        total = rs.getInt("total");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    
    public double getTotalCartPrice(int cartId) throws Exception {
        double total = 0;
        try (Connection conn = new DBContext().getConnection()) {
            String sql = "SELECT SUM(price * quantity) AS total FROM CartItem WHERE cart_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, cartId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        total = rs.getDouble("total");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    
    public static void main(String[] args) {
        try {
            DAOCart daoCart = new DAOCart();

            // Tạo giỏ hàng mới cho userId = 1
            Cart newCart = daoCart.createCart(1);
            int cartId = newCart.getId();

            // Thêm sản phẩm vào giỏ hàng
            CartItem item1 = new CartItem(0, 1, 15.0, 2, "L");
            daoCart.addOrUpdateProductInCart(cartId, item1);
            CartItem item2 = new CartItem(0, 2, 20.0, 1, "M");
            daoCart.addOrUpdateProductInCart(cartId, item2);
            System.out.println("Added items: " + item1 + ", " + item2);

            // Cập nhật sản phẩm trong giỏ hàng
            item1.setQuantity(5);
            item1.setPrice(14.99);
            daoCart.updateCartItem(cartId, item1);
            System.out.println("Updated item: " + item1);

            // Tính tổng giá tiền của giỏ hàng
            double totalPrice = daoCart.getTotalCartPrice(cartId);
            System.out.println("Total cart price: " + totalPrice);

            // Tăng số lượng sản phẩm
            daoCart.incrementProductQuantity(cartId, 1);
            System.out.println("Incremented quantity of product 1");

            // Giảm số lượng sản phẩm
            daoCart.decrementProductQuantity(cartId, 1);
            System.out.println("Decremented quantity of product 1");

            // Xóa sản phẩm khỏi giỏ hàng
            daoCart.removeCartItem(cartId, 2);
            System.out.println("Removed product with id 2 from cart");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
