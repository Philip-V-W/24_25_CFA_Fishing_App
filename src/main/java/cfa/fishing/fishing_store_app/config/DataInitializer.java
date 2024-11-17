package cfa.fishing.fishing_store_app.config;

import cfa.fishing.fishing_store_app.entity.contest.*;
import cfa.fishing.fishing_store_app.entity.order.*;
import cfa.fishing.fishing_store_app.entity.permit.*;
import cfa.fishing.fishing_store_app.entity.product.*;
import cfa.fishing.fishing_store_app.entity.user.*;
import cfa.fishing.fishing_store_app.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static cfa.fishing.fishing_store_app.entity.user.Role.ADMIN;
import static cfa.fishing.fishing_store_app.entity.user.Role.CUSTOMER;

@Configuration
public class DataInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initDatabase(
            UserRepository userRepository,
            AddressRepository addressRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository,
            PermitRepository permitRepository,
            ContestRepository contestRepository,
            ContestRegistrationRepository contestRegistrationRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // Create users if none exist
            if (userRepository.count() == 0) {
                logger.info("Initializing users...");
                User admin = User.builder()
                        .email("admin@fishing.com")
                        .password(passwordEncoder.encode("admin123"))
                        .firstName("Admin")
                        .lastName("User")
                        .role(ADMIN)
                        .active(true)
                        .build();
                userRepository.save(admin);

                User customer = User.builder()
                        .email("customer@example.com")
                        .password(passwordEncoder.encode("customer123"))
                        .firstName("John")
                        .lastName("Doe")
                        .phoneNumber("0123456789")
                        .role(CUSTOMER)
                        .active(true)
                        .build();
                userRepository.save(customer);
                logger.info("Users initialized successfully");
            }

            // Create addresses for users
            if (addressRepository.count() == 0) {
                logger.info("Initializing addresses...");

                // Get the already saved users from the database
                User admin = userRepository.findByEmail("admin@fishing.com")
                        .orElseThrow(() -> new RuntimeException("Admin user not found"));
                User customer = userRepository.findByEmail("customer@example.com")
                        .orElseThrow(() -> new RuntimeException("Customer user not found"));

                Address address1 = new Address();
                address1.setUser(admin);  // Use the fetched admin user
                address1.setStreetAddress("456 Admin Road");
                address1.setCity("Admin City");
                address1.setState("NY");
                address1.setPostalCode("12345");
                address1.setCountry("USA");
                address1.setDefault(true);
                addressRepository.save(address1);

                Address address2 = new Address();
                address2.setUser(customer);  // Use the fetched customer user
                address2.setStreetAddress("123 Fishing Street");
                address2.setCity("Fishing Town");
                address2.setState("CA");
                address2.setPostalCode("67890");
                address2.setCountry("USA");
                address2.setDefault(true);
                addressRepository.save(address2);

                logger.info("Addresses initialized successfully");
            }

            // Create products if none exist
            if (productRepository.count() == 0) {
                logger.info("Initializing products...");

                // Fishing Rods
                Product rod = new Product(
                        "Professional Fishing Rod",  // Keep this exact name - used in order initialization
                        "High-quality carbon fiber rod for professional anglers",
                        new BigDecimal("149.99"),
                        10,
                        ProductCategory.FISHING_RODS,
                        "rod1.jpg"
                );
                productRepository.save(rod);

                Product rod2 = new Product(
                        "Intermediate Spinning Rod",
                        "Versatile spinning rod for fresh and saltwater fishing",
                        new BigDecimal("89.99"),
                        5,
                        ProductCategory.FISHING_RODS,
                        "rod2.jpg"
                );
                productRepository.save(rod2);

                Product rod3 = new Product(
                        "Beginner Casting Rod",
                        "Perfect starter rod for newcomers to fishing",
                        new BigDecimal("59.99"),
                        0,
                        ProductCategory.FISHING_RODS,
                        "rod3.webp"
                );
                productRepository.save(rod3);

                // Reels
                Product reel = new Product(
                        "Premium Fishing Reel",  // Changed back to exact name used in order initialization
                        "Smooth drag system reel",
                        new BigDecimal("99.99"),
                        15,
                        ProductCategory.REELS,
                        "reel1.webp"
                );
                productRepository.save(reel);

                Product reel2 = new Product(
                        "Baitcasting Reel",
                        "Professional grade baitcasting reel with magnetic brake",
                        new BigDecimal("129.99"),
                        12,
                        ProductCategory.REELS,
                        "reel2.jpg"
                );
                productRepository.save(reel2);

                Product reel3 = new Product(
                        "Ultra-Light Spinning Reel",
                        "Lightweight reel perfect for panfishing",
                        new BigDecimal("79.99"),
                        18,
                        ProductCategory.REELS,
                        "reel3.jpg"
                );
                productRepository.save(reel3);

                // Lures
                Product lure = new Product(
                        "Luminous Fishing Lure",
                        "Glow-in-the-dark lure for night fishing",
                        new BigDecimal("5.00"),
                        100,
                        ProductCategory.LURES,
                        "lure1.jpg"
                );
                productRepository.save(lure);

                Product lure2 = new Product(
                        "Topwater Bass Lure",
                        "Surface lure perfect for bass fishing",
                        new BigDecimal("8.99"),
                        75,
                        ProductCategory.LURES,
                        "lure2.webp"
                );
                productRepository.save(lure2);

                Product lure3 = new Product(
                        "Soft Plastic Lure Set",
                        "Variety pack of soft plastic fishing lures",
                        new BigDecimal("12.99"),
                        50,
                        ProductCategory.LURES,
                        "lure3.webp"
                );
                productRepository.save(lure3);

                // Lines
                Product line = new Product(
                        "Braided Fishing Line",
                        "Strong and durable braided line",
                        new BigDecimal("19.99"),
                        20,
                        ProductCategory.LINES,
                        "line1.jpg"
                );
                productRepository.save(line);

                Product line2 = new Product(
                        "Fluorocarbon Line",
                        "Nearly invisible underwater fishing line",
                        new BigDecimal("15.99"),
                        25,
                        ProductCategory.LINES,
                        "line2.webp"
                );
                productRepository.save(line2);

                Product line3 = new Product(
                        "Monofilament Line",
                        "Versatile all-purpose fishing line",
                        new BigDecimal("9.99"),
                        30,
                        ProductCategory.LINES,
                        "line3.webp"
                );
                productRepository.save(line3);

                // Clothing
                Product clothing = new Product(
                        "Waterproof Fishing Jacket",  // Keep this exact name - used in order initialization
                        "All-weather protection for serious anglers",
                        new BigDecimal("89.99"),
                        25,
                        ProductCategory.CLOTHING,
                        "jacket1.jpg"
                );
                productRepository.save(clothing);

                Product pants = new Product(
                        "Fishing Cargo Pants",
                        "Durable water-resistant fishing pants",
                        new BigDecimal("59.99"),
                        20,
                        ProductCategory.CLOTHING,
                        "pants1.jpg"
                );
                productRepository.save(pants);

                Product hoody = new Product(
                        "Fishing Hoodie",
                        "Comfortable UV-protected fishing hoodie",
                        new BigDecimal("49.99"),
                        30,
                        ProductCategory.CLOTHING,
                        "hoody1.jpg"
                );
                productRepository.save(hoody);

                Product shirt = new Product(
                        "Performance Fishing Shirt",
                        "Quick-dry UV-protected fishing shirt",
                        new BigDecimal("34.99"),
                        40,
                        ProductCategory.CLOTHING,
                        "shirt1.jpg"
                );
                productRepository.save(shirt);

                // Accessories
                Product pliers = new Product(
                        "Fishing Pliers Set",
                        "Professional-grade stainless steel pliers",
                        new BigDecimal("29.99"),
                        30,
                        ProductCategory.ACCESSORIES,
                        "pliers1.jpg"
                );
                productRepository.save(pliers);

                Product gaff = new Product(
                        "Telescopic Fishing Gaff",
                        "Heavy-duty telescopic gaff hook",
                        new BigDecimal("39.99"),
                        15,
                        ProductCategory.ACCESSORIES,
                        "gaff1.jpg"
                );
                productRepository.save(gaff);

                Product rodHolder = new Product(
                        "Adjustable Rod Holder",
                        "Universal mount fishing rod holder",
                        new BigDecimal("24.99"),
                        25,
                        ProductCategory.ACCESSORIES,
                        "rod-holder1.jpg"
                );
                productRepository.save(rodHolder);

                Product net = new Product(
                        "Landing Net",
                        "Large rubber mesh landing net",
                        new BigDecimal("44.99"),
                        20,
                        ProductCategory.ACCESSORIES,
                        "net1.jpg"
                );
                productRepository.save(net);

                // Tackle Boxes
                Product tackleBox = new Product(
                        "Large Tackle Storage Box",  // Keep this exact name - used in order initialization
                        "Waterproof box with multiple compartments",
                        new BigDecimal("45.99"),
                        15,
                        ProductCategory.TACKLE_BOXES,
                        "tackle1.jpg"
                );
                productRepository.save(tackleBox);

                Product tackle2 = new Product(
                        "Medium Tackle Box",
                        "Compact waterproof tackle storage",
                        new BigDecimal("29.99"),
                        20,
                        ProductCategory.TACKLE_BOXES,
                        "tackle2.jpg"
                );
                productRepository.save(tackle2);

                Product tackle3 = new Product(
                        "Premium Tackle Backpack",
                        "Fishing backpack with integrated tackle storage",
                        new BigDecimal("79.99"),
                        10,
                        ProductCategory.TACKLE_BOXES,
                        "tackle3.webp"
                );
                productRepository.save(tackle3);

                // Electronics
                Product electronics = new Product(
                        "Fish Finder GPS Combo",  // Keep this exact name - used in order initialization
                        "Advanced sonar with GPS navigation",
                        new BigDecimal("299.99"),
                        8,
                        ProductCategory.ELECTRONICS,
                        "fish-finder1.jpg"
                );
                productRepository.save(electronics);

                Product fishFinder2 = new Product(
                        "Advanced Fish Finder",
                        "High-resolution fish finder with GPS",
                        new BigDecimal("399.99"),
                        8,
                        ProductCategory.ELECTRONICS,
                        "fish-finder2.jpg"
                );
                productRepository.save(fishFinder2);

                Product fishFinder3 = new Product(
                        "Professional Fish Finder",
                        "Professional-grade fish finder with side imaging",
                        new BigDecimal("599.99"),
                        5,
                        ProductCategory.ELECTRONICS,
                        "fish-finder3.jpg"
                );
                productRepository.save(fishFinder3);


                logger.info("Products initialized successfully");
            }

            // Create orders and order items if none exist
            if (orderRepository.count() == 0) {
                logger.info("Initializing orders and order items...");
                try {
                    User customer = userRepository.findByEmail("customer@example.com")
                            .orElseThrow(() -> new RuntimeException("Customer not found"));
                    User admin = userRepository.findByEmail("admin@fishing.com")
                            .orElseThrow(() -> new RuntimeException("Admin not found"));

                    Product rod = productRepository.findByName("Professional Fishing Rod")
                            .orElseThrow(() -> new RuntimeException("Rod product not found"));
                    Product reel = productRepository.findByName("Premium Fishing Reel")
                            .orElseThrow(() -> new RuntimeException("Reel product not found"));
                    Product clothing = productRepository.findByName("Waterproof Fishing Jacket")
                            .orElseThrow(() -> new RuntimeException("Clothing product not found"));
                    Product electronics = productRepository.findByName("Fish Finder GPS Combo")
                            .orElseThrow(() -> new RuntimeException("Electronics product not found"));

                    // Create first order for customer
                    Order order1 = new Order();
                    order1.setUser(customer);
                    order1.setStatus(OrderStatus.PAID);
                    order1.setShippingAddress("123 Fishing Street");
                    order1.setOrderDate(LocalDateTime.now().minusDays(5));
                    order1.setTrackingNumber("TRACK-123-456");

                    OrderItem item1 = new OrderItem(rod, 1);
                    OrderItem item2 = new OrderItem(reel, 2);

                    order1.addItem(item1);
                    order1.addItem(item2);

                    orderRepository.save(order1);

                    // Create second order for customer (pending)
                    Order order2 = new Order();
                    order2.setUser(customer);
                    order2.setStatus(OrderStatus.PENDING);
                    order2.setShippingAddress("123 Fishing Street");
                    order2.setOrderDate(LocalDateTime.now());

                    OrderItem item3 = new OrderItem(rod, 1);

                    order2.addItem(item3);

                    orderRepository.save(order2);

                    // Create orders for admin
                    Order adminOrder1 = new Order();
                    adminOrder1.setUser(admin);
                    adminOrder1.setStatus(OrderStatus.DELIVERED);
                    adminOrder1.setShippingAddress("456 Admin Road");
                    adminOrder1.setOrderDate(LocalDateTime.now().minusDays(10));
                    adminOrder1.setTrackingNumber("TRACK-789-012");

                    OrderItem adminItem1 = new OrderItem(electronics, 1);
                    OrderItem adminItem2 = new OrderItem(clothing, 1);

                    adminOrder1.addItem(adminItem1);
                    adminOrder1.addItem(adminItem2);

                    orderRepository.save(adminOrder1);

                    Order adminOrder2 = new Order();
                    adminOrder2.setUser(admin);
                    adminOrder2.setStatus(OrderStatus.SHIPPED);
                    adminOrder2.setShippingAddress("456 Admin Road");
                    adminOrder2.setOrderDate(LocalDateTime.now().minusDays(2));
                    adminOrder2.setTrackingNumber("TRACK-345-678");

                    OrderItem adminItem3 = new OrderItem(rod, 2);
                    adminOrder2.addItem(adminItem3);

                    orderRepository.save(adminOrder2);

                    Order adminOrderProcessing = new Order();
                    adminOrderProcessing.setUser(admin);
                    adminOrderProcessing.setStatus(OrderStatus.PROCESSING);
                    adminOrderProcessing.setShippingAddress("456 Admin Road");
                    adminOrderProcessing.setOrderDate(LocalDateTime.now().minusHours(12));
                    adminOrderProcessing.setTrackingNumber("TRACK-PROC-123");
                    OrderItem processingItem = new OrderItem(electronics, 1);
                    adminOrderProcessing.addItem(processingItem);

                    Order adminOrderCancelled = new Order();
                    adminOrderCancelled.setUser(admin);
                    adminOrderCancelled.setStatus(OrderStatus.CANCELLED);
                    adminOrderCancelled.setShippingAddress("456 Admin Road");
                    adminOrderCancelled.setOrderDate(LocalDateTime.now().minusDays(15));
                    adminOrderCancelled.setTrackingNumber("TRACK-CANC-456");
                    OrderItem cancelledItem = new OrderItem(clothing, 2);
                    adminOrderCancelled.addItem(cancelledItem);

                    orderRepository.save(adminOrderProcessing);
                    orderRepository.save(adminOrderCancelled);

                    logger.info("Orders and order items initialized successfully");
                } catch (Exception e) {
                    logger.error("Error initializing orders: " + e.getMessage());
                    throw e;
                }
            }

            // Create fishing permits if none exist
            if (permitRepository.count() == 0) {
                logger.info("Initializing permits...");
                User customer = userRepository.findByEmail("customer@example.com").orElseThrow();
                User admin = userRepository.findByEmail("admin@fishing.com").orElseThrow();

                FishingPermit customerPermit = new FishingPermit();
                customerPermit.setUser(customer);
                customerPermit.setPermitType(PermitType.ANNUAL);
                customerPermit.setStartDate(LocalDate.now());
                customerPermit.setEndDate(LocalDate.now().plusYears(1));
                customerPermit.setStatus(PermitStatus.APPROVED);
                customerPermit.setPrice(new BigDecimal("150.00"));
                customerPermit.setNotes("Annual fishing permit for all waters");

                permitRepository.save(customerPermit);

                // Add permit for admin
                FishingPermit adminPermit = new FishingPermit();
                adminPermit.setUser(admin);
                adminPermit.setPermitType(PermitType.LIFETIME);
                adminPermit.setStartDate(LocalDate.now().minusYears(1));
                adminPermit.setEndDate(LocalDate.now().plusYears(99));
                adminPermit.setStatus(PermitStatus.APPROVED);
                adminPermit.setPrice(new BigDecimal("1000.00"));
                adminPermit.setNotes("Lifetime fishing permit with all privileges");

                FishingPermit adminPendingPermit = new FishingPermit();
                adminPendingPermit.setUser(admin);
                adminPendingPermit.setPermitType(PermitType.DAILY);
                adminPendingPermit.setStartDate(LocalDate.now().plusDays(5));
                adminPendingPermit.setEndDate(LocalDate.now().plusDays(6));
                adminPendingPermit.setStatus(PermitStatus.PENDING);
                adminPendingPermit.setPrice(new BigDecimal("15.00"));
                adminPendingPermit.setNotes("Day fishing permit pending approval");

                FishingPermit adminExpiredPermit = new FishingPermit();
                adminExpiredPermit.setUser(admin);
                adminExpiredPermit.setPermitType(PermitType.MONTHLY);
                adminExpiredPermit.setStartDate(LocalDate.now().minusMonths(2));
                adminExpiredPermit.setEndDate(LocalDate.now().minusMonths(1));
                adminExpiredPermit.setStatus(PermitStatus.EXPIRED);
                adminExpiredPermit.setPrice(new BigDecimal("45.00"));
                adminExpiredPermit.setNotes("Expired monthly permit");

                FishingPermit adminRejectedPermit = new FishingPermit();
                adminRejectedPermit.setUser(admin);
                adminRejectedPermit.setPermitType(PermitType.WEEKLY);
                adminRejectedPermit.setStartDate(LocalDate.now().minusDays(10));
                adminRejectedPermit.setEndDate(LocalDate.now().minusDays(3));
                adminRejectedPermit.setStatus(PermitStatus.REJECTED);
                adminRejectedPermit.setPrice(new BigDecimal("25.00"));
                adminRejectedPermit.setNotes("Rejected due to incomplete information");

                permitRepository.save(adminPermit);
                permitRepository.save(adminPendingPermit);
                permitRepository.save(adminExpiredPermit);
                permitRepository.save(adminRejectedPermit);

                logger.info("Permits initialized successfully");
            }

            // Create contests if none exist
            if (contestRepository.count() == 0) {
                logger.info("Initializing contests...");
                Contest contest = Contest.builder()
                        .name("Spring Fishing Championship")
                        .description("Annual spring fishing competition with prizes for biggest catch")
                        .startDate(LocalDateTime.now().plusDays(30))
                        .endDate(LocalDateTime.now().plusDays(31))
                        .location("Lake Superior")
                        .maxParticipants(50)
                        .entryFee(new BigDecimal("25.00"))
                        .status(ContestStatus.UPCOMING)
                        .build();

                Contest contestInProgress = Contest.builder()
                        .name("Summer Bass Tournament")
                        .description("Catch the biggest bass this summer")
                        .startDate(LocalDateTime.now().minusDays(1))
                        .endDate(LocalDateTime.now().plusDays(5))
                        .location("Lake Michigan")
                        .maxParticipants(100)
                        .entryFee(new BigDecimal("50.00"))
                        .status(ContestStatus.IN_PROGRESS)
                        .build();

                Contest contestCompleted = Contest.builder()
                        .name("Spring Trout Derby")
                        .description("Annual trout fishing competition")
                        .startDate(LocalDateTime.now().minusMonths(1))
                        .endDate(LocalDateTime.now().minusMonths(1).plusDays(2))
                        .location("Rocky River")
                        .maxParticipants(75)
                        .entryFee(new BigDecimal("35.00"))
                        .status(ContestStatus.COMPLETED)
                        .build();

                Contest contestCancelled = Contest.builder()
                        .name("Winter Ice Fishing")
                        .description("Cancelled due to thin ice conditions")
                        .startDate(LocalDateTime.now().plusMonths(2))
                        .endDate(LocalDateTime.now().plusMonths(2).plusDays(1))
                        .location("Frozen Lake")
                        .maxParticipants(30)
                        .entryFee(new BigDecimal("40.00"))
                        .status(ContestStatus.CANCELLED)
                        .build();

                contestRepository.save(contest);
                contestRepository.save(contestInProgress);
                contestRepository.save(contestCompleted);
                contestRepository.save(contestCancelled);

                // Create contest registration
                if (contestRegistrationRepository.count() == 0) {
                    logger.info("Initializing contest registrations...");
                    User customer = userRepository.findByEmail("customer@example.com").orElseThrow();
                    User admin = userRepository.findByEmail("admin@fishing.com").orElseThrow();

                    ContestRegistration customerRegistration = ContestRegistration.builder()
                            .contest(contest)
                            .user(customer)
                            .status(RegistrationStatus.CONFIRMED)
                            .build();

                    ContestRegistration adminRegistration = ContestRegistration.builder()
                            .contest(contest)
                            .user(admin)
                            .status(RegistrationStatus.CONFIRMED)
                            .build();

                    ContestRegistration adminPendingReg = ContestRegistration.builder()
                            .contest(contestInProgress)
                            .user(admin)
                            .status(RegistrationStatus.PENDING)
                            .build();

                    ContestRegistration adminCancelledReg = ContestRegistration.builder()
                            .contest(contestCompleted)
                            .user(admin)
                            .status(RegistrationStatus.CANCELLED)
                            .build();

                    contestRegistrationRepository.save(customerRegistration);
                    contestRegistrationRepository.save(adminRegistration);
                    contestRegistrationRepository.save(adminPendingReg);
                    contestRegistrationRepository.save(adminCancelledReg);
                    logger.info("Contest registrations initialized successfully");
                }
                logger.info("Contests initialized successfully");
            }
        };
    }
}