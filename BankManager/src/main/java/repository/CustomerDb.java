package repository;


import model.BankAccount;
import model.Customer;
import model.CustomerTypeEnum;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDb implements ICustomerDb{

    public void newRegister(Customer customer) {
        try (Connection connect = Database.getInstance();
             PreparedStatement preparedStatement = connect.prepareStatement(
                     "INSERT INTO customers (name, surname,tc,password, phone_number, customer_type) VALUES (?, ?, ?,?, ?,?)")
        ) {
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getSurname());
            preparedStatement.setString(3, customer.getTc());
            preparedStatement.setString(4, customer.getPassword());
            preparedStatement.setString(5, customer.getPhoneNumber());
            preparedStatement.setString(6,customer.getCustomerTypeEnum().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Customer getCustomerForBankAccountIBAN(String iban) {
        Customer customer = null;
        String query = "SELECT * FROM customers p INNER JOIN bank_accounts bc ON p.customer_id = bc.customer_id_fk WHERE bc.iban = ?";
        try (Connection connection = Database.getInstance();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, iban);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setName(rs.getString("name"));
                customer.setSurname(rs.getString("surname"));
                customer.setTc(rs.getString("tc"));
                customer.setPassword(rs.getString("password"));
                customer.setPhoneNumber(rs.getString("phone_number"));
                customer.setCustomerTypeEnum(CustomerTypeEnum.valueOf(rs.getString("customer_type")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customer;
    }

    public Customer findCustomer(int customerId) {
        Customer findCustomer = null;
        String query = "SELECT * FROM customers WHERE customer_id = ?";
        try (Connection connect = Database.getInstance();
             PreparedStatement preparedStatement = connect.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, customerId);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                findCustomer = new Customer();
                findCustomer.setCustomerId(rs.getInt("customer_id"));
                findCustomer.setName(rs.getString("name"));
                findCustomer.setSurname(rs.getString("surname"));
                findCustomer.setTc(rs.getString("tc"));
                findCustomer.setPassword(rs.getString("password"));
                findCustomer.setPhoneNumber(rs.getString("phone_number"));
                findCustomer.setCustomerTypeEnum(CustomerTypeEnum.valueOf(rs.getString("customer_type")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return findCustomer;
    }

    public Customer findCustomerByTc(String tc) {
        Customer findCustomer = null;
        String query = "SELECT * FROM customers WHERE tc = ?";
        try (Connection connect = Database.getInstance();
             PreparedStatement preparedStatement = connect.prepareStatement(query)) {
            preparedStatement.setString(1, tc); // tc'yi String olarak geçiriyoruz
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                findCustomer = new Customer();
                findCustomer.setCustomerId(rs.getInt("customer_id"));
                findCustomer.setName(rs.getString("name"));
                findCustomer.setSurname(rs.getString("surname"));
                findCustomer.setTc(rs.getString("tc"));
                findCustomer.setPassword(rs.getString("password"));
                findCustomer.setPhoneNumber(rs.getString("phone_number"));
                findCustomer.setCustomerTypeEnum(CustomerTypeEnum.valueOf(rs.getString("customer_type")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return findCustomer;
    }

    public Customer findCustomerByTcAndPassword(String tc, String password) {
        Customer findCustomer = null;
        String query = "SELECT * FROM customers WHERE tc = ? AND password = ?";

        try (Connection connect = Database.getInstance();
             PreparedStatement preparedStatement = connect.prepareStatement(query)
        ) {
            preparedStatement.setString(1, tc);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                findCustomer = new Customer();
                findCustomer.setCustomerId(rs.getInt("customer_id"));
                findCustomer.setName(rs.getString("name"));
                findCustomer.setSurname(rs.getString("surname"));
                findCustomer.setTc(rs.getString("tc"));
                findCustomer.setPassword(rs.getString("password"));
                findCustomer.setPhoneNumber(rs.getString("phone_number"));
                findCustomer.setCustomerTypeEnum(CustomerTypeEnum.valueOf(rs.getString("customer_type")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return findCustomer;
    }


    public List<Customer> customerShowList() {
        List<Customer> customerList = new ArrayList<>();
        String query = "SELECT * FROM customers";
        try (Connection connect = Database.getInstance();
             PreparedStatement preparedStatement = connect.prepareStatement(query);
             ResultSet rs = preparedStatement.executeQuery()
        ) {
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setName(rs.getString("name"));
                customer.setSurname(rs.getString("surname"));
                customer.setTc(rs.getString("tc"));
                customer.setPassword(rs.getString("password"));
                customer.setPhoneNumber(rs.getString("phone_number"));
                customer.setCustomerTypeEnum(CustomerTypeEnum.valueOf(rs.getString("customer_type")));

                customerList.add(customer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customerList;
    }

    public void deleteCustomer(int customerId) {
        String query = "DELETE FROM customers WHERE customer_id = ?";
        try (Connection connect = Database.getInstance();
             PreparedStatement preparedStatement = connect.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, customerId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateCustomer(Customer customer) {
        String query = "UPDATE customers SET name = ?, surname = ?, password = ?, phone_number = ?  WHERE customer_id = ?";
        try (Connection connection = Database.getInstance();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getSurname());
            preparedStatement.setString(3, customer.getPassword());
            preparedStatement.setString(4, customer.getPhoneNumber());

            preparedStatement.setInt(5, customer.getCustomerId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

