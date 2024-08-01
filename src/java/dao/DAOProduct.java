/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import context.DBContext;
import entity.Category;
import entity.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author OS
 */
public class DAOProduct {
      // Ket noi sql
    Connection conn = null;
    // query sql
    PreparedStatement ps = null;
    // nhan ket qua tra ve
    ResultSet rs = null;
    
    // GET ALL PRODUCT -> list 
    public List<Product> getAllProduct() {
        List<Product> list = new ArrayList<>();
        String queryProduct = "SELECT * FROM Product";
        String queryImages = "SELECT url FROM ProductImage WHERE product_id = ?";
        
        try {
            conn = new DBContext().getConnection(); // Mở kết nối đến SQL Server
            ps = conn.prepareStatement(queryProduct);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                int productId = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String sizeString = rs.getString("size");
                int cateID = rs.getInt("cateID");
                int sellID = rs.getInt("sell_ID");
                
                // Tách các kích thước từ chuỗi
                List<String> sizes = new ArrayList<>();
                if (sizeString != null && !sizeString.isEmpty()) {
                    String[] sizeArray = sizeString.split(", ");
                    for (String size : sizeArray) {
                        sizes.add(size);
                    }
                }
                
                // Lấy tất cả các hình ảnh của sản phẩm
                List<String> imageUrls = new ArrayList<>();
                PreparedStatement psImages = conn.prepareStatement(queryImages);
                psImages.setInt(1, productId);
                ResultSet rsImages = psImages.executeQuery();
                while (rsImages.next()) {
                    imageUrls.add(rsImages.getString("url"));
                }
                
                // Thêm sản phẩm vào danh sách
                list.add(new Product(productId, name, price, title, description, cateID, sellID, sizes, imageUrls));
                
                rsImages.close();
                psImages.close();
            }
            
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // GET ALL CATEGORY
    public List<Category> getAllCategory() {
        List<Category> list = new ArrayList<>();
        String query = "SELECT * FROM Category";

        try {
            conn = new DBContext().getConnection(); // Mở kết nối đến SQL Server
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("cid");
                String name = rs.getString("cname");

                // Thêm đối tượng Category vào danh sách
                list.add(new Category(id, name));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    
    // GET LIST PRODUCT WITH cateID -> list Product
    public List<Product> getProductByCID(String cateID) {
        List<Product> list = new ArrayList<>();
        String queryProduct = "SELECT * FROM Product WHERE cateID = ?";
        String queryImages = "SELECT url FROM ProductImage WHERE product_id = ?";

        try {
            conn = new DBContext().getConnection(); // Mở kết nối đến SQL Server
            ps = conn.prepareStatement(queryProduct);
            ps.setString(1, cateID); // Sử dụng cateID dưới dạng String
            rs = ps.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String sizeString = rs.getString("size");
                int categoryId = rs.getInt("cateID");
                int sellID = rs.getInt("sell_ID");

                // Tách các kích thước từ chuỗi
                List<String> sizes = new ArrayList<>();
                if (sizeString != null && !sizeString.isEmpty()) {
                    String[] sizeArray = sizeString.split(", ");
                    for (String size : sizeArray) {
                        sizes.add(size);
                    }
                }

                // Lấy tất cả các hình ảnh của sản phẩm
                List<String> imageUrls = new ArrayList<>();
                PreparedStatement psImages = conn.prepareStatement(queryImages);
                psImages.setInt(1, productId);
                ResultSet rsImages = psImages.executeQuery();
                while (rsImages.next()) {
                    imageUrls.add(rsImages.getString("url"));
                }

                // Thêm sản phẩm vào danh sách
                list.add(new Product(productId, name, price, title, description, categoryId, sellID, sizes, imageUrls));

                rsImages.close();
                psImages.close();
            }

                rs.close();
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
        }
        return list;
    }
    
    // GET  PRODUCT WITH id ->  Product
    public Product getProductByID(String id) {
        String queryProduct = "SELECT * FROM Product WHERE id = ?";
        String queryImages = "SELECT url FROM ProductImage WHERE product_id = ?";
        Product product = null;

        try {
            conn = new DBContext().getConnection(); // Mở kết nối đến SQL Server
            ps = conn.prepareStatement(queryProduct);
            ps.setString(1, id); // Sử dụng id dưới dạng String
            rs = ps.executeQuery();

            if (rs.next()) {
                int productId = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String sizeString = rs.getString("size");
                int categoryId = rs.getInt("cateID");
                int sellID = rs.getInt("sell_ID");

                // Tách các kích thước từ chuỗi
                List<String> sizes = new ArrayList<>();
                if (sizeString != null && !sizeString.isEmpty()) {
                    String[] sizeArray = sizeString.split(", ");
                    for (String size : sizeArray) {
                        sizes.add(size);
                    }
                }

                // Lấy tất cả các hình ảnh của sản phẩm
                List<String> imageUrls = new ArrayList<>();
                PreparedStatement psImages = conn.prepareStatement(queryImages);
                psImages.setInt(1, productId);
                ResultSet rsImages = psImages.executeQuery();
                while (rsImages.next()) {
                    imageUrls.add(rsImages.getString("url"));
                }

            // Tạo đối tượng Product với các thông tin thu được
            product = new Product(productId, name, price, title, description, categoryId, sellID, sizes, imageUrls);
            
            rsImages.close();
            psImages.close();
        }
        
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return product;
    }

    // GET PRODUCT BY sell_id -> list Product
    public List<Product> getProductBySellID(int sellID) {
        List<Product> list = new ArrayList<>();
        String queryProduct = "SELECT * FROM Product WHERE sell_ID = ?";
        String queryImages = "SELECT url FROM ProductImage WHERE product_id = ?";

        try {
            conn = new DBContext().getConnection(); // Mở kết nối đến SQL Server
            ps = conn.prepareStatement(queryProduct);
            ps.setInt(1, sellID);
            rs = ps.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String sizeString = rs.getString("size");
                int categoryId = rs.getInt("cateID");
                int sellIDFromDb = rs.getInt("sell_ID");

                // Tách các kích thước từ chuỗi
                List<String> sizes = new ArrayList<>();
                if (sizeString != null && !sizeString.isEmpty()) {
                    String[] sizeArray = sizeString.split(", ");
                    for (String size : sizeArray) {
                        sizes.add(size);
                    }
                }

                // Lấy tất cả các hình ảnh của sản phẩm
                List<String> imageUrls = new ArrayList<>();
                PreparedStatement psImages = conn.prepareStatement(queryImages);
                psImages.setInt(1, productId);
                ResultSet rsImages = psImages.executeQuery();
                while (rsImages.next()) {
                    imageUrls.add(rsImages.getString("url"));
                }

                // Thêm sản phẩm vào danh sách
                list.add(new Product(productId, name, price, title, description, categoryId, sellIDFromDb, sizes, imageUrls));

                rsImages.close();
                psImages.close();
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
    // Search product by name -> list product
    public List<Product> searchByName(String txtSearch) {
        List<Product> list = new ArrayList<>();
        String queryProduct = "SELECT * FROM Product WHERE [name] LIKE ?";
        String queryImages = "SELECT url FROM ProductImage WHERE product_id = ?";

        try {
            conn = new DBContext().getConnection(); // Mở kết nối đến SQL Server
            ps = conn.prepareStatement(queryProduct);
            ps.setString(1, "%" + txtSearch + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String sizeString = rs.getString("size");
                int categoryId = rs.getInt("cateID");
                int sellIDFromDb = rs.getInt("sell_ID");

                // Tách các kích thước từ chuỗi
                List<String> sizes = new ArrayList<>();
                if (sizeString != null && !sizeString.isEmpty()) {
                    String[] sizeArray = sizeString.split(", ");
                    for (String size : sizeArray) {
                        sizes.add(size);
                    }
                }

                // Lấy tất cả các hình ảnh của sản phẩm
                List<String> imageUrls = new ArrayList<>();
                PreparedStatement psImages = conn.prepareStatement(queryImages);
                psImages.setInt(1, productId);
                ResultSet rsImages = psImages.executeQuery();
                while (rsImages.next()) {
                    imageUrls.add(rsImages.getString("url"));
                }

                // Thêm sản phẩm vào danh sách
                list.add(new Product(productId, name, price, title, description, categoryId, sellIDFromDb, sizes, imageUrls));

                rsImages.close();
                psImages.close();
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
//    frontend
//    servlet
//    dao
//    db
    
    // GET PRODUCT WIT ID LAST
    public Product getLast() {
        String queryProduct = "SELECT TOP 1 * FROM Product ORDER BY id DESC";
        String queryImages = "SELECT url FROM ProductImage WHERE product_id = ?";

        try {
            conn = new DBContext().getConnection(); // Mở kết nối đến SQL Server
            ps = conn.prepareStatement(queryProduct);
            rs = ps.executeQuery();

            if (rs.next()) {
                int productId = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String sizeString = rs.getString("size");
                int cateID = rs.getInt("cateID");
                int sellID = rs.getInt("sell_ID");

                // Tách các kích thước từ chuỗi
                List<String> sizes = new ArrayList<>();
                if (sizeString != null && !sizeString.isEmpty()) {
                    String[] sizeArray = sizeString.split(", ");
                    for (String size : sizeArray) {
                        sizes.add(size);
                    }
                }

                // Lấy tất cả các hình ảnh của sản phẩm
                List<String> imageUrls = new ArrayList<>();
                PreparedStatement psImages = conn.prepareStatement(queryImages);
                psImages.setInt(1, productId);
                ResultSet rsImages = psImages.executeQuery();
                while (rsImages.next()) {
                    imageUrls.add(rsImages.getString("url"));
                }

                rsImages.close();
                psImages.close();

                // Trả về đối tượng Product
                return new Product(productId, name, price, title, description, cateID, sellID, sizes, imageUrls);
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Insert into Product and Images
    public void insertProduct(String name, String price, String title, 
                          String description, String category, int sid, 
                          List<String> sizes, List<String> images) {
        String queryProduct = "INSERT INTO Product (name, price, title, description, cateID, sell_ID, size) " +
                              "VALUES (?, ?, ?, ?, ?, ?, ?)";
        String queryImage = "INSERT INTO ProductImage (product_id, url) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection(); // Mở kết nối đến SQL Server

            // Bắt đầu transaction
            conn.setAutoCommit(false);

            // Chèn sản phẩm
            ps = conn.prepareStatement(queryProduct, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, price);
            ps.setString(3, title);
            ps.setString(4, description);
            ps.setString(5, category);
            ps.setInt(6, sid);

            // Xử lý kích thước
            String sizeString = sizes != null ? String.join(", ", sizes) : "";
            ps.setString(7, sizeString);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                // Lấy ID của sản phẩm vừa chèn
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int productId = rs.getInt(1);

                    // Chèn hình ảnh liên quan đến sản phẩm
                    if (images != null && !images.isEmpty()) {
                        ps = conn.prepareStatement(queryImage);
                        for (String imageUrl : images) {
                            ps.setInt(1, productId);
                            ps.setString(2, imageUrl);
                            ps.addBatch(); // Thêm vào batch
                        }
                        ps.executeBatch(); // Thực thi batch
                    }
                }
            }

            // Commit transaction
            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback transaction nếu có lỗi
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        } finally {
            // Đóng tài nguyên
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
    
    // EDIT PRODUCT BY ID
   public void editProduct(String name, String price, String title,
                            String description, String category, String pid,
                            List<String> sizes, List<String> images) {
        String queryProduct = "UPDATE product SET name = ?, price = ?, title = ?, description = ?, cateID = ?, size = ? WHERE id = ?";
        String queryDeleteImages = "DELETE FROM ProductImage WHERE product_id = ?";
        String queryInsertImage = "INSERT INTO ProductImage (product_id, url) VALUES (?, ?)";

        try {
            conn = new DBContext().getConnection(); // Mở kết nối đến SQL Server
            conn.setAutoCommit(false); // Bắt đầu giao dịch

            // Cập nhật thông tin sản phẩm
            ps = conn.prepareStatement(queryProduct);
            ps.setString(1, name);
            ps.setString(2, price);
            ps.setString(3, title);
            ps.setString(4, description);
            ps.setString(5, category);
            ps.setString(6, String.join(", ", sizes)); // Chuyển danh sách kích thước thành chuỗi
            ps.setString(7, pid);
            ps.executeUpdate();

            // Xóa các hình ảnh cũ
            PreparedStatement psDeleteImages = conn.prepareStatement(queryDeleteImages);
            psDeleteImages.setString(1, pid);
            psDeleteImages.executeUpdate();

            // Thêm các hình ảnh mới
            if (images != null && !images.isEmpty()) {
                ps = conn.prepareStatement(queryInsertImage);
                for (String image : images) {
                    ps.setString(1, pid);
                    ps.setString(2, image);
                    ps.addBatch(); // Thêm vào batch
                }
                ps.executeBatch(); // Thực hiện batch
            }

            conn.commit(); // Xác nhận giao dịch
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); // Rollback nếu có lỗi
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

   // DELETE PRODUCT AND IMAGE
    public void deleteProduct(String pid) {
        String queryDeleteImages = "DELETE FROM ProductImage WHERE product_id = ?";
        String queryDeleteProduct = "DELETE FROM Product WHERE id = ?";

        try {
            conn = new DBContext().getConnection(); // Mở kết nối đến SQL Server
            conn.setAutoCommit(false); // Bắt đầu giao dịch

            // Xóa tất cả hình ảnh của sản phẩm
            PreparedStatement psDeleteImages = conn.prepareStatement(queryDeleteImages);
            psDeleteImages.setString(1, pid);
            psDeleteImages.executeUpdate();

            // Xóa sản phẩm
            PreparedStatement psDeleteProduct = conn.prepareStatement(queryDeleteProduct);
            psDeleteProduct.setString(1, pid);
            psDeleteProduct.executeUpdate();

            conn.commit(); // Xác nhận giao dịch
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); // Rollback nếu có lỗi
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } 
    }


    
    public static void main(String[] args) {
        DAOProduct daoP = new DAOProduct();
        List<Product> listP = daoP.getAllProduct();
        List<Product> listCid = daoP.getProductByCID("1");
        Product p = daoP.getProductByID("2");
        List<Product> listS = daoP.getProductBySellID(3);
        List<Product> listSearch = daoP.searchByName("NIKE");
        List<Category> listC = daoP.getAllCategory();
//        System.out.println(p);
//        for (Category o : listC) {
//           System.out.println(o);
//       }
//         System.out.println(daoP.getLast());

         // Thông tin sản phẩm
//        String name = "Test insert";
//        String price = "11.99";
//        String title = "High Comfort and Durability";
//        String description = "Ideal for running and sports activities. Available in various sizes.";
//        String category = "2"; 
//        int sellID = 1; 
//
//        // Danh sách kích thước
//        List<String> sizes = new ArrayList<>();
//        sizes.add("36");
//        sizes.add("37");
//        sizes.add("38");
//        sizes.add("39");
//        sizes.add("40");
//        sizes.add("41");
//        sizes.add("42");
//
//        // Danh sách hình ảnh
//        List<String> images = new ArrayList<>();
//        images.add("http://example.com/image1.jpg");
//        images.add("http://example.com/image2.jpg");
//        images.add("http://example.com/image3.jpg");
//        
//        
//        daoP.insertProduct(name, price, title, description, category, sellID, sizes, images);
//        Product Pi = daoP.getProductByID("21");
        
        String pid = "21"; // ID của sản phẩm cần sửa
        String name = "Updated Product Name";
        String price = "19.99";
        String title = "Updated Title";
        String description = "Updated Description";
        String category = "3"; // Thay đổi phù hợp với giá trị cateID thực tế trong cơ sở dữ liệu
        
        // Danh sách kích thước mới
        List<String> sizes = new ArrayList<>();
        sizes.add("44");
        sizes.add("45");
        
        // Danh sách hình ảnh mới
        List<String> images = new ArrayList<>();
        images.add("http://example.com/new_image1.jpg");
        images.add("http://example.com/new_image2.jpg");
//        daoP.editProduct(name, price, title, description, category, pid, sizes, images);
//        System.out.println("Product updated successfully!");
        
        daoP.deleteProduct("21");
        System.out.println("Product Delete successfully!");
    }
}
