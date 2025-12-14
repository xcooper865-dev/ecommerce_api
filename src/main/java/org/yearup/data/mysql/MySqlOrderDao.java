package org.yearup.data.mysql;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.yearup.models.Order;
import org.yearup.models.OrderLineItem;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlOrderDao extends MySqlDaoBase implements OrderDao {
    public MySqlOrderDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Order createOrder(Order order) {

        String sql = """
                INSERT INTO orders (user_id, order_date, total)
                VALUES (?, NOW(), ?
                """;

        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, order.getUserId());
            preparedStatement.setBigDecimal(2, order.getTotal());
            preparedStatement.executeUpdate();

            ResultSet keys = preparedStatement.getGeneratedKeys();
            if (keys.next())
                order.setOrderId(keys.getInt(1));

            return order;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void addLineItem(OrderLineItem item) {

        String sql = """
                INSERT INTO order_line_items (order_id, product_id, quantity,VALUES(?,?,?,?)
                
                """;
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, item.getOrderId());
            preparedStatement.setInt(2, item.getProductId());
            preparedStatement.setInt(3, item.getQuantity());
            preparedStatement.setBigDecimal(4, item.getPrice());
            preparedStatement.executeUpdate();
        } catch (SQLException e)
        {
            throw new RuntimeException();
        }
    }
}
