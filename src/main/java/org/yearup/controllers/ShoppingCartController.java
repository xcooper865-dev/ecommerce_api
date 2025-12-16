package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("/cart")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class ShoppingCartController {

    private final ShoppingCartDao shoppingCartDao;
    private final UserDao userDao;
    private final ProductDao productDao;

    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    @GetMapping("")
    public ShoppingCart getCart(Principal principal) {
        User user = getUser(principal);
        return shoppingCartDao.getByUserId(user.getId());
    }

    @PostMapping("/products/{productId}")
    public ShoppingCart addProductToCart(@PathVariable int productId, Principal principal) {
        User user = getUser(principal);

        if (productDao.getById(productId) == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        shoppingCartDao.addProduct(user.getId(), productId);
        return shoppingCartDao.getByUserId(user.getId());
    }

    @PutMapping("/products/{productId}")
    public ShoppingCart updateProductInCart(@PathVariable int productId, @RequestBody ShoppingCartItem item, Principal principal) {
        User user = getUser(principal);

        if (productDao.getById(productId) == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        if (item == null || item.getQuantity() < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be 0 or greater.");

        shoppingCartDao.updateProduct(user.getId(), productId, item.getQuantity());
        return shoppingCartDao.getByUserId(user.getId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(Principal principal) {
        User user = getUser(principal);
        shoppingCartDao.clearCart(user.getId());
    }

    @DeleteMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeProductFromCart(@PathVariable int productId, Principal principal) {
        User user = getUser(principal);
        shoppingCartDao.removeProduct(user.getId(), productId);
    }


    private User getUser(Principal principal) {
        String username = principal.getName();
        User user = userDao.getByUsername(username);  // FIXED method name
        if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        return user;
    }
}
