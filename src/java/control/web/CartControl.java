package control.web;

import dao.DAOCart;
import entity.Cart;
import entity.Account;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/CartControl")
public class CartControl extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("acc");

        if (account != null) {
            Integer userId = account.getId();
            
            DAOCart daoCart = new DAOCart();
            try {
                Cart cart = daoCart.getCartByUserId(userId);
                int cartCount = (cart != null) ? daoCart.getCartSize(cart.getId()) : 0;
                session.setAttribute("cartCount", cartCount); // Cập nhật session với số lượng sản phẩm trong giỏ hàng
                request.setAttribute("cart", cart);
                request.getRequestDispatcher("Cart.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            response.sendRedirect("Login.jsp");
        }
    }
}
