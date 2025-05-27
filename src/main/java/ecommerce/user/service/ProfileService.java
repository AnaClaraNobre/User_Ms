package ecommerce.user.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ecommerce.user.dtos.ProfileDTO;
import ecommerce.user.models.ProfileModel;
import ecommerce.user.repository.AddressRepository;
import ecommerce.user.repository.ProfileRepository;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final AddressRepository addressRepository;

    public ProfileService(ProfileRepository profileRepository, AddressRepository addressRepository) {
        this.profileRepository = profileRepository;
        this.addressRepository = addressRepository;
    }

    public ProfileDTO createProfile(ProfileDTO dto) {
        if (profileRepository.existsByAuthUserId(dto.getAuthUserId())) {
            throw new IllegalArgumentException("Já existe um perfil vinculado ao AUTH_USER_ID: " + dto.getAuthUserId());
        }

        if (dto.getDefaultAddressId() != null) {
            boolean isValid = addressRepository.findById(dto.getDefaultAddressId())
                    .map(addr -> addr.getAuthUserId().equals(dto.getAuthUserId()))
                    .orElse(false);
            if (!isValid) {
                throw new IllegalArgumentException("Endereço não pertence ao usuário autenticado.");
            }
        }

        ProfileModel model = mapToModel(dto);
        model.setCreatedAt(LocalDateTime.now());
        model.setUpdatedAt(LocalDateTime.now());

        ProfileModel saved = profileRepository.save(model);
        return mapToDTO(saved);
    }

    public Optional<ProfileDTO> findByAuthUserId(Long authUserId) {
        return profileRepository.findByAuthUserId(authUserId).map(this::mapToDTO);
    }
    
    public boolean setDefaultAddress(Long authUserId, Long addressId) {
        if (!addressRepository.findById(addressId)
                .map(addr -> addr.getAuthUserId().equals(authUserId))
                .orElse(false)) {
            return false;
        }

        Optional<ProfileModel> profileOpt = profileRepository.findByAuthUserId(authUserId);
        if (profileOpt.isPresent()) {
            ProfileModel profile = profileOpt.get();
            profile.setDefaultAddressId(addressId);
            profile.setUpdatedAt(LocalDateTime.now());
            profileRepository.save(profile);
            return true;
        }

        return false;
    }
    
    public ProfileDTO updateProfile(Long authUserId, ProfileDTO dto) {
        ProfileModel existingProfile = profileRepository.findByAuthUserId(authUserId)
            .orElseThrow(() -> new IllegalArgumentException("Perfil não encontrado para o AUTH_USER_ID: " + authUserId));

        if (dto.getDefaultAddressId() != null) {
            boolean isValid = addressRepository.findById(dto.getDefaultAddressId())
                .map(addr -> addr.getAuthUserId().equals(authUserId))
                .orElse(false);
            if (!isValid) {
                throw new IllegalArgumentException("Endereço informado não pertence ao usuário autenticado.");
            }
        }

        existingProfile.setUsername(dto.getUsername());
        existingProfile.setCpf(dto.getCpf());
        existingProfile.setBirthdate(dto.getBirthday());
        existingProfile.setPhoneNumber(dto.getPhone());
        existingProfile.setEmail(dto.getEmail());
        existingProfile.setUpdatedAt(LocalDateTime.now());
        existingProfile.setDefaultAddressId(dto.getDefaultAddressId());

        ProfileModel updated = profileRepository.save(existingProfile);
        return mapToDTO(updated);
    }

    private ProfileDTO mapToDTO(ProfileModel model) {
        ProfileDTO dto = new ProfileDTO();
        dto.setId(model.getId());
        dto.setUsername(model.getUsername());
        dto.setCpf(model.getCpf());
        dto.setBirthday(model.getBirthdate());
        dto.setPhone(model.getPhoneNumber());
        dto.setCreateDate(model.getCreatedAt());
        dto.setUpdateDate(model.getUpdatedAt());
        dto.setDefaultAddressId(model.getDefaultAddressId());
        dto.setAuthUserId(model.getAuthUserId());
        dto.setEmail(model.getEmail());
        return dto;
    }

    private ProfileModel mapToModel(ProfileDTO dto) {
        ProfileModel model = new ProfileModel();
        model.setId(dto.getId());
        model.setUsername(dto.getUsername());
        model.setCpf(dto.getCpf());
        model.setBirthdate(dto.getBirthday());
        model.setPhoneNumber(dto.getPhone());
        model.setCreatedAt(dto.getCreateDate());
        model.setUpdatedAt(dto.getUpdateDate());
        model.setDefaultAddressId(dto.getDefaultAddressId());
        model.setAuthUserId(dto.getAuthUserId());
        model.setEmail(dto.getEmail());
        return model;
    }
}
