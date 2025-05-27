package ecommerce.user.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import ecommerce.user.dtos.AddressDTO;
import ecommerce.user.models.AddressModel;
import ecommerce.user.models.ProfileModel;
import ecommerce.user.repository.AddressRepository;
import ecommerce.user.repository.ProfileRepository;

@Service
public class AdressService {

    private final AddressRepository repository;
    private final ProfileRepository profileRepository;

    public AdressService(AddressRepository repository, ProfileRepository profileRepository) {
        this.repository = repository;
        this.profileRepository = profileRepository;
    }

    public AddressDTO create(AddressDTO dto) {
        AddressModel model = toModel(dto);
        AddressModel saved = repository.save(model);
        return toDTO(saved);
    }

    public Optional<AddressDTO> getById(Long id) {
        return repository.findById(id).map(this::toDTO);
    }

    public List<AddressDTO> getByAuthUserId(Long authUserId) {
        return repository.findByAuthUserId(authUserId)
                         .stream()
                         .map(this::toDTO)
                         .collect(Collectors.toList());
    }

    public Optional<AddressDTO> update(Long id, AddressDTO dto) {
        return repository.findById(id).map(existing -> {
            existing.setStreet(dto.getStreet());
            existing.setNumber(dto.getNumber());
            existing.setComplement(dto.getComplement());
            existing.setNeighborhood(dto.getNeighborhood());
            existing.setCity(dto.getCity());
            existing.setState(dto.getState());
            existing.setZipCode(dto.getZipCode());
            existing.setCountry(dto.getCountry());
            return toDTO(repository.save(existing));
        });
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

	private AddressModel toModel(AddressDTO dto) {
		AddressModel model = new AddressModel();
		model.setId(dto.getId());
		model.setAuthUserId(dto.getAuthUserId());
		model.setStreet(dto.getStreet());
		model.setNumber(dto.getNumber());
		model.setComplement(dto.getComplement());
		model.setNeighborhood(dto.getNeighborhood());
		model.setCity(dto.getCity());
		model.setState(dto.getState());
		model.setZipCode(dto.getZipCode());
		ProfileModel profile = profileRepository.findByAuthUserId(dto.getAuthUserId()).orElseThrow(
				() -> new IllegalArgumentException("Perfil não encontrado para o usuário: " + dto.getAuthUserId()));

		model.setProfile(profile);
		model.setCountry(dto.getCountry());
		return model;
	}

    private AddressDTO toDTO(AddressModel model) {
        AddressDTO dto = new AddressDTO();
        dto.setId(model.getId());
        dto.setAuthUserId(model.getAuthUserId());
        dto.setStreet(model.getStreet());
        dto.setNumber(model.getNumber());
        dto.setComplement(model.getComplement());
        dto.setNeighborhood(model.getNeighborhood());
        dto.setCity(model.getCity());
        dto.setState(model.getState());
        dto.setZipCode(model.getZipCode());
        dto.setCountry(model.getCountry());

        return dto;
    }
}
