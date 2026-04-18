package iva.jewelry.service;

import iva.jewelry.model.Role;
import iva.jewelry.repository.RoleRepository;
import iva.jewelry.repository.UserRepository;
import iva.jewelry.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Getter
@Setter
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public List<User> allUsers() {
            return userRepository.findAll();
}

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow();
    }

    @Transactional
    public User addRoleToUser(Integer userId, String roleName) {
        User user = getUserById(userId);

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException("Роль не найдена"));

        user.getRoles().add(role);
        return userRepository.save(user);
    }

    @Transactional
    public User removeRoleFromUser(Integer userId, String roleName) {
        User user = getUserById(userId);

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException("Роль не найдена"));

        user.getRoles().remove(role);
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Integer userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }

    @Transactional
    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Пользователь не найден"));
    }
}
