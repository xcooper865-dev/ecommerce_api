package org.yearup.models;

import java.math.BigDecimal;

public class PriceRange {
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    public PriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }
}
