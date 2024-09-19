package userInterface;

import database.DbPostgre;
import domain.Customer;
import test.InitCall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangePasswordGui extends JDialog implements InitCall {

    private JTextField currentPasswordField;
    private JTextField newPasswordField;
    private JButton submitButton;
    private Customer loggedCustomer;

    ChangePasswordGui(Customer customer){
        this.loggedCustomer=customer;
    }


    @Override
    public void initWindow() {
        JPanel panel = initPanel();
        add(panel);
        setTitle("Şifre Değiştir");
        setSize(400, 300); // Uygun bir boyut seçebilirsiniz
        setLocationRelativeTo(null); // Ekranın ortasında açılır
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Çıkışta pencereyi kapatır
        setVisible(true);
    }

    @Override
    public JPanel initPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10)); // Daha düzenli bir görünüm için GridLayout kullanımı

        JLabel currentPasswordLabel = new JLabel("Mevcut Şifre:");
        JLabel newPasswordLabel = new JLabel("Yeni Şifre:");

        currentPasswordField = new JPasswordField();
        newPasswordField = new JPasswordField();

        submitButton = new JButton("Onayla");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
                dispose();
            }
        });

        panel.add(currentPasswordLabel);
        panel.add(currentPasswordField);
        panel.add(newPasswordLabel);
        panel.add(newPasswordField);
        panel.add(new JLabel()); // Boş bir hücre
        panel.add(submitButton);

        return panel;
    }
        DbPostgre dbPostgre = new DbPostgre();
    private void handleSubmit() {

        Customer selectedCustomer = dbPostgre.findCustomer(loggedCustomer.getId());


        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();


        if (currentPassword.equals(selectedCustomer.getPassword())) {

            selectedCustomer.setPassword(newPassword);
            dbPostgre.updateCustomer(selectedCustomer);
            JOptionPane.showMessageDialog(this, "Eski Şifre: " + currentPassword + "\nYeni Şifre: " + newPassword);

        }else {
            JOptionPane.showMessageDialog(this,"yanlış şifre");
        }


    }
}
