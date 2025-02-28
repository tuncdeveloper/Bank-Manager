package repository;

import model.Customer;

import java.util.List;

public interface ICustomerDb {

    void newRegister(Customer customer);
    Customer getCustomerForBankAccountIBAN(String iban);
    Customer findCustomer(int id);
    Customer findCustomerByTc(String tc);
    Customer findCustomerByTcAndPassword(String tc , String password);
    List<Customer> customerShowList();
    void deleteCustomer(int id);
    void updateCustomer(Customer customer);




}
