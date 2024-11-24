package org.example;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Main {

    public static void main(String[] args) {
        // Create login frame

        JFrame loginFrame = new JFrame("Email Client Login");
        loginFrame.setSize(500, 300);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(null);

        // Email field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 20, 100, 30);
        loginFrame.add(emailLabel);
        JTextField emailField = new JTextField();
        emailField.setBounds(120, 20, 250, 30);
        loginFrame.add(emailField);

        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 60, 100, 30);
        loginFrame.add(passwordLabel);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(120, 60, 250, 30);
        loginFrame.add(passwordField);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 100, 100, 30);
        loginFrame.add(loginButton);

        // Action listener for login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                // Create EmailService instance
                EmailService emailService = new EmailService(email, password);

                // Authenticate
                if (emailService.authenticate()) {
                    // Show the email sending interface after successful authentication
                    JFrame emailFrame = new JFrame("Send Email");
                    emailFrame.setSize(400, 300);
                    emailFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    emailFrame.setLayout(null);

                    // Recipient field
                    JLabel toLabel = new JLabel("To:");
                    toLabel.setBounds(20, 20, 100, 30);
                    emailFrame.add(toLabel);
                    JTextField toField = new JTextField();
                    toField.setBounds(120, 20, 250, 30);
                    emailFrame.add(toField);

                    // Subject field
                    JLabel subjectLabel = new JLabel("Subject:");
                    subjectLabel.setBounds(20, 60, 100, 30);
                    emailFrame.add(subjectLabel);
                    JTextField subjectField = new JTextField();
                    subjectField.setBounds(120, 60, 250, 30);
                    emailFrame.add(subjectField);

                    // Body field
                    JLabel bodyLabel = new JLabel("Body:");
                    bodyLabel.setBounds(20, 100, 100, 30);
                    emailFrame.add(bodyLabel);
                    JTextArea bodyArea = new JTextArea();
                    bodyArea.setBounds(120, 100, 250, 100);
                    emailFrame.add(bodyArea);

                     // Attach file button
                    
                     JButton attachButton = new JButton("Attach File");
                     attachButton.setBounds(150, 260, 100, 30);
                     emailFrame.add(attachButton);

                    // Send button
                    JButton sendButton = new JButton("Send");
                    sendButton.setBounds(150, 220, 100, 30);
                    emailFrame.add(sendButton);

                

                    // Action listener for attach file button
                    attachButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JFileChooser fileChooser = new JFileChooser();
                            int returnValue = fileChooser.showOpenDialog(null);
                            if (returnValue == JFileChooser.APPROVE_OPTION) {
                                File selectedFile = fileChooser.getSelectedFile();
                                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                            }
                        }
                    });

                    // Action listener for send button
                    sendButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String to = toField.getText();
                            String subject = subjectField.getText();
                            String body = bodyArea.getText();

                            // Optionally handle file attachment
                            File attachment = null; // set to a valid File if needed

                            // Send email
                            emailService.sendEmail(to, subject, body, attachment);
                        }
                    });

                    emailFrame.setVisible(true);




                    // Action listener for send button
                    sendButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String to = toField.getText();
                            String subject = subjectField.getText();
                            String body = bodyArea.getText();

                            // Optionally handle file attachment
                            File attachment = null; // set to a valid File if needed

                            // Send email
                            emailService.sendEmail(to, subject, body, attachment);
                        }
                    });

                    emailFrame.setVisible(true);
                    emailFrame.setLocationRelativeTo(null); // Center the frame
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Login failed. Check your credentials.");
                }
            }
 
        });

        loginFrame.setVisible(true);
        loginFrame.setLocationRelativeTo(null); // Center the frame

    }
    

}
