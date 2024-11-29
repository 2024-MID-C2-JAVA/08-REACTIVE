package co.sofka;

import co.sofka.event.AccountAdd;
import co.sofka.event.CustomerCreated;
import co.sofka.event.TransactionAdd;
import co.sofka.generic.EventChange;
import co.sofka.values.account.AccountId;
import co.sofka.values.comun.*;
import co.sofka.values.account.Number;
import co.sofka.values.customer.Password;
import co.sofka.values.customer.Rol;
import co.sofka.values.customer.UserName;
import co.sofka.values.transaction.TransactionId;
import co.sofka.values.transaction.TransactionRole;
import co.sofka.values.transaction.TypeTransaction;

import java.util.ArrayList;

public class CustomerChange extends EventChange {


    public CustomerChange(Customer customer) {
        apply((CustomerCreated event) -> {
            customer.username = new UserName(event.getUserName());
            customer.pwd = new Password(event.getPwd());
            customer.rol = new Rol(event.getRol());
            customer.accounts = new ArrayList<>();
        });

        apply((AccountAdd event) -> {
            Account account = new Account(
                    AccountId.of(event.getAccountId()),
                    new Number(event.getNumber()),
                    new Amount(event.getAmount()),
                    new CreatedAt(event.getCreatedAt()),
                    new Delete(event.getDeleted()),
                    new ArrayList<>());
            customer.accounts.add(account);
        });


        apply((TransactionAdd event) -> {
            TransactionAccountDetail transaction = new TransactionAccountDetail(
                    TransactionId.of(event.getTransactionId()),
                    new CustumerTransaction(event.getCustumerTransactionSenderID()),
                    new CustumerTransaction(event.getCustumerTransactionResiverId()),
                    new AccountTransaction(event.getNumberAccountTransactionSenderID()),
                    new AccountTransaction(event.getNumberAccountTransactionResiverId()),
                    new Amount(event.getAmountTransaction()),
                    new Amount(event.getTransactionCost()),
                    new TypeTransaction(event.getTypeTransaction()),
                    new CreatedAt(event.getTimeStamp()),
                    new TransactionRole(event.getTransactionRole()));
            customer.accounts.stream().map(item->{
                if(item.identity().equals(AccountId.of(event.getCustumerTransactionSenderID()))
                || item.identity().equals(AccountId.of(event.getCustumerTransactionResiverId()))){
                    item.getTransactionAccountDetails().add(transaction);
                }
                return item;
            });
        });

    }
}
