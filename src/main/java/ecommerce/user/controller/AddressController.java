package ecommerce.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import ecommerce.user.dtos.AddressDTO;
import ecommerce.user.service.AdressService;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final AdressService addressService;

    public AddressController(AdressService addressService) {
        this.addressService = addressService;
    }

    private Long getAuthenticatedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (principal instanceof Long) ? (Long) principal : null;
    }

    @PostMapping
    public ResponseEntity<AddressDTO> create(@RequestBody AddressDTO dto) {
        dto.setAuthUserId(getAuthenticatedUserId());
        return ResponseEntity.ok(addressService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAllForUser() {
        Long authUserId = getAuthenticatedUserId();
        return ResponseEntity.ok(addressService.getByAuthUserId(authUserId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getById(@PathVariable Long id) {
        return addressService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDTO> update(@PathVariable Long id, @RequestBody AddressDTO dto) {
        return addressService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
