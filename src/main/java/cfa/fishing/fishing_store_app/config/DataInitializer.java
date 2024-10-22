package cfa.fishing.fishing_store_app.config;

import cfa.fishing.fishing_store_app.entity.product.Product;
import cfa.fishing.fishing_store_app.entity.product.ProductCategory;
import cfa.fishing.fishing_store_app.entity.user.Role;
import cfa.fishing.fishing_store_app.entity.user.User;
import cfa.fishing.fishing_store_app.repository.ProductRepository;
import cfa.fishing.fishing_store_app.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, ProductRepository productRepository) {
        return args -> {
            // Create users if none exist
            if (userRepository.count() == 0) {
                User admin = new User("admin@fishing.com", "admin123");
                admin.setFirstName("Admin");
                admin.setLastName("User");
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);

                User customer = new User("customer@example.com", "customer123");
                customer.setFirstName("John");
                customer.setLastName("Doe");
                customer.setAddress("123 Fishing Street");
                customer.setPhoneNumber("0123456789");
                userRepository.save(customer);
            }

            // Create products if none exist
            if (productRepository.count() == 0) {
                Product rod = new Product(
                        "Professional Fishing Rod",
                        "High-quality carbon fiber rod",
                        new BigDecimal("149.99"),
                        10,
                        ProductCategory.FISHING_RODS,
                        "rod1.jpg"
                );
                productRepository.save(rod);

                Product reel = new Product(
                        "Premium Fishing Reel",
                        "Smooth drag system",
                        new BigDecimal("89.99"),
                        15,
                        ProductCategory.REELS,
                        "reel1.jpg"
                );
                productRepository.save(reel);

                Product bait = new Product(
                        "Artificial Worm Set",
                        "Pack of 10 soft plastic worms",
                        new BigDecimal("12.99"),
                        50,
                        ProductCategory.BAITS,
                        "bait1.jpg"
                );
                productRepository.save(bait);
            }
        };
    }
}