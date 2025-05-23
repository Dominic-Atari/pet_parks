package pet.park.controller.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;
import pet.park.controller.model.ContributorData.PetparkResponse;
import pet.park.entity.Amenity;
import pet.park.entity.Contributor;
import pet.park.entity.GeoLocation;
import pet.park.entity.PetPark;
@Data
@NoArgsConstructor
public class PetParkData {
	
	private Long petParkId;
	private String petParkName;
	private String directions;
	private String stateOrProvince;
	private String country;
	private GeoLocation geoLocation;
	private PetParkContributor contributor;
	private Set<String> amenities = new HashSet<>();
	
	public PetParkData(PetPark petPark) {
		petParkId = petPark.getPetParkId();
		petParkName = petPark.getPetParkName();
		directions = petPark.getDirections();
		stateOrProvince = petPark.getStateOrProvince();
		country = petPark.getCountry();
		geoLocation = petPark.getGeoLocation();
		contributor = new PetParkContributor(petPark.getContributor());
		
		for(Amenity amenity : petPark.getAmenities()) {
			amenities.add(amenity.getAmenity());
		}
	}

@Data
@NoArgsConstructor
public static class PetParkContributor{
	private Long contributorId;
	private String contributorName;
	private String contributorEmail;
	//private Set<PetparkResponse> petParks = new HashSet<>();
	
	public PetParkContributor(Contributor contributor) {
		contributorId = contributor.getContributorId();
		contributorName = contributor.getContributorName();
		contributorEmail = contributor.getContributorEmail();
		
		/*for(PetPark petPark : contributor.getPetParks()) {
			petParks.add(petPark.());
		}*/
	}
	
	
	}
	
}