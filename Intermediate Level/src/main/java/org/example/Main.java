package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame mainFrame = new JFrame("Event Management System");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setLayout(new GridLayout(3, 1));
            
            JButton eventButton = new JButton("Manage Events");
            JButton attendeeButton = new JButton("Manage Attendees");
            JButton scheduleButton = new JButton("Manage Schedules");

            eventButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new EventForm();
                }
            });

            attendeeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new AttendeeForm();
                }
            });

            scheduleButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new ScheduleForm();
                }
            });

            mainFrame.add(eventButton);
            mainFrame.add(attendeeButton);
            mainFrame.add(scheduleButton);

            mainFrame.setSize(700, 500);
            mainFrame.setLocationRelativeTo(null); // Center the window
            mainFrame.setVisible(true);
            mainFrame.setTitle("Event Management System");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }
}
