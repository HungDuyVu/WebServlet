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

                // Lấy hoặc tạo giỏ hàng của người dùng
                Cart cart = daoCart.createOrGetCart(userId);

                if (cart != null) {
                    // Lấy thông tin sản phẩm
                    Product product = daoProduct.getProductByID(String.valueOf(productId));
                    if (product != null) {
                        // Tạo đối tượng CartItem với thông tin sản phẩm
                        CartItem item = new CartItem(product.getId(), product.getPrice(), 1, "defaultSize", cart.getId());

                        // Thêm sản phẩm vào giỏ hàng
                        daoCart.addOrUpdateProductInCart(cart.getId(), item);

                        // Cập nhật số lượng sản phẩm trong giỏ hàng
                        int cartCount = daoCart.getCartSize(cart.getId());
                        session.setAttribute("cartCount", cartCount);
                    }
                }

            } catch (NumberFormatException | NullPointerException e) {
                Logger.getLogger(AddToCart.class.getName()).log(Level.SEVERE, "Invalid input", e);
            } catch (Exception ex) {
                Logger.getLogger(AddToCart.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        response.sendRedirect("HomeControl"); // Chuyển hướng về trang chủ hoặc trang thích hợp khác
    }
}
