import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class VehicleRentalTest {

	@Test
	public void testLicensePlateValidation() {
		Vehicle car1 = new Car(null, null, 0, 0);
		car1.setLicensePlate("AAA100");
		assertTrue(car1.getLicensePlate().equals("AAA100"), "AAA100 isn't valid, check code");
		Vehicle car2 = new Car(null, null, 0, 0);
		car2.setLicensePlate("ABC567");
		assertTrue(car2.getLicensePlate().equals("ABC567"), "ABC567 isn't valid, check code");
		Vehicle car3 = new Car(null, null, 0, 0);
		car3.setLicensePlate("ZZZ999");
		assertTrue(car3.getLicensePlate().equals("ZZZ999"), "ZZZ999 isn't valid, check code");
		
		Vehicle car4 = new Car(null, null, 0, 0);
		assertThrows(IllegalArgumentException.class, () -> {
			car4.setLicensePlate("");
		}, "empty plate doesn't throw exception, check code");
		assertThrows(IllegalArgumentException.class, () -> {
			car4.setLicensePlate(null);
		}, "null plate doesn't throw exception, check code");
		assertThrows(IllegalArgumentException.class, () -> {
			car4.setLicensePlate("AAA1000");
		}, "AAA1000 doesn't throw exception, check code");
		assertThrows(IllegalArgumentException.class, () -> {
			car4.setLicensePlate("ZZZ99");
		}, "ZZZ99 plate doesn't throw exception, check code");
	}

}
