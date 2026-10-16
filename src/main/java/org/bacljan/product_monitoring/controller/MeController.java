package org.bacljan.product_monitoring.controller;

import org.bacljan.product_monitoring.DTO.MeDto;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MeController {

    @GetMapping("/me")
    public MeDto me(Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream()
                .map(a -> {
                    String role = a.getAuthority();
                    return role != null && role.startsWith("ROLE_") ? role.substring(5) : role;
                })
                .toList();
        return new MeDto(authentication.getName(), roles);
    }
}
