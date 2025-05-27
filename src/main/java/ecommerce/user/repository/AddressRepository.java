package ecommerce.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ecommerce.user.models.AddressModel;

@Repository
public interface AddressRepository extends JpaRepository<AddressModel, Long> {
    List<AddressModel> findByAuthUserId(Long authUserId);

}
