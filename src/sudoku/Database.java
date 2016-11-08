package sudoku;

import java.sql.*;
import java.util.*;

public class Database {

    // variable from connection class
    public Connection conn = null;

    public Connection DBconnect() {
        try {
            // next 2 lines are used to connect the DB if connected return the connection else return NULL
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:SudokuDB.sqlite");
            // System.out.println("Connected");
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            //  System.out.printf("Error");
            return null;
        }
    }

    /*  Select Function will be used to select from 2 tables (1) allSudoku (2) Load  
     it will return a random soduko from the mode the user Specified and it will return null
     if there wasn't and saved sudoku in Load table
     */
    public ArrayList<String> Select(String Difficulty, int choice) throws SQLException {
        // variable from statement class used to writed query in to be excuted
        Random rnd = new Random(); // variable from Random Class
        int randomNum = 0;     //random.nextInt(max - min) + min
        Statement stmt = conn.createStatement(); // variable from statement class used to write query in to be excuted
        String query = null;
        if (choice == 0) // choice 0 means to select from allSudoku table and return a specified sudoko by difficulty 
        {
            randomNum = rnd.nextInt(5 - 1) + 1; // Generate Random Number from 1 to 5 to select the sudoku because easch mode has 5 sudoku
            query = "Select Sudoku from 'allSudoku' WHERE Diff = " + "\"" + Difficulty + "\""; // get all sudoko according to the Difficulty from allSudoku Table
        } else if (choice == 1) { // choice 1 means to load the saved sudoko from Load table
            query = "Select * from 'Load'";  // get the saved sudoku from Load table
        }
        // variable from result set class to take the result of the query 
        ResultSet result = stmt.executeQuery(query); //Query Excution
        ArrayList<String> arr = new ArrayList();    // array used to get the results
        ArrayList<String> arr2 = new ArrayList();  // array 2 to hold the random sudoku from arr number 1

        while (result.next()) {
            if (choice == 0) {
                arr.add(result.getString("Sudoku")); //Fill the first array with all Sudukos from allSudoku Table
            } else {
                arr.add(result.getInt("ID") + "," + result.getString("Sudoku") + "," + result.getInt("Timer")
                        + "," + result.getString("Level") + "," + result.getString("savingTime")); // get all Sudokus in Load Table
            }
        }
        if (arr.isEmpty()) {
            return null; //return null if the array is empty
        } else if (choice == 0) {
            arr2.add(arr.get(randomNum)); // add random sudoku from array1 to array2 
            return arr2; //return array2
        } else {
            return arr; //return the array which contains all the loaded sudoku
        }

    }

    public void insert(String SU, int Timer, String level) throws SQLException {
        String query = null;
        Statement stmt = conn.createStatement(); // variable from statement class used to write query in to be excuted
        query = "INSERT INTO Load (Sudoku , Timer , Level) Values ( " + "\"" + SU + "\"" + "," + "\"" + Timer + "\"" + "," + "\"" + level + "\"" + ")";
        stmt.executeUpdate(query);
    }

}