package manager;

import java.util.Random;

public class BankAccountFactory {



    public BankAccountFactory() {
    }


    // Rastgele IBAN oluşturma metodu
    public String generateRandomIban() {
        Random random = new Random();
        StringBuilder iban = new StringBuilder("TR");
        for (int i = 0; i < 24; i++) {
            iban.append(random.nextInt(10));
        }
        return iban.toString();
    }

    // Rastgele hesap numarası oluşturma metodu
    public String generateRandomAccountNumber() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            accountNumber.append(random.nextInt(10));
        }
        return accountNumber.toString();
    }

    // Rastgele bakiye oluşturma metodu
    public double generateRandomBalance() {
        Random random = new Random();
        double balance = 1000 + random.nextInt(5000); // 1000 ile 5000 arasında
        return balance;
    }



}
