package ecommerce.user.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "ADDRESS", schema = "user_ms")
public class AddressModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ADDRESS_ID")
	@SequenceGenerator(name = "SEQ_ADDRESS_ID",     sequenceName = "user_ms.SEQ_ADDRESS_ID", allocationSize = 1)
	@Column(name = "ID", nullable = false)
	private Long id;

	@Column(name = "STREET", nullable = false)
	private String street;

	@Column(name = "AUTH_USER_ID", nullable = false)
	private Long authUserId;
	
	@Column(name = "NUMBER", nullable = false)
	private String number;

	@Column(name = "COMPLEMENT")
	private String complement;

	@Column(name = "NEIGHBORHOOD", nullable = false)
	private String neighborhood;

	@Column(name = "CITY", nullable = false)
	private String city;

	@Column(name = "STATE", nullable = false)
	private String state;

	@Column(name = "ZIP_CODE", nullable = false)
	private String zipCode;

	@Column(name = "COUNTRY", nullable = false)
	private String country;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PROFILE_ID", nullable = false)
	private ProfileModel profile;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Long getAuthUserId() {
		return authUserId;
	}

	public void setAuthUserId(Long authUserId) {
		this.authUserId = authUserId;
	}

	public ProfileModel getProfile() {
		return profile;
	}

	public void setProfile(ProfileModel profile) {
		this.profile = profile;
	}

}
