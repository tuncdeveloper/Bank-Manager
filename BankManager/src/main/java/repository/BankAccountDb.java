package repository;

import model.BankAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankAccountDb implements IBankAccountDb{

    public List<BankAccount> getBankAccountsForCustomer(int customerId) {
        List<BankAccount> bankAccountList = new ArrayList<>();
        String query = "SELECT * FROM bank_accounts WHERE customer_id_fk = ?";

        try (Connection connection = Database.getInstance();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, customerId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                BankAccount bankAccount = new BankAccount();
                bankAccount.setBankAccountId(rs.getInt("bank_id"));
                bankAccount.setIBAN(rs.getString("iban"));
                bankAccount.setAccountNumber(rs.getString("account_number"));
                bankAccount.setBankBalance(rs.getDouble("bank_balance"));
                bankAccount.setCustomerIdFk(rs.getInt("customer_id_fk"));
                bankAccountList.add(bankAccount);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bankAccountList;
    }

    public void deleteBankAccountsForCustomer(int customerId) {
        String query = "DELETE FROM bank_accounts WHERE customer_id_fk = ?";

        try (Connection connection = Database.getInstance();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, customerId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteBankAccount(int bankId) {
        String query = "DELETE FROM bank_accounts WHERE  bank_id = ?";

        try (Connection connection = Database.getInstance();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, bankId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public BankAccount findBankAccount(String iban) {
        String query = "SELECT * FROM bank_accounts WHERE iban = ?";
        BankAccount bankAccount = null;

        try (Connection connection = Database.getInstance();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, iban);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                bankAccount = new BankAccount();
                bankAccount.setBankAccountId(rs.getInt("bank_id"));
                bankAccount.setIBAN(rs.getString("iban"));
                bankAccount.setAccountNumber(rs.getString("account_number"));
                bankAccount.setBankBalance(rs.getDouble("bank_balance"));
                bankAccount.setCustomerIdFk(rs.getInt("customer_id_fk"));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bankAccount;
    }
    public void addBankAccount(BankAccount bankAccount) {
        String query = "INSERT INTO bank_accounts (iban, account_number, bank_balance, customer_id_fk) VALUES (?, ?, ?, ?)";

        try (Connection connection = Database.getInstance();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, bankAccount.getIBAN());
            preparedStatement.setString(2, bankAccount.getAccountNumber());
            preparedStatement.setDouble(3, bankAccount.getBankBalance());
            preparedStatement.setInt(4, bankAccount.getCustomerIdFk());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void upgradeBankAccount(BankAccount bankAccount) {
        String query = "UPDATE bank_accounts SET iban = ?, account_number = ?, bank_balance = ? WHERE bank_id = ?";

        try (Connection connection = Database.getInstance();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, bankAccount.getIBAN());
            preparedStatement.setString(2, bankAccount.getAccountNumber());
            preparedStatement.setDouble(3, bankAccount.getBankBalance());
            preparedStatement.setInt(4, bankAccount.getBankAccountId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

