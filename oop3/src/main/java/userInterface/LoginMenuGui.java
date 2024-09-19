package userInterface;

import domain.Customer;
import test.InitCall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginMenuGui extends JFrame implements InitCall {

    private Customer loggedInCustomer;

    public LoginMenuGui(Customer customer) {
        this.loggedInCustomer = customer;  // Store the logged-in customer
    }

    @Override
    public void initWindow() {
        JPanel panel = initPanel();
        add(panel);
        setTitle("Giriş");
        setSize(600, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public JPanel initPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Margins
        gbc.anchor = GridBagConstraints.WEST; // Align labels to the left

        // Display logged-in customer details
        JLabel nameLabel = new JLabel("Ad: " + loggedInCustomer.getName());
        JLabel surnameLabel = new JLabel("Soyadı: " + loggedInCustomer.getSurname());
        JLabel phoneLabel = new JLabel("Telefon Numarası: " + loggedInCustomer.getPhoneNumber());

        gbc.gridx = 0;
        gbc.gridy = 0;
        infoPanel.add(nameLabel, gbc);

        gbc.gridy = 1;
        infoPanel.add(surnameLabel, gbc);

        gbc.gridy = 2;
        infoPanel.add(phoneLabel, gbc);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10)); // 2x2 grid for buttons with spacing

        // "Hesaplarım" button to view bank accounts
        JButton myAccountsButton = new JButton("Hesaplarım");
        myAccountsButton.setPreferredSize(new Dimension(150, 40));
        buttonPanel.add(myAccountsButton);

        myAccountsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a new window showing the user's bank accounts
                BankAccountShowGui bankAccountShowGui = new BankAccountShowGui(loggedInCustomer);
                bankAccountShowGui.initWindow();
            }
        });

        // "Para Aktar" button
        JButton transferButton = new JButton("Para Aktar");
        transferButton.setPreferredSize(new Dimension(150, 40));
        buttonPanel.add(transferButton);

        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TransferMenuGui transferMenuGui = new TransferMenuGui(loggedInCustomer);
                transferMenuGui.initWindow();
            }
        });

        // "Parola Değiştir" button
        JButton changePasswordButton = new JButton("Parola Değiştir");
        changePasswordButton.setPreferredSize(new Dimension(150, 40));
        buttonPanel.add(changePasswordButton);

        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChangePasswordGui changePasswordGui = new ChangePasswordGui(loggedInCustomer);
                changePasswordGui.initWindow();
            }
        });

        // "Çıkış" button
        JButton exitButton = new JButton("Çıkış");
        exitButton.setPreferredSize(new Dimension(150, 40));
        buttonPanel.add(exitButton);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, "Çıkmak istediğinize emin misiniz?", "Çıkış", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose(); // Bu pencereyi kapatır
                }
            }
        });

        // Adding panels to the main panel
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }
}
