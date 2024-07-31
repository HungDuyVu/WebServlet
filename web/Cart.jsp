<%-- 
    Document   : Cart
    Created on : Oct 31, 2020, 9:42:21 PM
    Author     : trinh
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Shopping Cart</title>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
</head>
<body>
    <jsp:include page="Menu.jsp"></jsp:include>
    <div class="container">
        <h2>Your Shopping Cart</h2>
        <c:if test="${not empty cart.items}">
    <table class="table table-striped">
        <thead>
            <tr>
                <th>Product</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Size</th>
                <th>Total</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${cart.items.values()}" var="item">
                <tr>
                    <td>${item.productName}</td>
                    <td>${item.price}</td>
                    <td>${item.quantity}</td>
                    <td>${item.size}</td>
                    <td>${item.quantity * item.price}</td>
                    <td>
                        <form action="UpdateCartServlet" method="post" style="display:inline;">
                            <input type="hidden" name="productId" value="${item.productId}">
                            <input type="number" name="quantity" value="${item.quantity}" min="1">
                            <button type="submit" class="btn btn-primary">Update</button>
                        </form>
                        <form action="RemoveFromCartServlet" method="post" style="display:inline;">
                            <input type="hidden" name="productId" value="${item.productId}">
                            <button type="submit" class="btn btn-danger">Remove</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td colspan="4" class="text-right"><strong>Total:</strong></td>
                <td>${cart.totalPrice}</td>
                <td></td>
            </tr>
        </tbody>
    </table>
</c:if>
<c:if test="${empty cart.items}">
    <p>Your cart is empty.</p>
</c:if>

        <a href="Checkout.jsp" class="btn btn-success">Proceed to Checkout</a>
    </div>
    <jsp:include page="Footer.jsp"></jsp:include>
</body>
</html>

