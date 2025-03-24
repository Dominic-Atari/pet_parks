package pet.park.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.park.controller.model.ContributorData;
import pet.park.service.ParkService;

@RestController
@RequestMapping("/pet_park")
@Slf4j
public class PetController {
	
		private static final String contributorData = null;
		@Autowired
		private ParkService parkService;

		@PostMapping("/contributor")
		@ResponseStatus(code = HttpStatus.CREATED)
			public ContributorData insertContributor (
					@RequestBody ContributorData contributorData) {
				log.info("Creating contributor {}", contributorData);
				return parkService.saveContributor(contributorData);
				
				
	}
		
		/*update contributor*/
		
		@PutMapping("/contributor/{contributorId}")
		public ContributorData updateContributor(@PathVariable Long contributorId,
				@RequestBody ContributorData contributorData) {
			contributorData.setContributorId(contributorId);
			log.info("Updating contributor {}", contributorData);
			return parkService.saveContributor(contributorData);
		}
		//retrieving all contributors (Get).
		
		@GetMapping("/contributor")
		public List<ContributorData> retriveAllContributors(){
			log.info("Retrive all contributors called");
			return parkService.retriveAllContributors();
		}
		
		// retrieve single contributor by ID.
		
		@GetMapping("/contributor/{contributorId}")
		public ContributorData retrieveContributorById(@PathVariable Long contributorId) {
			log.info("Retrieving contributor by ID = {}", contributorId);
			return parkService.retirveContributorById(contributorId);
		}
		
		// delete a contributor
		
		@DeleteMapping("/contributor")
		public void deleteAllContributors(){
			log.info("Attempting to delete all contrubutors");
			throw new UnsupportedOperationException("Deleting all contributors is not allowed.");
		}
		
		@DeleteMapping("/contributor/{contributorId}")
		public Map<String, String> deleteContributorById(
				@PathVariable Long contributorId){
			log.info("Deleting contributor by ID={}", contributorId);
			
			parkService.deleteContributorById(contributorId);
			
			return Map.of("message", "Deletion of contributor by ID= " + contributorId + "was successfull.");
		}
}
