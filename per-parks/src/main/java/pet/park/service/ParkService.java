package pet.park.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Data;
import pet.park.Dao.ContributorDao;
import pet.park.controller.model.ContributorData;
import pet.park.entity.Contributor;

@Service
@Data
public class ParkService {

	@Autowired
	private ContributorDao contributorDao;
	
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
			
		
		if(Objects.isNull(contributorId)) {
			Optional<Contributor> opContrib = contributorDao.findByContributorEmail(contributorEmail);
			
			if(opContrib.isPresent()) {
				throw new DuplicateKeyException(
						"Contributor with email " + contributorEmail + "already exist.");
			}
			
			contributor = new Contributor();
			
			}else {
				contributor = findContributorById(contributorId);
			}
		return contributor;
		}

	

	private Contributor findContributorById(Long contributorId) {
		return contributorDao.findById(contributorId).
				orElseThrow(() -> new NoSuchElementException(
						"Contributor with ID=" + contributorId + "was not found."));
	}
		// changes made to retrieve data (Get).
	
	@Transactional(readOnly = true)
	public List<ContributorData> retriveAllContributors() {
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
		
		//@formatter:off
		return contributorDao.findAll()
			.stream()
			.map(ContributorData::new).
			toList();
	
		//formatter:on
	}

		//implementing method from parkController to retrieve contributor by ID.
	@Transactional(readOnly = true)
	public ContributorData retirveContributorById(Long contributorId) {
		Contributor contributor = findContributorById(contributorId);
		return new ContributorData(contributor);
		
	}
	@Transactional(readOnly = true)
	public void deleteContributorById(Long contributorId) {
		Contributor contributor = findContributorById(contributorId);
		contributorDao.delete(contributor);
		
	}

}
