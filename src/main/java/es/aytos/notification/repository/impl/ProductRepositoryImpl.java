package es.aytos.notification.repository.impl;

import es.aytos.notification.model.Product;
import es.aytos.notification.repository.ProductRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
@Setter
public class ProductRepositoryImpl implements ProductRepository {

    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @Override
    public Product getProduct() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT id, nombre, precio FROM products WHERE id = 1";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        Product product = new Product();
        product.setId((BigDecimal) rows.get(0).get("id"));
        product.setNombre((String) rows.get(0).get("nombre"));
        product.setPrecio(((BigDecimal) rows.get(0).get("precio")).doubleValue());

        return product;
    }
}