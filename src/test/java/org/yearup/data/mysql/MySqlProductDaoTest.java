package org.yearup.data.mysql;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yearup.models.Product;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MySqlProductDaoTest extends BaseDaoTestClass
{
    private MySqlProductDao dao;

    @BeforeEach
    public void setup()
    {
        dao = new MySqlProductDao(dataSource);
    }

    @Test
    public void getById_shouldReturn_theCorrectProduct()
    {
        // arrange
        int productId = 1;
        Product expected = new Product()
        {{
            setProductId(1);
            setName("Smartphone");
            setPrice(new BigDecimal("499.99"));
            setCategoryId(1);
            setDescription("A powerful and feature-rich smartphone for all your communication needs.");
            setSubCategory("Black");
            setStock(50);
            setFeatured(false);
            setImageUrl("smartphone.jpg");
        }};

        // act
        var actual = dao.getById(productId);

        // assert
        assertEquals(expected.getPrice(), actual.getPrice(), "Because I tried to get product 1 from the database.");
    }
    @Test
    public void search_shouldReturnProductsMatchingFilters()
    {
        Integer categoryId = 1;
        BigDecimal minPrice = new BigDecimal("100");
        BigDecimal maxPrice = new BigDecimal("2000");

        var results = dao.search(categoryId, minPrice, maxPrice, null);

        assertTrue(results.size()> 0, "Search should return results");
        results.forEach(product -> {
            assertEquals(categoryId, product.getCategoryId(),
                    "Product should belong to the request category");

            assertTrue(product.getPrice().compareTo(minPrice)>= 0,
                    "product price should be >= minPrice");

            assertTrue(product.getPrice().compareTo(maxPrice )<= 0,
                    "Product price should be <= maxPrice");
        });

    }
    @Test
    public  void update_shouldModifyExistingProductOnly()
    {
        int productId = 1;
        Product product = dao.getById(productId);

        BigDecimal newPrice = new BigDecimal("450.00");
        product.setPrice(newPrice);

        dao.update(productId,product);
        Product updated = dao.getById(productId);

        assertEquals(newPrice, updated.getPrice(),
                "update should change price without creating a new product");
    }
}

