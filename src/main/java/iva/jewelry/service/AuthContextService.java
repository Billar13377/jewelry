package iva.jewelry.service;

import iva.jewelry.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthContextService {

    private final UserService userService;  // ← Используем UserService с кэшем!

    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            // Теперь используем кэшируемый метод!
            return userService.findByEmail(email);
        }
        throw new IllegalStateException("Ошибка аутентификации");
    }
}