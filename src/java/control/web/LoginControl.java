package control.web;

import dao.DAOUser;
import entity.Account;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author OS
 */
public class LoginControl extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String username = request.getParameter("user");
        String password = request.getParameter("pass");
        
        DAOUser daoU = new DAOUser();
        
        Account account = daoU.login(username, password);
        if (account == null) {
            // Tài khoản không tồn tại hoặc mật khẩu không đúng
            request.setAttribute("mess", "Incorrect username or password.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
        } else {
            // Kiểm tra trạng thái tài khoản
            if (account.getStatus() == Account.Status.IS_ACTIVE) {
                // Trạng thái là isActive, cho phép đăng nhập
                HttpSession session = request.getSession();
                session.setAttribute("acc", account);
                // Chuyển trang mà cần mang theo dữ liệu
                request.getRequestDispatcher("home").forward(request, response);
            } else {
                // Trạng thái không phải isActive, không cho phép đăng nhập
                request.setAttribute("mess", "Account is not active. Please contact support.");
                request.getRequestDispatcher("Login.jsp").forward(request, response);
            }
        }
    } 

    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
