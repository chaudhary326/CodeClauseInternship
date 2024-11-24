package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AttendeeForm extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JComboBox<String> eventComboBox;
    private JButton saveButton;
    private JButton deleteButton;
    private int attendeeId;

    public AttendeeForm() {
        setTitle("Manage Attendees");
        setLayout(new GridLayout(5, 2));

        // Input fields
        nameField = new JTextField();
        emailField = new JTextField();
        
        // Event selection (dummy data for demonstration)
        eventComboBox = new JComboBox<>(new String[]{"Select Event", "Event 1", "Event 2"});
        
        saveButton = new JButton("Save");
        deleteButton = new JButton("Delete");

        // Button actions
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAttendee();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAttendee();
            }
        });

        // Adding components to the frame
        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Event:"));
        add(eventComboBox);
        add(saveButton);
        add(deleteButton);

        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void saveAttendee() {
        String name = nameField.getText();
        String email = emailField.getText();
        String selectedEvent = (String) eventComboBox.getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || selectedEvent.equals("Select Event")) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO Attendees (name, email, event_id) VALUES (?, ?, (SELECT event_id FROM Events WHERE title = ?))";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, selectedEvent);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Attendee saved successfully.");
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving attendee.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAttendee() {
        if (attendeeId == 0) {
            JOptionPane.showMessageDialog(this, "Select an attendee to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "DELETE FROM Attendees WHERE attendee_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, attendeeId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Attendee deleted successfully.");
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting attendee.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        eventComboBox.setSelectedIndex(0);
        attendeeId = 0; // Reset the attendee ID
    }

    public static void main(String[] args) {
        new AttendeeForm();
    }
}
