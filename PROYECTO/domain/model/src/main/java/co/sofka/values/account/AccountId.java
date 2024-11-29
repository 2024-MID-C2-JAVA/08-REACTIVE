package co.sofka.values.account;

import co.sofka.generic.Identity;

public class AccountId extends Identity {

    public AccountId() {
    }

    private AccountId(String uuid) {
        super(uuid);
    }

    public static AccountId of(String uuid){
        return new AccountId(uuid);
    }

}
