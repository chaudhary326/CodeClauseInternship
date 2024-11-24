package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class EventForm extends JFrame {
    private JTextField titleField;
    private JTextArea descriptionField;
    private JSpinner dateSpinner;
    private JTextField locationField;
    private JButton saveButton;
    private JButton deleteButton;
    private JComboBox<String> eventComboBox;
    private int eventId;

    public EventForm() {
        setTitle("Manage Events");
        setLayout(new GridLayout(8, 2));

        // Input fields
        titleField = new JTextField();
        descriptionField = new JTextArea(5, 20);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionField);
        
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        
        locationField = new JTextField();

        // Event selection (populate dynamically from the database)
        eventComboBox = new JComboBox<>(loadEvents());

        saveButton = new JButton("Save");
        deleteButton = new JButton("Delete");

        // Button actions
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createEvent();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) { 
                deleteEvent();
            }
            

        });
    

    }
        

    private String[] loadEvents() {
        String[] events = {"Select Event"};
        String sql = "SELECT title FROM Events";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                events = Arrays.copyOf(events, events.length + 1);
                events[events.length - 1] = rs.getString("title");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading events.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return events;
    
        
}

    private void loadEventDetails() {
        String selectedEvent = (String) eventComboBox.getSelectedItem();
        if (selectedEvent.equals("Select Event")) {
            return;
        }

        String sql = "SELECT * FROM Events WHERE title =?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, selectedEvent);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                eventId = rs.getInt("event_id");
                titleField.setText(rs.getString("title"));
                descriptionField.setText(rs.getString("description"));
                dateSpinner.setValue(new java.util.Date(rs.getDate("date").getTime()));
                locationField.setText(rs.getString("location"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading event details.", "Error", JOptionPane.ERROR_MESSAGE );
            eventId = 0;
            clearFields();
            eventComboBox.setSelectedIndex(0);
            titleField.setText("");
            eventComboBox.setSelectedIndex(0);
            descriptionField.setText("");
            locationField.setText("");
            dateSpinner.setValue(new java.util.Date());
            eventComboBox.setSelectedIndex(0);
        }
        
    }

    private void createEvent() {
        String title = titleField.getText();
        String description = descriptionField.getText();
        java.sql.Date date = new java.sql.Date(((java.util.Date) dateSpinner.getValue()).getTime());
        String location = locationField.getText();

        if (title.isEmpty() || description.isEmpty() || location.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO Events (title, description, date, location) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setDate(3, date);
            pstmt.setString(4, location);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Event created successfully.");
            clearFields();
            eventComboBox.setModel(new DefaultComboBoxModel<>(loadEvents()));
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating event.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateEvent() {
        String title = titleField.getText();
        String description = descriptionField.getText();
        java.sql.Date date = new java.sql.Date(((java.util.Date) dateSpinner.getValue()).getTime());
        String location = locationField.getText();

        if (title.isEmpty() || description.isEmpty() || location.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "UPDATE Events SET title = ?, description = ?, date = ?, location = ? WHERE event_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setDate(3, date);
            pstmt.setString(4, location);
            pstmt.setInt(5, eventId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Event updated successfully.");
            clearFields();
            eventComboBox.setModel(new DefaultComboBoxModel<>(loadEvents()));
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating event.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteEvent() {
        if (eventId == 0) {
            JOptionPane.showMessageDialog(this, "Select an event to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "DELETE FROM Events WHERE event_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Event deleted successfully.");
            clearFields();
            eventComboBox.setModel(new DefaultComboBoxModel<>(loadEvents()));
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting event.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        titleField.setText("");
        descriptionField.setText("");
        dateSpinner.setValue(new java.util.Date());
        locationField.setText("");
        eventId = 0; // Reset the event ID
        eventComboBox.setSelectedIndex(0);
    }
}
