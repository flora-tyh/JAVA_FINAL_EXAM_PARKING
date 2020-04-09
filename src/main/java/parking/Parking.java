package parking;

public class Parking {
    private int num;
    private String carNumber;

    public Parking() {
    }

    public Parking(int num, String carNumber) {
        this.num = num;
        this.carNumber = carNumber;
    }

    public int getNum() {
        return num;
    }

    public String getCarNumber() {
        return carNumber;
    }
}
