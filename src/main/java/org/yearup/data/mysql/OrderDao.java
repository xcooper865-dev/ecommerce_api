package org.yearup.data.mysql;

import org.yearup.models.Order;
import org.yearup.models.OrderLineItem;

public interface OrderDao {

    Order createOrder(Order order);
    void addLineItem(OrderLineItem item);
}
