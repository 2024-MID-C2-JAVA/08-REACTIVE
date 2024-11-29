package co.sofka.event;

import co.sofka.generic.DomainEvent;

public class CustomerCreated extends DomainEvent {

    private String userName;

    private String pwd;

    private String rol;

    public CustomerCreated() {
        super("CreateCustomer");
    }

    public CustomerCreated(String userName, String pwd, String rol) {
        super("CreateCustomer");
        this.userName = userName;
        this.pwd = pwd;
        this.rol = rol;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
