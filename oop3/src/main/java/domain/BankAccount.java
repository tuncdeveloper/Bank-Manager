package domain;

public class BankAccount {

    private int id ;
    private String accountNumber;
    private String iban;
    private double bankBalance;

    public BankAccount(){

    }

    public BankAccount(int id, String accountNumber, String iban, Integer bankBalance) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.iban = iban;
        this.bankBalance = bankBalance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", IBAN='" + iban + '\'' +
                ", bankBalance=" + bankBalance +
                '}';
    }
}
