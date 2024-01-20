package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class HospitalManagement {
    public static final String driver="oracle.jdbc.driver.OracleDriver";
    public static final String dbURL="jdbc:oracle:thin:@localhost:1521:xe";
    public static final String uName="system";
    public static final String pWord="system";

    public static void main (String[]args) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Scanner sc= new Scanner(System.in);
        try {
            Connection con= DriverManager.getConnection("oracle.jdbc.driver.OracleDriver","system","system");
            Patient patient= new  Patient(con , sc);
            Doctor  doctor= new Doctor(con);
            while(true) {
                System.out.println("Hospital Management System");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter your choice:");
                int choice = sc.nextInt();

                switch(choice) {
                    case 1:
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        patient.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        bookAppointment(patient, doctor,con, sc);
                        System.out.println();
                        break;
                    case 5:
                        System.out.println("Thankyou for using Hospital Management System");
                        return;
                    default:
                        System.out.println("Enter valid choice !!!");
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void bookAppointment(Patient  patient, Doctor doctor,Connection con, Scanner  sc) {
        System.out.println("Enter Patient Id:");
        int patientid= sc.nextInt();
        System.out.println("Enter Doctor Id:");
        int doctorid= sc.nextInt();
        System.out.println("Enter appointment date (YYYY-MM-DD)");
        String  appointmentDate = sc.next();
        if(patient.getPatientById(patientid) && doctor.getDoctorById(doctorid)) {
            if(checkDoctorAvailability(doctorid , appointmentDate, con)) {
                String appointmentQuery = "insert into appointments(patient_id, doctor_id, appointment_date)values(?,?,?)";
                try {
                    PreparedStatement  ps=con.prepareStatement(appointmentQuery);
                    ps.setInt(1, patientid);
                    ps.setInt(2, doctorid);
                    ps.setString(3, appointmentDate);
                    int k= ps.executeUpdate();
                    if(k>0) {
                        System.out.println("Appointment Booked");
                    }
                    else {
                        System.out.println("Failed to book Appointment");
                    }
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                System.out.println("Doctor not Available on this date");
            }
        }
        else {
            System.out.println("Doctor or Patient does not exist...");
        }
    }
    public static boolean checkDoctorAvailability(int doctorid, String appointmentDate, Connection con){
        String query= " select count(*) from appointments where doctor_id=? and appointment_date =?";
        try {
            PreparedStatement ps= con.prepareStatement(query);
            ps.setInt(1, doctorid);
            ps.setString(2, appointmentDate);
            ResultSet rs =ps.executeQuery();
            if(rs.next()) {
                int count =rs.getInt(1);
                if(count==0) {
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
