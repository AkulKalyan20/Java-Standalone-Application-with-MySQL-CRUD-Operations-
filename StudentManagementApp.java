package com.example;

import com.example.dao.StudentDAO;
import com.example.model.Student;
import com.example.util.DatabaseUtil;

import java.util.List;
import java.util.Scanner;

public class StudentManagementApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentDAO studentDAO = new StudentDAO();
    
    public static void main(String[] args) {
        // Create table if not exists
        StudentDAO.createTableIfNotExists();
        
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\n=== Student Management System ===");
            System.out.println("1. Add New Student");
            System.out.println("2. View All Students");
            System.out.println("3. View Student by ID");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    viewAllStudents();
                    break;
                case 3:
                    viewStudentById();
                    break;
                case 4:
                    updateStudent();
                    break;
                case 5:
                    deleteStudent();
                    break;
                case 6:
                    exit = true;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        
        // Close database connection when done
        DatabaseUtil.closeConnection();
        scanner.close();
    }
    
    private static void addStudent() {
        System.out.println("\n--- Add New Student ---");
        
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter student email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter student course: ");
        String course = scanner.nextLine();
        
        Student student = new Student();
        student.setName(name);
        student.setEmail(email);
        student.setCourse(course);
        
        studentDAO.addStudent(student);
        System.out.println("Student added successfully with ID: " + student.getId());
    }
    
    private static void viewAllStudents() {
        System.out.println("\n--- All Students ---");
        List<Student> students = studentDAO.getAllStudents();
        
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        
        for (Student student : students) {
            System.out.println(student);
        }
    }
    
    private static void viewStudentById() {
        System.out.print("\nEnter student ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        Student student = studentDAO.getStudent(id);
        
        if (student != null) {
            System.out.println("\n--- Student Details ---");
            System.out.println("ID: " + student.getId());
            System.out.println("Name: " + student.getName());
            System.out.println("Email: " + student.getEmail());
            System.out.println("Course: " + student.getCourse());
        } else {
            System.out.println("Student not found with ID: " + id);
        }
    }
    
    private static void updateStudent() {
        System.out.print("\nEnter student ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        // Check if student exists
        Student existingStudent = studentDAO.getStudent(id);
        if (existingStudent == null) {
            System.out.println("Student not found with ID: " + id);
            return;
        }
        
        System.out.println("Current details: " + existingStudent);
        
        System.out.print("Enter new name (leave blank to keep current): ");
        String name = scanner.nextLine();
        
        System.out.print("Enter new email (leave blank to keep current): ");
        String email = scanner.nextLine();
        
        System.out.print("Enter new course (leave blank to keep current): ");
        String course = scanner.nextLine();
        
        // Update only the fields that were provided
        if (!name.isEmpty()) existingStudent.setName(name);
        if (!email.isEmpty()) existingStudent.setEmail(email);
        if (!course.isEmpty()) existingStudent.setCourse(course);
        
        boolean updated = studentDAO.updateStudent(existingStudent);
        
        if (updated) {
            System.out.println("Student updated successfully!");
            System.out.println("Updated details: " + existingStudent);
        } else {
            System.out.println("Failed to update student.");
        }
    }
    
    private static void deleteStudent() {
        System.out.print("\nEnter student ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        System.out.print("Are you sure you want to delete student with ID " + id + "? (yes/no): ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("yes")) {
            boolean deleted = studentDAO.deleteStudent(id);
            if (deleted) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Failed to delete student or student not found.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
}
