package ie.cj.data;

import java.sql.*;

public class Xx {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://172.17.0.2/test", "postgres",
                "Good4You")) {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("select * from person");
            while(rs.next()) {
                System.out.println(rs.getString("firstname"));
            }
        }
    }
}
