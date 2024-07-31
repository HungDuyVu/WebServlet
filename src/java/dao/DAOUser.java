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
import java.util.Date;
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
            Account.Status status = Account.Status.fromValue(rs.getString("status"));
            if (status == Account.Status.IS_ACTIVE) {
                return new Account(
                    rs.getInt("uID"),
                    rs.getString("nameAccount"),
                    rs.getString("user"),
                    rs.getString("pass"),
                    rs.getInt("isSell"),
                    rs.getInt("isAdmin"),
                    status, // Chuyển đổi từ chuỗi thành enum
                    rs.getDate("isDelete")
                );
            } else {
                System.out.println("Account is not active");
            }
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
                    Account.Status.fromValue(rs.getString("status")), // Trạng thái của tài khoản
                    rs.getDate("isDelete")
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
            return new Account(0, nameAccount, user, pass, 0, 0, Account.Status.IS_ACTIVE, null);
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
    
    // GET ALL ACCOUNT -> lIST ACCOUNT
     public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT uID, nameAccount, [user], isSell, isAdmin, [status] FROM Account";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                Account account = new Account();
                account.setId(rs.getInt("uID"));
                account.setNameAccount(rs.getString("nameAccount"));
                account.setUser(rs.getString("user"));
                account.setIsSell(rs.getInt("isSell"));
                account.setIsAdmin(rs.getInt("isAdmin"));

                // Chuyển đổi giá trị của status từ chuỗi sang enum
                String statusStr = rs.getString("status");
                try {
                    account.setStatus(Account.Status.valueOf(statusStr));
                } catch (IllegalArgumentException e) {
                    // Xử lý giá trị không hợp lệ
                    System.err.println("Invalid status value: " + statusStr);
                    account.setStatus(Account.Status.IS_ACTIVE); // Hoặc giá trị mặc định nào đó
                }

                accounts.add(account);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accounts;
    }

     // Change isSell
    public void changeRoleSell(String userId, String isSell) {
        String query = "UPDATE Account SET isSell = ? WHERE uID = ?";
        
        try {
            conn = new DBContext().getConnection(); // Mở kết nối đến cơ sở dữ liệu
            ps = conn.prepareStatement(query);
            
            // Set giá trị cho các tham số
            ps.setString(1, isSell);
            ps.setString(2, userId);
            
            // Thực hiện câu lệnh cập nhật
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    
     public void changeRoleAdmin(String userId, String newIsAdmin) {
        String query = "UPDATE Account SET isAdmin = ? WHERE uID = ?";
        
        try {
            conn = new DBContext().getConnection(); // Mở kết nối đến SQL Server
            ps = conn.prepareStatement(query);

            // Cập nhật giá trị isAdmin
            ps.setString(1, newIsAdmin);
            ps.setString(2, userId);
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new Exception("No account found with ID: " + userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
     
     public void changeStatusAccount(String userId, Account.Status newStatus) {
        String query = "UPDATE Account SET status = ?, deletedAt = ? WHERE uID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, newStatus.getValue());

            if (newStatus == Account.Status.IS_DELETE) {
                ps.setTimestamp(2, new java.sql.Timestamp(new Date().getTime()));
            } else {
                ps.setTimestamp(2, null);
            }

            ps.setString(3, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

  public void permanentlyDeleteExpiredAccounts() {
    String query = "DELETE FROM Account WHERE status = 'isDelete' AND DATEDIFF(second, deletedAt, GETDATE()) > 5";
    try {
        conn = new DBContext().getConnection();
        ps = conn.prepareStatement(query);
        
        int rowsAffected = ps.executeUpdate();
        System.out.println("Rows affected: " + rowsAffected); // In ra số dòng bị ảnh hưởng
        
        if (rowsAffected > 0) {
            System.out.println("Accounts permanently deleted.");
        } else {
            System.out.println("No accounts to delete.");
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
    
    public static void main(String[] args)  {
        DAOUser daoU = new DAOUser();
        
//        String username = "phuong"; // Thay đổi theo tên người dùng cần kiểm tra
//        String password = "789";
//        Account ac = daoU.login(username, password);
//        if (ac != null) {
//            System.out.println("Login sussefully");
//        }
        
//        Account ax = daoU.checkAccountExist(username);
//        if (ax != null) {
//            System.out.println("Account exits");
//        }
        
//        String acname = "test2";
//        String suser = "testsignup2";
//       String spass = "111";
//        Account at = daoU.signup(acname, suser, spass);
//        if (at != null) {
//          System.out.println("sign up sussefully");
//       }
//       
//        List<Account> accounts = daoU.getAllAccounts();
//        if (accounts != null) {
//            System.out.println("Danh sach tai khoan:");
//            for (Account account : accounts) {
//                System.out.println("ID: " + account.getId());
//                System.out.println("name account: " + account.getNameAccount());
//                System.out.println("name: " + account.getUser());
//                System.out.println("isSell: " + account.getIsSell());
//                System.out.println("isAdmin: " + account.getIsAdmin());
//                System.out.println("Status: " + account.getStatus());
//                System.out.println("----------------------------------");
//            }
//        } else {
//            System.out.println("Không có tài khoản nào.");
//        }

//        daoU.changeRoleSell("1", "1");
//        daoU.changeRoleAdmin("1", "1");

//           try {
//        // Đánh dấu tài khoản có userId là 6 là đã bị xóa
//            daoU.changeStatusAccount("7", Account.Status.IS_DELETE);
//            System.out.println("tk da bi xoa.");
//
//            // Chờ 5 giây để kiểm tra xem tài khoản có bị xóa hay không
//            Thread.sleep(5000);
//
//            // Thực hiện xóa vĩnh viễn các tài khoản đã bị xóa quá 5 giây
//            daoU.permanentlyDeleteExpiredAccounts();
//            System.out.println("thuc hien xoa vinh vien");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
            daoU.changeStatusAccount("11", Account.Status.IS_BLOCK);
    }

}
