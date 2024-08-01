package control.web;

import dao.DAOCart;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/UpdateCartControl")
public class UpdateCartControl extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int cartId = Integer.parseInt(request.getParameter("cartId"));
        int productId = Integer.parseInt(request.getParameter("productId"));
        String action = request.getParameter("action");

        DAOCart daoCart = new DAOCart();

        try {
            switch (action) {
                case "increase" -> daoCart.incrementProductQuantity(cartId, productId);
                case "decrease" -> daoCart.decrementProductQuantity(cartId, productId);
                case "remove" -> daoCart.removeCartItem(cartId, productId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("cart.jsp");
    }
}
