package cfa.fishing.fishing_store_app.controller.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    @GetMapping("")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/products")
    public String products() {
        return "admin/products";
    }

    @GetMapping("/orders")
    public String orders() {
        return "admin/orders";
    }

    @GetMapping("/customers")
    public String customers() {
        return "admin/customers";
    }

    @GetMapping("/permits")
    public String permits() {
        return "admin/permits";
    }

    @GetMapping("/contests")
    public String contests() {
        return "admin/contests";
    }
}