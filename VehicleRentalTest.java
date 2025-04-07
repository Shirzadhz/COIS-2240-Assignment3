import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

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
	
	@Test
	public void testRentAndReturnVehicle() {
		Vehicle vehicle = new Car("Toyota", "Corolla", 2019, 5);
		vehicle.setLicensePlate("AAA100");
		Customer customer = new Customer(1, "Shirzad");
		assertTrue(vehicle.getStatus()==Vehicle.VehicleStatus.AVAILABLE, "Vehicle isn't available");
		RentalSystem rental = RentalSystem.getInstance();
		boolean rented = rental.rentVehicle(vehicle, customer, LocalDate.of(2025, 04, 06), 0);
		assertTrue(rented, "Vehicle was not rented");
		assertTrue(vehicle.getStatus()==Vehicle.VehicleStatus.RENTED, "Vehicle status was not set to rented");
		rented = rental.rentVehicle(vehicle, customer, LocalDate.of(2025, 04, 06), 0);
		assertFalse(rented, "Vehicle was rented when it wasn't supposed to");
		boolean returned = rental.returnVehicle(vehicle, customer, LocalDate.of(2024, 04, 06), 0);
		assertTrue(returned, "Vehicle was not returned");
		assertTrue(vehicle.getStatus()==Vehicle.VehicleStatus.AVAILABLE, "Vehicle status was not set to available after being returned");
		returned = rental.returnVehicle(vehicle, customer, LocalDate.of(2025, 04, 06), 0);
		assertFalse(returned, "Vehicle was returned when it wasn't supposed to");
		
	}
}
