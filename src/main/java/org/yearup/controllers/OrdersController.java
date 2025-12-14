package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.data.mysql.OrderDao;
import org.yearup.models.*;

import java.security.Principal;

@RestController
@RequestMapping("/orders")
@PreAuthorize("isAuthenticated()")
public class OrdersController {

    private OrderDao orderDao;
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;

    @Autowired
    public OrdersController(
            OrderDao orderDao,
            ShoppingCartDao shoppingCartDao,
            UserDao userDao
    )
    {
        this.orderDao = orderDao;
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
    }
    @PostMapping
            public Order checkout(Principal principal){

        User user = userDao.getByUserName(principal.getName());

        ShoppingCart cart = shoppingCartDao.getByUserId(user.getId());

        Order order = new Order();
        order.setUserId(user.getId());
        order.setTotal(cart.getTotal());

        order = orderDao.createOrder(order);

        for(ShoppingCartItem item : cart.getItems().values())
        {
            OrderLineItem line = new OrderLineItem();
            line.setOrderId(order.getOrderId());
            line.setProductId(item.getProductId());
            line.setQuantity(item.getQuantity());
            line.setPrice(item.getProduct().getPrice());


            orderDao.addLineItem(line);
        }

        shoppingCartDao.clearCart(user.getId());

        return order;

    }

}
