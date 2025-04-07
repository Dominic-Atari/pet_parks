package pet.park.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Data;
import pet.park.Dao.AmenityDao;
import pet.park.Dao.ContributorDao;
import pet.park.Dao.PetParkDao;
import pet.park.controller.model.ContributorData;
import pet.park.controller.model.PetParkData;
import pet.park.entity.Amenity;
import pet.park.entity.Contributor;
import pet.park.entity.PetPark;

@Service
@Data
public class ParkService {

	@Autowired
	private ContributorDao contributorDao;

	@Autowired
	private AmenityDao amenityDao;

	@Autowired
	private PetParkDao petParkDao;

	@Transactional(readOnly = false)
	public ContributorData saveContributor(ContributorData contributorData) {
		Long contributorId = contributorData.getContributorId();
		Contributor contributor = findOrCreateContributor(contributorId, contributorData.getContributorEmail());

		setFieldsInContributor(contributor, contributorData);
		return new ContributorData(contributorDao.save(contributor));
	}

	private void setFieldsInContributor(Contributor contributor, ContributorData contributorData) {
		contributor.setContributorEmail(contributorData.getContributorEmail());
		contributor.setContributorName(contributorData.getContributorName());

	}

	private Contributor findOrCreateContributor(Long contributorId, String contributorEmail) {
		Contributor contributor;

		if (Objects.isNull(contributorId)) {
			Optional<Contributor> opContrib = contributorDao.findByContributorEmail(contributorEmail);

			if (opContrib.isPresent()) {
				throw new DuplicateKeyException("Contributor with email " + contributorEmail + "already exist.");
			}

			contributor = new Contributor();

		} else {
				contributor = findContributorById(contributorId);
		}
		return contributor;
	}

	private Contributor findContributorById(Long contributorId) {
		return contributorDao.findById(contributorId).orElseThrow(
				() -> new NoSuchElementException("Contributor with ID=" + contributorId + "was not found."));
	}
	// changes made to retrieve data (Get).

	@Transactional(readOnly = true)
	public List<ContributorData> retriveAllContributors() {
		
		//@formatter:off
		/*
		 * ONE WAY TO RETRIEVE CONTRIBUTERS FRON CONTRIBUTER.
		 * 
		 * List<Contributor> contributors = contributorDao.findAll();
		 * List<ContributorData> response = new LinkedList<>();
		 * 
		 * for(Contributor contributor : contributors) { response.add(new
		 * ContributorData(contributor)); }
		 * 
		 * return response;
		 */

		// SECOND WAY TO RETRIEVE CONTRIBUTERS FROM CONTRIBUTER.

		
		return contributorDao.findAll()
			.stream()
			.map(ContributorData::new).
			toList();
	
		//formatter:on
	}

		//implementing method from parkController
		//to retrieve contributor by ID.
	@Transactional(readOnly = true)
	public ContributorData retrieveContributorById(Long contributorId) {
		Contributor contributor = findContributorById(contributorId);
		return new ContributorData(contributor);
		
	}
	@Transactional(readOnly = false)
	public void deleteContributorById(Long contributorId) {
		Contributor contributor = findContributorById(contributorId);
		contributorDao.delete(contributor);
		
//--------------------------------------------------------------------		
	
	
	}
	@Transactional(readOnly = false)
	public PetParkData savePetPark(Long contributorId, 
			PetParkData petParkData) {
		Contributor contributor = findContributorById(contributorId);
		
		Set<Amenity> amenities = 
				amenityDao.findAllByAmenityIn(petParkData.getAmenities());
	
		 PetPark petPark = findOrCreatePetPark(petParkData.getPetParkId());
		setPetParkFields(petPark, petParkData);
		
		//set relationships every where
		
		petPark.setContributor(contributor);
		contributor.getPetParks().add(petPark);
		
		for(Amenity amenity : amenities) {
			amenity.getPetParks().add(petPark);
			petPark.getAmenities().add(amenity);
		}
		
		PetPark dbPetPark = petParkDao.save(petPark);
		return new PetParkData(dbPetPark);
		
	}

	private void setPetParkFields(PetPark petPark, PetParkData petParkData) {
		petPark.setCountry(petParkData.getCountry());
		petPark.setDirections(petPark.getDirections());
		petPark.setGeoLocation(petPark.getGeoLocation());
		petPark.setParkName(petPark.getParkName());
		petPark.setPetParkId(petPark.getPetParkId());
		petPark.setStateOrProvince(petPark.getStateOrProvince());
	
	}

	private PetPark findOrCreatePetPark(Long petParkId) {
		PetPark petPark;
		
		if(Objects.isNull(petParkId)) {
			petPark = new PetPark();
		}
		else {
			petPark = findPetParkById(petParkId);
		}
		return petPark;
	}

	private PetPark findPetParkById(Long petParkId) {
		return petParkDao.findById(petParkId).
				orElseThrow(() -> new NoSuchElementException(
					"Pet Park with ID= " + petParkId + " does not exist."));
		
	}

	@Transactional(readOnly = true)
	public PetParkData retrievePetParkById(Long contributorId, Long parkId) {
		findContributorById(contributorId);
		PetPark petPark = findPetParkById(parkId);
		
		if(petPark.getContributor().getContributorId() != contributorId) {
			throw new IllegalStateException("Pet park with ID= " 
		+ parkId + "is not owned by contributor " 
					+ contributorId);
		}
		
		return new PetParkData(petPark);
	}

}
