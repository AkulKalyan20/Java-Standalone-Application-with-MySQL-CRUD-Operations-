package com.example.dao;

import com.example.model.Student;
import com.example.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    
    // SQL queries
    private static final String INSERT_STUDENT = "INSERT INTO students (name, email, course) VALUES (?, ?, ?)";
    private static final String SELECT_STUDENT_BY_ID = "SELECT * FROM students WHERE id = ?";
    private static final String SELECT_ALL_STUDENTS = "SELECT * FROM students";
    private static final String UPDATE_STUDENT = "UPDATE students SET name = ?, email = ?, course = ? WHERE id = ?";
    private static final String DELETE_STUDENT = "DELETE FROM students WHERE id = ?";
    
    // Create a new student
    public void addStudent(Student student) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_STUDENT, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.setString(3, student.getCourse());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating student failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    student.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating student failed, no ID obtained.");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error adding student: " + e.getMessage());
        }
    }
    
    // Retrieve a student by ID
    public Student getStudent(int id) {
        Student student = null;
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_STUDENT_BY_ID)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                student = new Student();
                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setEmail(rs.getString("email"));
                student.setCourse(rs.getString("course"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting student: " + e.getMessage());
        }
        
        return student;
    }
    
    // Retrieve all students
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_STUDENTS)) {
            
            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setEmail(rs.getString("email"));
                student.setCourse(rs.getString("course"));
                students.add(student);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all students: " + e.getMessage());
        }
        
        return students;
    }
    
    // Update a student
    public boolean updateStudent(Student student) {
        boolean rowUpdated = false;
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_STUDENT)) {
            
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.setString(3, student.getCourse());
            stmt.setInt(4, student.getId());
            
            rowUpdated = stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
        }
        
        return rowUpdated;
    }
    
    // Delete a student
    public boolean deleteStudent(int id) {
        boolean rowDeleted = false;
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_STUDENT)) {
            
            stmt.setInt(1, id);
            rowDeleted = stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
        }
        
        return rowDeleted;
    }
    
    // Create students table if not exists
    public static void createTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS students (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) UNIQUE NOT NULL, " +
                "course VARCHAR(100) NOT NULL" +
                ")";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(createTableSQL);
            System.out.println("Students table created or already exists.");
            
        } catch (SQLException e) {
            System.err.println("Error creating students table: " + e.getMessage());
        }
    }
}
