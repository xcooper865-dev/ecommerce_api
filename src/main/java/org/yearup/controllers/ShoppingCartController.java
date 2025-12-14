package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("/cart")
@PreAuthorize("isAuthenticated()")
public class ShoppingCartController
{
    private final ShoppingCartDao shoppingCartDao;
    private final UserDao userDao;

    @Autowired
    public ShoppingCartController(
            ShoppingCartDao shoppingCartDao,
            UserDao userDao)
    {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
    }

    // GET /cart
    @GetMapping
    public ShoppingCart getCart(Principal principal)
    {
        User user = userDao.getByUsername(principal.getName());
        return shoppingCartDao.getByUserId(user.getId());
    }

    // POST /cart/products/{productId}
    @PostMapping("/products/{productId}")
    public void addProductToCart(
            @PathVariable int productId,
            Principal principal)
    {
        User user = userDao.getByUsername(principal.getName());
        shoppingCartDao.addProduct(user.getId(), productId);
    }

    // PUT /cart/products/{productId}
    @PutMapping("/products/{productId}")
    public void updateProductQuantity(
            @PathVariable int productId,
            @RequestBody ShoppingCartItem item,
            Principal principal)
    {
        User user = userDao.getByUsername(principal.getName());
        shoppingCartDao.updateQuantity(
                user.getId(),
                productId,
                item.getQuantity());
    }

    // DELETE /cart
    @DeleteMapping
    public void clearCart(Principal principal)
    {
        User user = userDao.getByUsername(principal.getName());
        shoppingCartDao.clearCart(user.getId());
    }
}
