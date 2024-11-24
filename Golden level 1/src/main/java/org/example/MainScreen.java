package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainScreen {
    static JFrame frame;
    private static EmailService emailService;
    private DefaultListModel<String> emailListModel;

    public MainScreen(EmailService service) {
        MainScreen.emailService = service;
        frame = new JFrame("Email Client - Inbox");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        frame.add(panel);

        emailListModel = new DefaultListModel<>();
        JList<String> emailList = new JList<>(emailListModel);
        JScrollPane scrollPane = new JScrollPane(emailList);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh");
        panel.add(refreshButton, BorderLayout.NORTH);

        JButton composeButton = new JButton("Compose");
        panel.add(composeButton, BorderLayout.SOUTH);

        // Fetch and display emails
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadEmails();
            }
        });

        composeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ComposeEmail(emailService);
            }
        });

        loadEmails();
        frame.setVisible(true);
    }

    void loadEmails() {
        emailListModel.clear();
        for (String email : emailService.fetchEmails()) {
            emailListModel.addElement(email);
        }
        frame.pack();
        frame.repaint();

       
    }

    public static void start() {
        SwingUtilities.invokeLater(() -> {
            new MainScreen(emailService);
        });
    }
}

