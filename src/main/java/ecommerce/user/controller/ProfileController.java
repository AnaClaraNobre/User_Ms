package ecommerce.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.user.dtos.ProfileDTO;
import ecommerce.user.service.ProfileService;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    public ResponseEntity<ProfileDTO> createProfile(@RequestBody ProfileDTO profileDTO) {
        Long authUserId = getAuthenticatedUserId(); 
        profileDTO.setAuthUserId(authUserId);       

        ProfileDTO created = profileService.createProfile(profileDTO);
        return ResponseEntity.ok(created);
    }


    @GetMapping("/me")
    public ResponseEntity<ProfileDTO> getMyProfile() {
        Long authUserId = getAuthenticatedUserId();
        if (authUserId == null) {
            return ResponseEntity.status(401).build();
        }

        return profileService.findByAuthUserId(authUserId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    private Long getAuthenticatedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof Long) {
            return (Long) principal;
        }

        if (principal instanceof String stringPrincipal) {
            try {
                return Long.parseLong(stringPrincipal);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }

    
    @PutMapping("/me/default-address/{addressId}")
    public ResponseEntity<Void> setDefaultAddress(@PathVariable Long addressId) {
        Long authUserId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean updated = profileService.setDefaultAddress(authUserId, addressId);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    
    @PutMapping("/me")
    public ResponseEntity<ProfileDTO> updateMyProfile(@RequestBody ProfileDTO profileDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof Long authUserId) {
            ProfileDTO updated = profileService.updateProfile(authUserId, profileDTO);
            return ResponseEntity.ok(updated);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


}
