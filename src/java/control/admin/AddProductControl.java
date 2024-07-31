package control.admin;

import dao.DAOProduct;
import entity.Account;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author OS
 */
@WebServlet(name="AddControl", urlPatterns={"/add"})
public class AddProductControl extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        // Lấy thông tin sản phẩm từ request
        String pname = request.getParameter("name");
        String pprice = request.getParameter("price");
        String ptitle = request.getParameter("title");
        String pdescription = request.getParameter("description");
        String pcategory = request.getParameter("category");
        
        // Xử lý kích thước sản phẩm (có thể được gửi dưới dạng nhiều giá trị từ form)
        String[] sizesArray = request.getParameterValues("sizes"); // Ví dụ: ["36", "37"]
        List<String> sizes = sizesArray != null ? List.of(sizesArray) : new ArrayList<>();

        // Xử lý hình ảnh sản phẩm (có thể được gửi dưới dạng nhiều giá trị từ form)
        String[] imagesArray = request.getParameterValues("images"); // Ví dụ: ["http://example.com/image1.jpg", "http://example.com/image2.jpg"]
        List<String> images = imagesArray != null ? List.of(imagesArray) : new ArrayList<>();
        
        // Lấy ID người bán từ session
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("acc");
        int sid = a != null ? a.getId() : 0; // ID người bán, dùng 0 nếu không có session
        
        // Gọi phương thức DAO để chèn sản phẩm
        DAOProduct daoP = new DAOProduct();
        daoP.insertProduct(pname, pprice, ptitle, pdescription, pcategory, sid, sizes, images);
        
        // Chuyển hướng đến trang quản lý sản phẩm sau khi thêm sản phẩm
        response.sendRedirect("manager");
    } 

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "AddProductControl Servlet";
    }
}
