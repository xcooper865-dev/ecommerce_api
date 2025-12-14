package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.ShoppingCart;

@Repository
public class MySqlShoppingCartDao implements ShoppingCartDao
{
    private final JdbcTemplate jdbcTemplate;

    public MySqlShoppingCartDao(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ShoppingCart getByUserId(int userId)
    {
        // TODO: implement SQL
        return new ShoppingCart();
    }

    @Override
    public void addProduct(int userId, int productId)
    {
        // TODO: implement SQL
    }

    @Override
    public void updateQuantity(int userId, int productId, int quantity)
    {
        // TODO: implement SQL
    }

    @Override
    public void clearCart(int userId)
    {
        // TODO: implement SQL
    }
}
