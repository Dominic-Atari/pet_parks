package pet.park.entity;

import java.math.BigDecimal;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor


public class GeoLocation {
	// instance variables
	private BigDecimal latitude;
	private BigDecimal longitude;
	//constructor
	public GeoLocation(GeoLocation geoLocation) {
		this.latitude = geoLocation.latitude;
		this.longitude = geoLocation.longitude;
	}
}
