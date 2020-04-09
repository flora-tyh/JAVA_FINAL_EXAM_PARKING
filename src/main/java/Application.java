import exception.InvalidTicketException;
import exception.ParkingLotFullException;
import parking.ParkingLots;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Application {

    public static void main(String[] args) {
        operateParking();
    }

    public static void operateParking() {
        while (true) {
            System.out.println("1. 初始化停车场数据\n2. 停车\n3. 取车\n4. 退出\n请输入你的选择(1~4)：");
            Scanner printItem = new Scanner(System.in);
            String choice = printItem.next();
            if (choice.equals("4")) {
                System.out.println("系统已退出");
                break;
            }
            handle(choice);
        }
    }

    private static void handle(String choice) {
        Scanner scanner = new Scanner(System.in);
        switch (choice) {
            case "1":
                System.out.println("请输入初始化数据\n格式为\"停车场编号1：车位数,停车场编号2：车位数\" 如 \"A:1,B:2,C:3\"：");
                String initInfo = scanner.next();
                if (init(initInfo)) {
                    System.out.println("初始化成功！");
                } else {
                    System.out.println("请输入正确的停车场信息！");
                }
                break;
            case "2":
                System.out.println("请输入车牌号\n格式为\"车牌号\" 如: \"A10000\"：");
                String carInfo = scanner.next();
                if (checkCarInfo(carInfo)) {
                    String ticket = park(carInfo);
                    String[] ticketDetails = ticket.split(",");
                    System.out.format("已将您的车牌号为%s的车辆停到%s停车场%s号车位，停车券为：%s，请您妥善保存。\n", ticketDetails[2], ticketDetails[0], ticketDetails[1], ticket);
                } else {
                    System.out.println("请检查车牌号");
                }
                break;
            case "3":
                System.out.println("请输入停车券信息\n格式为\"停车场编号1,车位编号,车牌号\" 如 \"A,1,A10000\"：");
                String ticket = scanner.next();
                if (checkTicket(ticket)) {
                    String car = fetch(ticket);
                    System.out.format("已为您取到车牌号为%s的车辆，很高兴为您服务，祝您生活愉快!\n", car);
                } else {
                    System.out.println("停车券信息有误");
                }
                break;
        }
    }

    public static boolean init(String initInfo) {
        JDBC.deleteAllTable();
        JDBC.createParkingLots();
        String[] parkingList = initInfo.split(",");
        String pattern = ".+:\\d+$";

        for (String s : parkingList) {
            if (!Pattern.matches(pattern, s)) {
                return false;
            }
            String[] parkingInfo = s.split(":");
            String parkingId = parkingInfo[0];
            int spaceNum = Integer.parseInt(parkingInfo[1]);
            JDBC.initParkingLots(parkingId, spaceNum);
            JDBC.createParking(parkingId);
            for (int i = 1; i <= spaceNum; i++) {
                JDBC.insertParking(parkingId, i);
            }
        }
        return true;
    }

    public static String park(String carNumber) {
        List<ParkingLots> parkingLotsArrayList = JDBC.getParkingLots();
        ParkingLots firstEmptyParking = null;
        for (ParkingLots parkingLots : parkingLotsArrayList) {
            if (parkingLots.getRestSpace() != 0) {
                firstEmptyParking = parkingLots;
                break;
            }
        }
        if (firstEmptyParking == null) {
            throw new ParkingLotFullException("非常抱歉，由于车位已满，暂时无法为您停车！");
        }
        String parkingId = firstEmptyParking.getId();
        JDBC.changeParkingSpace(parkingId, -1);
        JDBC.addCarParking(parkingId, carNumber);
        int parkingSpaceId = JDBC.findCar(parkingId, carNumber);
        return parkingId + "," + parkingSpaceId + "," + carNumber;
    }

    public static String fetch(String ticket) {
        String[] ticketDetails = ticket.split(",");
        String ticketParkingId = ticketDetails[0];
        int ticketSpaceId = Integer.parseInt(ticketDetails[1]);
        String carNumber = ticketDetails[2];
        if (JDBC.findCar(ticketParkingId, carNumber) == ticketSpaceId) {
            JDBC.deleteCar(ticketParkingId, ticketSpaceId);
            JDBC.changeParkingSpace(ticketParkingId, 1);
            return carNumber;
        };
        throw new InvalidTicketException("很抱歉，无法通过您提供的停车券为您找到相应的车辆，请您再次核对停车券是否有效！");
    }

    public static boolean checkCarInfo(String carInfo) {
        String pattern = "^[A-Z]\\d{5}$";
        return Pattern.matches(pattern, carInfo);
    }

    public static boolean checkTicket(String ticket) {
        String pattern = ".+,\\d+,[A-Z]\\d{5}";
        return Pattern.matches(pattern, ticket);
    }
}