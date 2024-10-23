package cfa.fishing.fishing_store_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("cfa.fishing.fishing_store_app.entity")
@EnableJpaRepositories("cfa.fishing.fishing_store_app.repository")
public class FishingStoreApp {
    public static void main(String[] args) {
        SpringApplication.run(FishingStoreApp.class, args);
    }
}