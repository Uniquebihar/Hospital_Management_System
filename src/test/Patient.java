package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Patient {
    private Connection con;
    private Scanner sc;

    public Patient(Connection con, Scanner sc) {
        this.con=con;
        this.sc=sc;
    }
    public void addPatient() {
        System.out.println("Enter Patient Name:");
        String name=sc.next();
        System.out.println("Enter Patient Age:");
        int age=sc.nextInt();
        System.out.println("Enter Patient Gender:");
        String gender=sc.next();

        try {
            String query ="insert into patients(name, age, gender) values(?,?,?)";
            PreparedStatement  ps=con.prepareStatement(query);
            ps.setString(1,name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            int k=ps.executeUpdate();
            if(k>0) {
                System.out.println("Patient Added Successfully");
            }
            else {
                System.out.println("Failed to Add Patient");
            }
        }
        catch(Exception  e) {
            e.printStackTrace();
        }
    }
    public void viewPatients() {
        String query="selsct * from patients";
        try {
            PreparedStatement  ps=con.prepareStatement(query);
            ResultSet  rs=ps.executeQuery();
            System.out.println("Patients:");
            System.out.println("+------------+----------------+---------+----------+");
            System.out.println("| Patient Id | Name           | Age      | Gender  |");
            while(rs.next()) {
                int id= rs.getInt("id");
                String name= rs.getString("name");
                int age= rs.getInt("age");
                String gender= rs.getString("gender");
                System.out.printf("|%-12s|%-16s|%-9s|%-10s|\n",id,name,age,gender);
                System.out.println("+------------+----------------+---------+----------+");
            }
        }
        catch(Exception  e) {
            e.printStackTrace();
        }
    }
    public  boolean  getPatientById(int id){
        String  query= "select * from patients where id=?";
        try {
            PreparedStatement ps=con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return true;
            }
            else {
                return false;
            }
        }
        catch(Exception  e){
            e.printStackTrace();
        }
        return false;
    }
}
