package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

public class ScheduleForm extends JFrame {
    private JTextField sessionTitleField;
    private JSpinner sessionTimeSpinner;
    private JComboBox<String> eventComboBox;
    private JButton saveButton;
    private JButton deleteButton;
    private int scheduleId;

    public ScheduleForm() {
        setTitle("Manage Schedule");
        setLayout(new GridLayout(5, 2));

        // Input fields
        sessionTitleField = new JTextField();
        
        // Session time spinner
        sessionTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(sessionTimeSpinner, "HH:mm");
        sessionTimeSpinner.setEditor(timeEditor);
        
        // Event selection (populate dynamically from the database)
        eventComboBox = new JComboBox<>(loadEvents());

        saveButton = new JButton("Save");
        deleteButton = new JButton("Delete");

        // Button actions
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSchedule();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSchedule();
            }
        });

        // Adding components to the frame
        add(new JLabel("Session Title:"));
        add(sessionTitleField);
        add(new JLabel("Session Time:"));
        add(sessionTimeSpinner);
        add(new JLabel("Event:"));
        add(eventComboBox);
        add(saveButton);
        add(deleteButton);

        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private String[] loadEvents() {
        // Fetch event titles from the database
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT title FROM Events");
             ResultSet rs = pstmt.executeQuery()) {
             
            List<String> events = new java.util.ArrayList<>();
            events.add("Select Event");
            while (rs.next()) {
                events.add(rs.getString("title"));
            }
            return events.toArray(new String[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading events.", "Error", JOptionPane.ERROR_MESSAGE);
            return new String[]{"Error loading events"};
        }
    }

    private void saveSchedule() {
        String sessionTitle = sessionTitleField.getText();
        String selectedEvent = (String) eventComboBox.getSelectedItem();
        Time sessionTime = new Time(((java.util.Date) sessionTimeSpinner.getValue()).getTime());

        if (sessionTitle.isEmpty() || selectedEvent.equals("Select Event")) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO Schedules (event_id, session_title, session_time) VALUES ((SELECT event_id FROM Events WHERE title = ?), ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, selectedEvent);
            pstmt.setString(2, sessionTitle);
            pstmt.setTime(3, sessionTime);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Schedule saved successfully.");
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving schedule.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSchedule() {
        if (scheduleId == 0) {
            JOptionPane.showMessageDialog(this, "Select a schedule to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "DELETE FROM Schedules WHERE schedule_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, scheduleId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Schedule deleted successfully.");
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting schedule.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        sessionTitleField.setText("");
        sessionTimeSpinner.setValue(new java.util.Date());
        eventComboBox.setSelectedIndex(0);
        scheduleId = 0; // Reset the schedule ID
    }

}

