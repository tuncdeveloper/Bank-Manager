package model;

public class BankAccount {

    private int bankAccountId ;
    private String accountNumber;
    private String iban;
    private double bankBalance;
    private Integer customerIdFk;

    public BankAccount(){

    }

    public Integer getCustomerIdFk() {
        return customerIdFk;
    }

    public void setCustomerIdFk(Integer customerIdFk) {
        this.customerIdFk = customerIdFk;
    }

    public int getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(int bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIBAN() {
        return iban;
    }

    public void setIBAN(String IBAN) {
        this.iban = IBAN;
    }

    public double getBankBalance() {
        return bankBalance;
    }

    public void setBankBalance(double bankBalance) {
        this.bankBalance = bankBalance;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "bankAccountId=" + bankAccountId +
                ", accountNumber='" + accountNumber + '\'' +
                ", iban='" + iban + '\'' +
                ", bankBalance=" + bankBalance +
                '}';
    }
}
