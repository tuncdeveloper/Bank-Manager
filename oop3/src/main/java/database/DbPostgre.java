package database;

import domain.BankAccount;
import domain.Customer;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DbPostgre {

    private static final String URL = "jdbc:postgresql://localhost:5432/bankManager";
    private static final String USER = "postgres";
    private static final String PASSWORD = "test";

    public Connection getConnection() {

        Connection connect = null;

        try {
            connect = DriverManager.getConnection(URL, USER, PASSWORD);
            if (connect != null) {
                System.out.println("Successfully connected to the database!");
            } else {
                System.out.println("Database connection failed!");
            }

        } catch (SQLException e) {
            System.out.println("Bağlantı sırasında bir hata oluştu: " + e.getMessage());
        }

        return connect;
    }

    public void newRegister(Customer customer){

        Connection connect = getConnection();

        try {
            Statement statement = connect.createStatement();

            statement.executeUpdate("INSERT INTO person (name , surname ,password , phone_number ) VALUES ('"
                    + customer.getName() + "','"
                    + customer.getSurname() + "','"
                    + customer.getPassword() + "','"
                    + customer.getPhoneNumber() + "')");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void newBankAccount(BankAccount bankAccount, int id_person) {
        Connection connection = getConnection();
        try {
            String query = "INSERT INTO bank_accounts (iban, account, balance, id_person) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, bankAccount.getIBAN());
            preparedStatement.setString(2, bankAccount.getAccountNumber());
            preparedStatement.setDouble(3, bankAccount.getBankBalance());
            preparedStatement.setInt(4, id_person);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BankAccount> getBankAccountsForCustomer(int customerId) {
        List<BankAccount> bankAccountList = new ArrayList<>();
        Connection connection = getConnection();

        try {
            String query = "SELECT * FROM bank_accounts WHERE id_person = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, customerId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                BankAccount bankAccount = new BankAccount();
                bankAccount.setId(rs.getInt("id_bank"));
                bankAccount.setIBAN(rs.getString("iban"));
                bankAccount.setAccountNumber(rs.getString("account"));
                bankAccount.setBankBalance(rs.getInt("balance"));
                bankAccountList.add(bankAccount);
            }

            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return bankAccountList;
    }

    public Customer getCustomerForBankAccountIBAN(String iban) {
        Customer customer = new Customer();
        Connection connection = getConnection();

        try {
            String query = "SELECT * FROM person p " +
                    "INNER JOIN bank_accounts bc " +
                    "ON p.id_person = bc.id_person " +
                    "WHERE bc.iban = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, iban);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                customer.setId(rs.getInt("id_person"));
                customer.setName(rs.getString("name"));
                customer.setSurname(rs.getString("surname"));
                customer.setPassword(rs.getString("password"));
                customer.setPhoneNumber(rs.getString("phone_number"));

            }

            rs.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return customer;
    }



    public void deleteBankAccountsForCustomer(Customer customer){

            Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM bank_accounts WHERE id_person = "+customer.getId());


            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }



    public List<Customer> customerShowList(){

        List<Customer> customerList = new ArrayList<>();

        Connection connect = getConnection();

        try {
            Statement statement = connect.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM person");

            while (rs.next()){

                Customer customer = new Customer();

                customer.setId(rs.getInt("id_person"));
                customer.setName(rs.getString("name"));
                customer.setSurname(rs.getString("surname"));
                customer.setPassword(rs.getString("password"));
                customer.setPhoneNumber(rs.getString("phone_number"));

                customerList.add(customer);
            }

            statement.close();
            connect.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return customerList;
    }


    public Customer findCustomer(int tc){

        Customer findCustomer = null;

        Connection connect = getConnection();

        try {
            Statement statement = connect.createStatement();

            ResultSet rs = statement.executeQuery("SELECT * FROM person WHERE id_person = " + tc);


                while (rs.next()) {
                    findCustomer = new Customer();
                    findCustomer.setId(rs.getInt("id_person"));
                    findCustomer.setName(rs.getString("name"));
                    findCustomer.setSurname(rs.getString("surname"));
                    findCustomer.setPassword(rs.getString("password"));
                    findCustomer.setPhoneNumber(rs.getString("phone_number"));

                }


            statement.close();
            connect.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return findCustomer;
    }


    public BankAccount findBankAccount(String iban){

        BankAccount findBankAccount = null;
        String query = "SELECT * FROM bank_accounts WHERE iban = ?";

        Connection connection = getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,iban);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {

                findBankAccount = new BankAccount();
                findBankAccount.setId(rs.getInt("id_bank"));
                findBankAccount.setIBAN(rs.getString("iban"));
                findBankAccount.setAccountNumber(rs.getString("account"));
                findBankAccount.setBankBalance(rs.getInt("balance"));
            }

            rs.close();
            preparedStatement.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return findBankAccount;
    }


    public void deleteCustomer(Customer customer){

        Connection connect = getConnection();

        try {
            Statement statement = connect.createStatement();

            statement.executeUpdate("DELETE FROM person WHERE id_person = "+customer.getId());

            statement.close();
            connect.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void deleteBankAccount(BankAccount bankAccount){

        Connection connection = getConnection();

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM bank_accounts WHERE id_bank = "+bankAccount.getId());

            statement.close();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void updateCustomer(Customer customer){

        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();

            statement.executeUpdate("UPDATE person SET name = '"+customer.getName()+"', surname ='"+customer.getSurname()+"' , phone_number = '"+customer.getPhoneNumber()+"', password = '"+customer.getPassword()+"'WHERE id_person ="+customer.getId());

            statement.close();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


    public void updateBankAccount(BankAccount bankAccount) {
        Connection connection = getConnection();

        try {
            Statement statement = connection.createStatement();

            statement.executeUpdate("UPDATE bank_accounts SET balance = " + bankAccount.getBankBalance() + " WHERE id_bank = " + bankAccount.getId());

            statement.close();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }




}
