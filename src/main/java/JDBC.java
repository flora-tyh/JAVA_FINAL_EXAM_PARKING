import exception.InvalidTicketException;
import parking.Parking;
import parking.ParkingLots;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class JDBC {
    private static final String URL = "jdbc:mysql://localhost:3306/parking_lot?useUnicode=true&characterEncoding=utf-8&serverTimezone=Hongkong&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "tangyunhan";

    public static void deleteAllTable() {
        Connection conn = null;
        PreparedStatement ptmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "SHOW TABLES FROM parking_lot;";
            ptmt = conn.prepareStatement(sql);
            rs = ptmt.executeQuery();
            while (rs.next()) {
                deleteTable(rs.getString("Tables_in_parking_lot"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ptmt, rs);
        }
    }

    public static void deleteTable(String tableName) {
        Connection conn = null;
        PreparedStatement ptmt = null;

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "DROP TABLE `parking_lot`." + tableName + ";";
            ptmt = conn.prepareStatement(sql);
            ptmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ptmt);
        }
    }
    public static void createParkingLots() {
        Connection conn = null;
        PreparedStatement ptmt = null;

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "CREATE TABLE `parking_lots`(\n" +
                    "`id` varchar(1) NOT NULL,\n" +
                    "`rest_space` int DEFAULT NULL,\n" +
                    "`space` int DEFAULT NULL,\n" +
                    "PRIMARY KEY (`id`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
            ptmt = conn.prepareStatement(sql);
            ptmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ptmt);
        }
    }

    public static void initParkingLots(String id, int restSpace) {
        Connection conn = null;
        PreparedStatement ptmt = null;

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "INSERT INTO `parking_lot`.`parking_lots` (`id`, `rest_space`, `space`) VALUES (?, ?, ?);";
            ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, id);
            ptmt.setInt(2, restSpace);
            ptmt.setInt(3, restSpace);
            ptmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ptmt);
        }
    }

    public static void createParking(String id) {
        Connection conn = null;
        PreparedStatement ptmt = null;

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "CREATE TABLE "+ id +" (\n" +
                    "  `num` int NOT NULL AUTO_INCREMENT,\n" +
                    "  `car_number` varchar(6) DEFAULT NULL,\n" +
                    "  PRIMARY KEY (`num`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
            ptmt = conn.prepareStatement(sql);
            ptmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ptmt);
        }
    }

    public static List<ParkingLots> getParkingLots() {
        Connection conn = null;
        PreparedStatement ptmt = null;
        ResultSet rs = null;
        List<ParkingLots> parkingLotsArrayList = new ArrayList<ParkingLots>();

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "SELECT id,\n" +
                    "\trest_space,\n" +
                    "    space\n" +
                    "FROM parking_lots";
            ptmt = conn.prepareStatement(sql);
            rs = ptmt.executeQuery();
            while (rs.next()) {
                ParkingLots parkingLots = new ParkingLots(
                        rs.getString("id"), rs.getInt("space"), rs.getInt("rest_space"));
                parkingLotsArrayList.add(parkingLots);
            }
            return parkingLotsArrayList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            close(conn, ptmt, rs);
        }
    }

    public static List<Parking> getParking(String parkingId) {
        Connection conn = null;
        PreparedStatement ptmt = null;
        ResultSet rs = null;
        List<Parking> parkingArrayList = new ArrayList<Parking>();

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "SELECT num,\n" +
                    "    car_number\n" +
                    "FROM " + parkingId;
            ptmt = conn.prepareStatement(sql);
            rs = ptmt.executeQuery();
            while (rs.next()) {
                Parking parking = new Parking(
                        rs.getInt("num"), rs.getString("car_number"));
                parkingArrayList.add(parking);
            }
            return parkingArrayList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            close(conn, ptmt, rs);
        }
    }

    public static void countParkingSpace(String parkingId) {
        Connection conn = null;
        PreparedStatement ptmt = null;

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "UPDATE `parking_lot`.`parking_lots` SET `rest_space` = rest_space - 1 WHERE (`id` = ?);\n";
            ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, parkingId);
            ptmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ptmt);
        }
    }

    public static void addCarParking(String parkingId, String carNumber) {
        Connection conn = null;
        PreparedStatement ptmt = null;

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "INSERT INTO `parking_lot`."+ parkingId + " (`car_number`) VALUES (?);";
            ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, carNumber);
            ptmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ptmt);
        }
    }

    public static boolean findCar(String parkingId, int spaceId, String carNumber) {
        Connection conn = null;
        PreparedStatement ptmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "SELECT num,\n" +
                    "    car_number\n" +
                    "FROM " + parkingId +
                    " WHERE num = ? " +
                    "AND car_number = ?;";
            ptmt = conn.prepareStatement(sql);
            ptmt.setInt(1, spaceId);
            ptmt.setString(2, carNumber);
            rs = ptmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InvalidTicketException("很抱歉，无法通过您提供的停车券为您找到相应的车辆，请您再次核对停车券是否有效");
        } finally {
            close(conn, ptmt, rs);
        }
    }

    public static void deleteCar(String parkingId, int spaceId) {
        Connection conn = null;
        PreparedStatement ptmt = null;

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "DELETE FROM `parking_lot`." + parkingId + " WHERE (`num` = ?);";
            ptmt = conn.prepareStatement(sql);
            ptmt.setInt(1, spaceId);
            ptmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            close(conn, ptmt);
        }
    }

    public static void close(Connection conn, PreparedStatement ptmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        close(conn, ptmt);
    }

    public static void close(Connection conn, PreparedStatement ptmt) {
        if (ptmt != null) {
            try {
                ptmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
