package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ComposeEmail {
    private JFrame frame;
    private JTextField toField, subjectField;
    private JTextArea bodyField;
    private File attachment;

    public ComposeEmail(EmailService emailService) {
        frame = new JFrame("Compose Email");
        frame.setSize(500, 400);
        JPanel panel = new JPanel();
        frame.add(panel);
        panel.setLayout(null);

        JLabel toLabel = new JLabel("To:");
        toLabel.setBounds(10, 20, 80, 25);
        panel.add(toLabel);

        toField = new JTextField(20);
        toField.setBounds(100, 20, 300, 25);
        panel.add(toField);

        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setBounds(10, 60, 80, 25);
        panel.add(subjectLabel);

        subjectField = new JTextField(20);
        subjectField.setBounds(100, 60, 300, 25);
        panel.add(subjectField);

        JLabel bodyLabel = new JLabel("Body:");
        bodyLabel.setBounds(10, 100, 80, 25);
        panel.add(bodyLabel);

        bodyField = new JTextArea();
        bodyField.setBounds(100, 100, 300, 150);
        panel.add(bodyField);

        JButton attachButton = new JButton("Attach File");
        attachButton.setBounds(100, 270, 150, 25);
        panel.add(attachButton);

        JButton sendButton = new JButton("Send");
        sendButton.setBounds(270, 270, 80, 25);
        panel.add(sendButton);

        attachButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnVal = fileChooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    attachment = fileChooser.getSelectedFile();
                }
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String to = toField.getText();
                String subject = subjectField.getText();
                String body = bodyField.getText();
                emailService.sendEmail(to, subject, body, attachment);
                frame.dispose();
            }
        });

        frame.setVisible(true);
    }
}

