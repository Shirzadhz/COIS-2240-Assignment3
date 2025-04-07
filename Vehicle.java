public abstract class Vehicle {
    private String licensePlate;
    private String make;
    private String model;
    private int year;
    private VehicleStatus status;

    public enum VehicleStatus { AVAILABLE, RESERVED, RENTED, MAINTENANCE, OUTOFSERVICE }

    public Vehicle(String make, String model, int year) {
    	if (make == null || make.isEmpty())
    		this.make = null;
    	else
    		this.make = capitalize(make);
    	
    	if (model == null || model.isEmpty())
    		this.model = null;
    	else
    		this.model = capitalize(model);
    	
        this.year = year;
        this.status = VehicleStatus.AVAILABLE;
        this.licensePlate = null;
    }

    public Vehicle() {
        this(null, null, 0);
    }

    public void setLicensePlate(String plate) {
        if (isValidPlate(plate)) {
        	this.licensePlate = plate;
        }
        else {
        	throw new IllegalArgumentException("Invalid license plate format. The plate must be 3 letters followed by 3 digits.");
        }
    }

    public void setStatus(VehicleStatus status) {
    	this.status = status;
    }

    public String getLicensePlate() { return licensePlate; }

    public String getMake() { return make; }

    public String getModel() { return model;}

    public int getYear() { return year; }

    public VehicleStatus getStatus() { return status; }

    public String getInfo() {
        return "| " + licensePlate + " | " + make + " | " + model + " | " + year + " | " + status + " ";
    }
    
    private String capitalize(String input) {
    	return (input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase());
    }
    
    private boolean isValidPlate(String plate) {
    	if ((plate!=null)&&(plate.isEmpty()==false)&&(plate.length()==6)) {
    		char[] letters = plate.substring(0,3).toCharArray();
    		char[] digits = plate.substring(3,6).toCharArray();
    		for (char c : letters) {
    			if (Character.isLetter(c)==false) {
    				return false;
    			}
    		}
    		for (char c : digits) {
    			if (Character.isDigit(c)==false) {
    				return false;
    			}
    		}
    		return true;
    	}
    	return false;
    }

}
