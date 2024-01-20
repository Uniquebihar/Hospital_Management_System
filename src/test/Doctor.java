package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Doctor {
    private Connection con;


    public Doctor(Connection con) {
        this.con = con;

    }


    public void viewDoctors() {
        String query = "selsct * from doctors";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            System.out.println("Doctors:");
            System.out.println("+------------+----------------+--------------------+");
            System.out.println("| Doctors Id | Name           | Specialization     |");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String specialization = rs.getString("specialization");
                System.out.printf("|%-12s|%-16s|%-9s|%-20s|\n", id, name, specialization);
                System.out.println("+------------+----------------+--------------------+");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getDoctorById(int id) {
        String query = "select * from doctors where id=?";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}