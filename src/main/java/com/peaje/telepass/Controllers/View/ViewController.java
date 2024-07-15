package com.peaje.telepass.Controllers.View;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/auth")
    public String authPage() {
        return "index";
    }
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
    @GetMapping("/GestionUsuario")
    public String GestionUsuario() {
        return "GestionUsuario";
    }

    @GetMapping("/usuario")
    public String usuario() {
        return "usuario";
    }

}
