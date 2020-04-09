package parking;

import java.util.Objects;

public class ParkingLots {
    private String id;
    private int space;
    private int restSpace;

    public ParkingLots(String id, int space, int restSpace) {
        this.id = id;
        this.space = space;
        this.restSpace = restSpace;
    }

    public ParkingLots() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public int getRestSpace() {
        return restSpace;
    }

    public void setRestSpace(int restSpace) {
        this.restSpace = restSpace;
    }
}
