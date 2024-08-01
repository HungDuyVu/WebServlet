package control.web;

import dao.DAOCart;
import dao.DAOProduct;
import entity.Account;
import entity.Cart;
import entity.CartItem;
import entity.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/CartViewControl")
public class CartViewControl extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("acc"); // Lấy đối tượng account từ session
        if (account != null) {
            try {
                int userId = account.getId();
                DAOCart daoCart = new DAOCart();
                DAOProduct daoProduct = new DAOProduct();
                Cart cart = daoCart.getCartByUserId(userId);
                if (cart != null) {
                    Map<Integer, CartItem> cartItems = cart.getItems();
                    for (CartItem item : cartItems.values()) {
                        Product product = daoProduct.getProductByID(String.valueOf(item.getProductId()));
                        if (product != null) {
                            item.setProductName(product.getName());
                            item.setProductImage(product.getImageUrls().isEmpty() ? "" : product.getImageUrls().get(0));
                        }
                    }
                    request.setAttribute("cartItems", cartItems);
                    request.setAttribute("totalPrice", cart.getTotalPrice());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        request.getRequestDispatcher("Cart.jsp").forward(request, response);
    }
}
