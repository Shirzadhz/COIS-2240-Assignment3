import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

public class RentalSystem {
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();
    private static RentalSystem rentalSystem = new RentalSystem();
    
    public RentalSystem() {
    	loadData();
    }

    public boolean addVehicle(Vehicle vehicle) {
    	if (findVehicleByPlate(vehicle.getLicensePlate())!=null) {
    		System.out.println("Error - Vehicle is already added.");
    		return false;
    	}
    	else {
    		vehicles.add(vehicle);
            saveVehicle(vehicle);
            return true;
    	}
    }

    public boolean addCustomer(Customer customer) {
    	if (findCustomerById(customer.getCustomerId())!=null) {
    		System.out.println("Error - Customer already exists.");
    		return false;
    	}
    	else {
    		customers.add(customer);
            saveCustomer(customer);
            return true;
    	}
    }

    public boolean rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
            vehicle.setStatus(Vehicle.VehicleStatus.RENTED);
            RentalRecord rentalRecord = new RentalRecord(vehicle, customer, date, amount, "RENT");
            rentalHistory.addRecord(rentalRecord);
            System.out.println("Vehicle rented to " + customer.getCustomerName());
            saveRecord(rentalRecord);
            return true;
        }
        else {
            System.out.println("Vehicle is not available for renting.");
            return false;
        }
    }

    public boolean returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.RENTED) {
            vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
            RentalRecord rentalRecord = new RentalRecord(vehicle, customer, date, extraFees, "RETURN");
            rentalHistory.addRecord(rentalRecord);
            System.out.println("Vehicle returned by " + customer.getCustomerName());
            saveRecord(rentalRecord);
            return true;
        }
        else {
            System.out.println("Vehicle is not rented.");
            return false;
        }
    }    

    public void displayAvailableVehicles() {
    	System.out.println("|     Type         |\tPlate\t|\tMake\t|\tModel\t|\tYear\t|");
    	System.out.println("---------------------------------------------------------------------------------");
    	 
        for (Vehicle v : vehicles) {
            if (v.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
                System.out.println("|     " + (v instanceof Car ? "Car          " : "Motorcycle   ") + "|\t" + v.getLicensePlate() + "\t|\t" + v.getMake() + "\t|\t" + v.getModel() + "\t|\t" + v.getYear() + "\t|\t");
            }
        }
        System.out.println();
    }
    
    public void displayAllVehicles() {
        for (Vehicle v : vehicles) {
            System.out.println("  " + v.getInfo());
        }
    }

    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println("  " + c.toString());
        }
    }
    
    public void displayRentalHistory() {
        for (RentalRecord record : rentalHistory.getRentalHistory()) {
            System.out.println(record.toString());
        }
    }
    
    public Vehicle findVehicleByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        return null;
    }
    
    public Customer findCustomerById(int id) {
        for (Customer c : customers)
            if (c.getCustomerId() == id)
                return c;
        return null;
    }

    public Customer findCustomerByName(String name) {
        for (Customer c : customers)
            if (c.getCustomerName().equalsIgnoreCase(name))
                return c;
        return null;
    }
    
    public static RentalSystem getInstance() {
    	return rentalSystem;
    }
    
    public void saveVehicle(Vehicle vehicle) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("vehicles.txt", true));) {
			writer.write(vehicle.getClass().getSimpleName() + " " + vehicle.getInfo());
	    	writer.newLine();
		} catch (IOException e) {
			System.out.println("Error saving vehicle: " + e.getMessage());
		}
    }
    
    public void saveCustomer(Customer customer) {
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt", true));) {
			writer.write(customer.toString());
	    	writer.newLine();
		} catch (IOException e) {
			System.out.println("Error saving customer: " + e.getMessage());
		}
    }
    
    public void saveRecord(RentalRecord record) {
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter("rental_records.txt", true));) {
			writer.write(record.toString());
	    	writer.newLine();
		} catch (IOException e) {
			System.out.println("Error saving record: " + e.getMessage());
		}
    }
    
    private void loadData() {
    	File vFile = new File("vehicles.txt");
    	File cFile = new File("customers.txt");
    	File rFile = new File("rental_records.txt");
    	try {
    		if (vFile.exists()==false) {
        		vFile.createNewFile();
        	}
    	}
    	catch (IOException e) {
    		System.out.println("Error - Cannot create vehicle file.");
    	}
    	try (BufferedReader vehicleReader = new BufferedReader(new FileReader("vehicles.txt"))) {
    		String line;
    		while ((line = vehicleReader.readLine()) != null) {
    			String[] info = line.split("\\|");
    			String type = info[0].trim();
    			String licensePlate = info[1].trim();
    			String make = info[2].trim();
    			String model = info[3].trim();
    			int year = Integer.parseInt(info[4].trim());
    			Vehicle.VehicleStatus status = Vehicle.VehicleStatus.valueOf(info[5].trim());
    			Vehicle vehicle;
    			if (type.equals("Car")) {
    				int numOfSeats = Integer.parseInt(info[6].replace("Seats: ", "").trim());
    				vehicle = new Car(make, model, year, numOfSeats);
    			}
    			else if (type.equals("Motorcycle")) {
    				if (info[6].replace("Sidecar: ", "").trim().equals("Yes")) {
    					boolean hasSidecar = true;
    					vehicle = new Motorcycle(make, model, year, hasSidecar);
    				}
    				else {
    					boolean hasSidecar = false;
    					vehicle = new Motorcycle(make, model, year, hasSidecar);
    				}
    			}
    			else if (type.equals("Truck")) {
    				double cargoCapacity = Double.parseDouble(info[6].replace("Cargo Capacity: ", "").trim());
    				vehicle = new Truck(make, model, year, cargoCapacity);
    			}
    			else {
    				int numOfSeats = Integer.parseInt(info[6].replace("Seats: ","").trim());
    				int horsepower = Integer.parseInt(info[7].replace("Turbo", "").trim());
    				if (info[8].replace("Turbo: ", "").trim().equals("Yes")) {
    					boolean hasTurbo = true;
    					vehicle = new SportCar(make, model, year, numOfSeats, horsepower, hasTurbo);
    				}
    				else {
    					boolean hasTurbo = false;
    					vehicle = new SportCar(make, model, year, numOfSeats, horsepower, hasTurbo);
    				}
    			}
    			vehicle.setLicensePlate(licensePlate);
				vehicle.setStatus(status);
				vehicles.add(vehicle);
    		}
    	} catch (IOException e) {
    		System.out.println("Error loading vehicles: " + e.getMessage());
    	}
    	try {
    		if (cFile.exists()==false) {
        		cFile.createNewFile();
        	}
    	}
    	catch (IOException e) {
    		System.out.println("Error - Cannot create customer file.");
    	}
    	try (BufferedReader customerReader = new BufferedReader(new FileReader("customers.txt"))) {
    		String line;
    		while ((line = customerReader.readLine()) != null) {
    			String[] info = line.split("\\|");
    			int customerId = Integer.parseInt(info[0].replace("Customer ID: ", "").trim());
    			String name = info[1].replace("Name: ", "").trim();
    			Customer customer = new Customer(customerId, name);
    			customers.add(customer);
    		}
    	} catch (IOException e) {
    		System.out.println("Error loading customers: " + e.getMessage());
    	}
    	try {
    		if (rFile.exists()==false) {
        		rFile.createNewFile();
        	}
    	}
    	catch (IOException e) {
    		System.out.println("Error - Cannot create rental records file.");
    	}
    	try (BufferedReader rentalRecordReader = new BufferedReader(new FileReader("rental_records.txt"))) {
    		String line;
    		while ((line = rentalRecordReader.readLine()) != null) {
    			String[] info = line.split("\\|");
    			String recordType = info[0].trim();
    			String vehiclePlate = info[1].replace("Plate: ", "").trim();
    			String customerName = info[2].replace("Customer: ", "").trim();
    			LocalDate recordDate = LocalDate.parse(info[3].replace("Date: ", "").trim());
    			double totalAmount = Double.parseDouble(info[4].replace("Amount: $", "").trim());
    			Vehicle vehicle = findVehicleByPlate(vehiclePlate);
    			Customer customer = findCustomerByName(customerName);
    			RentalRecord rentalRecord = new RentalRecord(vehicle, customer, recordDate, totalAmount, recordType);
    			rentalHistory.addRecord(rentalRecord);
    		}
    	} catch (IOException e) {
    		System.out.println("Error loading vehicles: " + e.getMessage());
    	}
    }
}