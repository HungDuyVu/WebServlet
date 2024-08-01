package control.web;

import dao.DAOCart;
import dao.DAOProduct;
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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/AddToCart")
public class AddToCart extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        addToCart(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        addToCart(request, response);
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId != null) {
            try {
                int productId = Integer.parseInt(request.getParameter("productId"));
                DAOProduct daoProduct = new DAOProduct();
                DAOCart daoCart = new DAOCart();

                // Fetch or create the user's cart
                Cart cart = Optional.ofNullable(daoCart.getCartByUserId(userId))
                                    .orElseGet(() -> {
                    try {
                        return daoCart.createCart(userId);
                    } catch (Exception ex) {
                        Logger.getLogger(AddToCart.class.getName()).log(Level.SEVERE, null, ex);
                        return null;
                    }
                });

                if (cart != null) {
                    // Retrieve product details
                    Product product = daoProduct.getProductByID(String.valueOf(productId));
                    if (product != null) {
                        // Create a cart item
                        CartItem item = new CartItem(0, product.getId(), product.getPrice(), 1, "defaultSize");

                        // Add the item to the cart
                        daoCart.addOrUpdateProductInCart(cart.getId(), item);

                        // Update cart count in session
                        int cartCount = daoCart.getCartSize(cart.getId());
                        session.setAttribute("cartCount", cartCount);
                    }
                }

            } catch (NumberFormatException | NullPointerException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                Logger.getLogger(AddToCart.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        response.sendRedirect("HomeControl"); // Redirect to home page or another relevant page
    }
}
