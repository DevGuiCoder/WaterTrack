package com.example.demo.controller;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/verify")
    public String verifyToken(@RequestHeader("Authorization") String token)
            throws FirebaseAuthException {

        // Remove o prefixo "Bearer " do token
        String idToken = token.replace("Bearer ", "");

        // Valida o token e retorna o UID do usuário
        return FirebaseAuth.getInstance()
                .verifyIdToken(idToken)
                .getUid();
    }


    @PostMapping("/create-user")
    public String createUser(
            @RequestParam String email,
            @RequestParam String password
    ) throws FirebaseAuthException {

        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);

        UserRecord user = FirebaseAuth.getInstance().createUser(request);
        return "Usuário criado com UID: " + user.getUid();
    }

}
