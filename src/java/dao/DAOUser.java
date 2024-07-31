/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import context.DBContext;
import entity.Account;
import entity.Category;
import entity.Product;
import java.beans.Statement;
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
public class DAOUser {

    // Ket noi sql
    Connection conn = null;
    // query sql
    PreparedStatement ps = null;
    // nhan ket qua tra ve
    ResultSet rs = null;
    
    public Account login(String user, String pass) {
        String query = "SELECT * FROM Account WHERE [user] = ? AND [pass] = ?";
        try {
            conn = new DBContext().getConnection(); // Mở kết nối đến SQL Server
            ps = conn.prepareStatement(query);
            ps.setString(1, user);
            ps.setString(2, pass);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Account(
                    rs.getInt("uID"),
                    rs.getString("nameAccount"),
                    rs.getString("user"),
                    rs.getString("pass"),
                    rs.getInt("isSell"),
                    rs.getInt("isAdmin"),
                    Account.Status.fromValue(rs.getString("status")) // Chuyển đổi từ chuỗi thành enum
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                }
        }
        return null;
    }
    
    // check account exits
    public Account checkAccountExist(String user) {
        String query = "SELECT * FROM Account WHERE [user] = ?";
        Account account = null; // Khởi tạo biến để lưu thông tin tài khoản nếu tồn tại

        try {
            conn = new DBContext().getConnection(); // Mở kết nối đến cơ sở dữ liệu
            ps = conn.prepareStatement(query); // Chuẩn bị câu lệnh SQL
            ps.setString(1, user); // Thay thế tham số trong câu lệnh SQL

            rs = ps.executeQuery(); // Thực thi câu lệnh SQL và nhận kết quả

            // Nếu có kết quả trả về
            if (rs.next()) {
                account = new Account(
                    rs.getInt("uID"), // ID của tài khoản
                    rs.getString("nameAccount"), // Tên tài khoản
                    rs.getString("user"), // Tên đăng nhập
                    rs.getString("pass"), // Mật khẩu
                    rs.getInt("isSell"), // Trạng thái bán hàng (0 hoặc 1)
                    rs.getInt("isAdmin"), // Trạng thái quản trị (0 hoặc 1)
                    Account.Status.fromValue(rs.getString("status")) // Trạng thái của tài khoản
                );
            }
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi nếu có ngoại lệ
        } finally {
            // Đóng kết nối và các tài nguyên
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return account; // Trả về tài khoản nếu tồn tại, hoặc null nếu không tìm thấy
    }

    public Account signup(String nameAccount, String user, String pass) {
        // Kiểm tra xem user đã tồn tại chưa
    //    if (checkAccountExist(user) != null) {
    //        System.out.println("User already exists!");
    //        return null;
    //    }

        String query = "INSERT INTO account (nameAccount, [user], [pass], isSell, isAdmin, [status]) VALUES (?, ?, ?, 0, 0, 'isActive')";

        try {
            conn = new DBContext().getConnection(); // Mở kết nối đến cơ sở dữ liệu
            ps = conn.prepareStatement(query); // Chuẩn bị câu lệnh SQL

            // Thiết lập các giá trị cho các tham số của câu lệnh SQL
            ps.setString(1, nameAccount);
            ps.setString(2, user);
            ps.setString(3, pass);

            // Thực thi câu lệnh SQL
            ps.executeUpdate();

            // Trả về đối tượng Account với các giá trị đã được gán
            return new Account(0, nameAccount, user, pass, 0, 0, Account.Status.IS_ACTIVE);
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi nếu có ngoại lệ
        } finally {
            // Đóng kết nối và các tài nguyên
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null; // Trả về null nếu có lỗi hoặc không tạo được tài khoản
    }



    
    public static void main(String[] args) {
        DAOUser daoU = new DAOUser();
        
        String username = "phuong"; // Thay đổi theo tên người dùng cần kiểm tra
        String password = "789";
//        Account ac = daoU.login(username, password);
//        if (ac != null) {
//            System.out.println("Login sussefully");
//        }
        
//        Account ax = daoU.checkAccountExist(username);
//        if (ax != null) {
//            System.out.println("Account exits");
//        }
        
        String acname = "test2";
        String suser = "testsignup2";
       String spass = "111";
        Account at = daoU.signup(acname, suser, spass);
        if (at != null) {
          System.out.println("sign up sussefully");
       }
       
    }

}
