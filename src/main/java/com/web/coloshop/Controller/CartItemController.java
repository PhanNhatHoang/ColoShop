package com.web.coloshop.Controller;

import com.web.coloshop.Service.*;
import com.web.coloshop.model.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

@Controller
public class CartItemController {
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private UserService userService;
    @Autowired
    private StatusService statusService;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static String generateRandomString(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }
    @GetMapping("/Cart")
    public String Cart(Model model , HttpSession session){
        Long userId = (Long) session.getAttribute("userID");
        Cart cart = cartService.findbyUserId(userId);
        Long cartId = cart.getId();
        List<CartItem> cartItems = cartItemService.findByCartID(cartId);
        Double totalPrice = 0.0;
        for (CartItem cartItem: cartItems){
            totalPrice += cartItem.getTotalPrice();
        }
        model.addAttribute("total", totalPrice);
        model.addAttribute("cartItems", cartItems);
        return "User/Cart";
    }
    @DeleteMapping("/remove/{id}")
    public String remoce(@PathVariable Long id){
        cartItemService.deleteById(id);
        return "redirect:/Cart";
    }
    @PostMapping("/save_Cart_Item")
    public String saveCartItem(@RequestParam("productId") Long productId, HttpSession session) {
        if (session != null) {
            Long userId = (Long) session.getAttribute("userID");
            Cart cart = cartService.findbyUserId(userId);
            Long cartId = cart.getId();

            // Truy vấn cơ sở dữ liệu để lấy thông tin chi tiết của sản phẩm
            Product product = productService.findById(productId);

            // Kiểm tra xem sản phẩm đã tồn tại trong giỏ hàng chưa
            CartItem existingCartItem = cartItemService.getProductQuantityInCart(productId, cartId);
            if (existingCartItem != null) {
                // Nếu sản phẩm đã tồn tại trong giỏ hàng, tăng số lượng lên 1 và cập nhật tổng giá trị
                int newQuantity = existingCartItem.getQuantity() + 1;
                existingCartItem.setQuantity(newQuantity);
                Double total = newQuantity * product.getSaleprice();
                existingCartItem.setTotalPrice(total);
                cartItemService.save(existingCartItem);
            } else {
                // Nếu sản phẩm chưa tồn tại trong giỏ hàng, tạo mới CartItem
                CartItem cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setCart(cart);
                cartItem.setQuantity(1);
                Double total = 1 * product.getSaleprice();
                cartItem.setTotalPrice(total);
                cartItemService.save(cartItem);
            }
            return "redirect:/shop";
        } else {
            return "redirect:/Login";
        }
    }

    @PostMapping("/updateQuantity")
    public String updateQuantity(@RequestParam("cartItemId") Long cartItemId, @RequestParam("change") String change) {
        CartItem cartItem = cartItemService.findById(cartItemId);
        if (change.equals("-")){
            cartItem.setQuantity(cartItem.getQuantity()-1);
            cartItem.setTotalPrice(cartItem.getQuantity() * cartItem.getProduct().getSaleprice());
        }else if (change.equals("+")){
            cartItem.setQuantity(cartItem.getQuantity()+1);
            cartItem.setTotalPrice(cartItem.getQuantity() * cartItem.getProduct().getSaleprice());
        }
        cartItemService.save(cartItem);
        return "redirect:/Cart";
    }

    @PostMapping("/checkOut")
    public String CheckOut(HttpSession session){
        Long userId = (Long) session.getAttribute("userID");
        User user = userService.findById(userId);
        if (user.getPhone() == null|| user.getAddress()==null){
            return "redirect:/Info";
        }
        Cart cart = cartService.findbyUserId(userId);
        Long cartId = cart.getId();
        List<CartItem> cartItems = cartItemService.findByCartID(cartId);
        if (cartItems .size() > 0){
            Order order = new Order();
            Double totalPrice = 0.0;
            for (CartItem cartItem: cartItems){
                totalPrice += cartItem.getTotalPrice();
            }
            String CodeOrder = generateRandomString(13);
            order.setCode(CodeOrder);
            order.setTotal(totalPrice);
            Date now = new Date();
            order.setOrderDate(now);
            order.setUser(user);
            Status status = statusService.findById(1L);
            order.setStatus(status);
            orderService.save(order);
            for (CartItem cartItem: cartItems){
                Product product = productService.findById(cartItem.getProduct().getId());
                if (product.getCurrentQuantity()>cartItem.getQuantity()){
                    product.setCurrentQuantity(product.getCurrentQuantity() - cartItem.getQuantity());
                    OrderDetail orderDetail = new OrderDetail();
                    Order codeOrder = orderService.findFirstByCode(CodeOrder);
                    orderDetail.setOrder(codeOrder);
                    orderDetail.setProduct(product);
                    orderDetail.setQuantity(cartItem.getQuantity());
                    Double Total = cartItem.getQuantity() * cartItem.getProduct().getSaleprice();
                    orderDetail.setTotal(Total);
                    productService.save(product);
                    orderDetailService.save(orderDetail);
                    cartItemService.deleteById(cartItem.getId());
                }
            }
            return "redirect:/shop";
        }
        return "redirect:/Cart";
    }

    @PostMapping("/save_Cart_Item_Detail")
    public String saveCartItemDetail(@RequestParam("productId") Long productId,@RequestParam("quantity") Integer quantity, HttpSession session) {
        if (session != null) {
            Long userId = (Long) session.getAttribute("userID");
            Cart cart = cartService.findbyUserId(userId);
            Long cartId = cart.getId();

            // Truy vấn cơ sở dữ liệu để lấy thông tin chi tiết của sản phẩm
            Product product = productService.findById(productId);

            // Kiểm tra xem sản phẩm đã tồn tại trong giỏ hàng chưa
            CartItem existingCartItem = cartItemService.getProductQuantityInCart(productId, cartId);
            if (existingCartItem != null) {
                // Nếu sản phẩm đã tồn tại trong giỏ hàng, tăng số lượng lên 1 và cập nhật tổng giá trị
                int newQuantity = existingCartItem.getQuantity() + quantity;
                existingCartItem.setQuantity(newQuantity);
                Double total = newQuantity * product.getSaleprice();
                existingCartItem.setTotalPrice(total);
                cartItemService.save(existingCartItem);
            } else {
                // Nếu sản phẩm chưa tồn tại trong giỏ hàng, tạo mới CartItem
                CartItem cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setCart(cart);
                cartItem.setQuantity(quantity);
                Double total = quantity * product.getSaleprice();
                cartItem.setTotalPrice(total);
                cartItemService.save(cartItem);
            }
            return "redirect:/shop";
        } else {
            return "redirect:/Login";
        }
    }

}
