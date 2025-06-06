package com.minh.simple_typing_game.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.minh.simple_typing_game.entity.User;
import com.minh.simple_typing_game.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login/success")
    public RedirectView loginSuccess(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            
            // Determine OAuth provider
            String provider = determineProvider(oauth2User);
            
            try {
                // Create or update user in database
                User user = authService.createOrUpdateOAuth2User(oauth2User, provider);
                
                // Store user info in session
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("authType", "OAUTH2");
                session.setAttribute("provider", provider);
                
                log.info("OAuth2 user successfully authenticated: {} via {}", user.getEmail(), provider);
                
                // Redirect to frontend with success
                return new RedirectView("http://localhost:5173/game?login=success&userId=" + user.getId());
                
            } catch (Exception e) {
                // Log error and redirect with error message
                log.error("Failed to save OAuth2 user: {}", e.getMessage(), e);
                return new RedirectView("http://localhost:5173/login?error=oauth_save_failed");
            }
        }
        
        return new RedirectView("http://localhost:5173/login?error=auth_failed");
    }
      @GetMapping("/user")
    @ResponseBody
    public ResponseEntity<User> getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        log.debug("Checking current user, session exists: {}", session != null);
        
        if (session != null) {
            User user = (User) session.getAttribute("user");
            log.debug("User in session: {}", user != null ? user.getEmail() : "null");
            if (user != null) {
                return ResponseEntity.ok(user);
            }
        }
        
        log.debug("No authenticated user found in session");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    
    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            log.info("Logging out user, invalidating session: {}", session.getId());
            session.invalidate(); // Clear the session
        }
        
        log.info("User logged out successfully");
        return ResponseEntity.ok().body("Logged out successfully");
    }

    private String determineProvider(OAuth2User oauth2User) {
        // GitHub has 'login' attribute, Google has 'sub'
        if (oauth2User.getAttribute("login") != null) {
            return "GITHUB";
        } else if (oauth2User.getAttribute("sub") != null) {
            return "GOOGLE";
        }
        return "UNKNOWN";
    }
}
