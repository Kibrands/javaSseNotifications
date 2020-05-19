package es.aytos.notification.service.impl;

import es.aytos.notification.model.Notification;
import es.aytos.notification.model.Product;
import es.aytos.notification.repository.ProductRepository;
import es.aytos.notification.service.NotificationService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
@NoArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    private Notification notification;

    @Override
    public Notification getNotification() {
        Product newProduct = this.productRepository.getProduct();
        if (this.product == null) {
            this.product = newProduct;
            this.notification = new Notification(0, String.format("%s - %.2f", newProduct.getNombre(), newProduct.getPrecio()));
        } else if (!product.getPrecio().equals(newProduct.getPrecio())) {
            this.product = newProduct;
            this.notification.setId(this.notification.getId() + 1);
            this.notification.setMessage(String.format("%s - %.2f", newProduct.getNombre(), newProduct.getPrecio()));
        }
        return this.notification;
    }

}
