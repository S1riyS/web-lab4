package ru.s1riys.lab4.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.s1riys.lab4.domain.dto.JwtAuthResponse;
import ru.s1riys.lab4.domain.dto.SignInRequest;
import ru.s1riys.lab4.domain.dto.SignUpRequest;
import ru.s1riys.lab4.domain.entity.Permission;
import ru.s1riys.lab4.domain.entity.User;
import ru.s1riys.lab4.exceptions.BadRequestException;
import ru.s1riys.lab4.repository.PermissionRepository;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthResponse signUp(SignUpRequest request) {
        // Check if user with this username already exists
        if (userService.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Sign up error", "User with this username already exists");
        }

        // Assign default permissions
        Set<Permission> defaultPermissions = new HashSet<>();
        defaultPermissions.add(permissionRepository.findByName(Permission.DOT_CREATE));
        defaultPermissions.add(permissionRepository.findByName(Permission.DOT_READ));
        defaultPermissions.add(permissionRepository.findByName(Permission.DOT_DELETE));

        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .permissions(defaultPermissions)
                .build();

        userService.create(user);

        String jwt = jwtService.generateToken(user);
        return new JwtAuthResponse(jwt);
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthResponse signIn(SignInRequest request) {
        // Trying to authenticate user
        try {
            var authToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
            authenticationManager.authenticate(authToken);
        } catch (AuthenticationException exc) {
            throw new BadRequestException("Bad credentials", "Wrong username or password");
        }

        // Generate token
        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());
        String jwt = jwtService.generateToken(user);

        return new JwtAuthResponse(jwt);
    }
}
