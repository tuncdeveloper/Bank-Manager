package repository;

import model.BankAccount;

import java.util.List;

public interface IBankAccountDb {

    List<BankAccount> getBankAccountsForCustomer(int id);
    void deleteBankAccountsForCustomer(int id);
    void deleteBankAccount(int id);
    BankAccount findBankAccount(String iban);
    void addBankAccount(BankAccount bankAccount);
    void upgradeBankAccount(BankAccount bankAccount);

}
