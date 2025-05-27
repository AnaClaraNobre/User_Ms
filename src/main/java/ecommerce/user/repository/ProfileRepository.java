package ecommerce.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ecommerce.user.models.ProfileModel;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileModel, Long> {
    boolean existsByAuthUserId(Long authUserId);
    Optional<ProfileModel> findByAuthUserId(Long authUserId);


}
