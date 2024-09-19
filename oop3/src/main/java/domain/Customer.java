package domain;

import java.util.List;

public class Customer extends Person{

    private String phoneNumber;
    private int id;
    private List<BankAccount> bankAccounts;

    public Customer() {



    }

    public Customer(int id , String name, String surname, String password, String phoneNumber,List<BankAccount> bankAccountList) {
        super(name, surname, password);
        this.id=id;
        this.phoneNumber = phoneNumber;
        this.bankAccounts = bankAccountList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public String getSurname() {
        return super.getSurname();
    }

    @Override
    public void setSurname(String surname) {
        super.setSurname(surname);
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(List<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    @Override
    public String toString() {
        return "TC: " + getId() + ", Adı: " + getName() + ", Soyadı: " + getSurname() + ", Telefon: " + getPhoneNumber()+", Parola: "+getPassword();
    }



}
