package com.peaje.telepass.Controllers.RecuperarEmail;
// controlador para cambiar contraseña
import com.peaje.telepass.Services.Email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RecuperarEmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/recuperar-email")
    public void solicitarRecuperacion(@RequestParam String email) {
        String subject = "Recuperación de Contraseña";
        String body = "<p>Para cambiar su contraseña, haga clic en el siguiente enlace:</p>" +
                      "<a href=\"http://localhost:8080/reset-password/cambiar-contrasena?email=" + email + "\">Cambiar Contraseña</a>";
        emailService.sendEmail(email, subject, body);
    }
}
