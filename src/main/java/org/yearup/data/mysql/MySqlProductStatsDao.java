package org.yearup.data.mysql;

import org.springframework.stereotype.Repository;
import org.yearup.data.ProductStatsDao;
import org.yearup.models.PriceRange;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class MySqlProductStatsDao extends MySqlDaoBase implements ProductStatsDao {

    public MySqlProductStatsDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public PriceRange getPriceRange() {
        String sql = "SELECT MIN(price) AS min_price, MAX(price) AS max_price FROM products";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                BigDecimal min = rs.getBigDecimal("min_price");
                BigDecimal max = rs.getBigDecimal("max_price");
                return new PriceRange(min, max);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new PriceRange(BigDecimal.ZERO, BigDecimal.ZERO);
    }
}
