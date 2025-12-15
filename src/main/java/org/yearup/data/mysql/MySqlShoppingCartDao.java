package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

@Repository
public class MySqlShoppingCartDao implements ShoppingCartDao
{
    private final JdbcTemplate jdbcTemplate;

    private final ProductDao productDao;

    public MySqlShoppingCartDao(JdbcTemplate jdbcTemplate, ProductDao productDao)
    {
        this.jdbcTemplate = jdbcTemplate;
        this.productDao = productDao;
    }


    @Override
    public ShoppingCart getByUserId(int userId)
    {
        String sql = """
        SELECT product_id, quantity
        FROM shopping_cart
        WHERE user_id = ?
    """;

        ShoppingCart cart = new ShoppingCart();

        jdbcTemplate.query(sql, rs -> {
            int productId = rs.getInt("product_id");
            int quantity = rs.getInt("quantity");

            Product product = productDao.getById(productId);

            ShoppingCartItem item = new ShoppingCartItem();
            item.setProduct(product);
            item.setQuantity(quantity);

            cart.add(item);
        }, userId);

        return cart;
    }

    @Override
    public void addProduct(int userId, int productId)
    {
        // TODO: implement SQL
        String sql = """
                INSERT INTO shopping_cart (user_id, product_id, quantity)
                VALUES (?,?,1)
                ON DUPLICATE KEY UPDATE quantity = quantity + 1
                """;

        jdbcTemplate.update(sql, userId, productId);

    }

    @Override
    public void updateQuantity(int userId, int productId, int quantity)
    {
        // TODO: implement SQL
        String sql = """
                UPDATE shopping_cart
                SET quantity = ?
                WHERE user_id = ? AND product_id = ?
                """;
        jdbcTemplate.update(sql, quantity,userId,productId);
    }

    @Override
    public void clearCart(int userId)
    {
        // TODO: implement SQL
        String sql = """
                DELETE FROM shopping_cart
                WHERE user_id = ?
                """;

        jdbcTemplate.update(sql,userId);
    }
}
