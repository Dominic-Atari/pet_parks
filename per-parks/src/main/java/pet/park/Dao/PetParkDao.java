package pet.park.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import pet.park.entity.PetPark;

public interface PetParkDao extends JpaRepository<PetPark, Long> {

}
