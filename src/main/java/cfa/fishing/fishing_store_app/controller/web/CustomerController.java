//package cfa.fishing.fishing_store_app.controller.web;
//
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequestMapping("/user")
//@PreAuthorize("hasRole('CUSTOMER')")
//public class CustomerController {
//    @GetMapping("/profile")
//    public String profile() {
//        return "user/profile";
//    }
//
//    @GetMapping("/orders")
//    public String orders() {
//        return "user/orders";
//    }
//
//    @GetMapping("/permits")
//    public String permits() {
//        return "user/permits";
//    }
//
//    @GetMapping("/contests")
//    public String contests() {
//        return "user/contests";
//    }
//}