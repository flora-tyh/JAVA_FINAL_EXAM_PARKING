package parking;

public class Parking {
    private int num;
    private String licenseNumber;

    public Parking() {
    }

    public Parking(int num, String licenseNumber) {
        this.num = num;
        this.licenseNumber = licenseNumber;
    }

    public int getNum() {
        return num;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }
}
