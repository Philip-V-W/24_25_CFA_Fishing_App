//package cfa.fishing.fishing_store_app.controller.web;
//
//import cfa.fishing.fishing_store_app.dto.request.RegisterRequest;
//import cfa.fishing.fishing_store_app.service.auth.AuthenticationService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.ui.Model;
//
//@Controller
//@RequestMapping("/auth")
//@RequiredArgsConstructor
//public class AuthController {
//    private final AuthenticationService authenticationService;
//
//    @GetMapping("/register")
//    public String showRegisterForm(Model model) {
//        model.addAttribute("registerRequest", new RegisterRequest());
//        return "public/register";
//    }
//
//    @PostMapping("/register")
//    public String register(@Valid @ModelAttribute("registerRequest") RegisterRequest request,
//                           BindingResult result,
//                           Model model) {
//        if (result.hasErrors()) {
//            return "public/register";
//        }
//
//        try {
//            authenticationService.register(request);
//            return "redirect:/login?registered";
//        } catch (Exception e) {
//            model.addAttribute("error", e.getMessage());
//            return "public/register";
//        }
//    }
//}