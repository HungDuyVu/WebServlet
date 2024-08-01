package control.web;

import dao.DAOCart;
import entity.Cart;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/CountCartItems")
public class CountCartItems extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId"); // Assuming userId is stored in session

        int count = 0;
        if (userId != null) {
            DAOCart daoCart = new DAOCart();
            try {
                Cart cart = daoCart.getCartByUserId(userId);
                if (cart != null) {
                    count = daoCart.getCartSize(cart.getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        request.setAttribute("cartCount", count);
        request.getRequestDispatcher("Menu.jsp").forward(request, response);
    }
}
