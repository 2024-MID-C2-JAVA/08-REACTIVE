package co.sofka.commands.request;


import co.sofka.generic.Command;

public class CustomerCommand extends Command {

    private String customerId;

    private String userName;

    private String pwd;

    private String rol;

    public CustomerCommand() {
    }

    public CustomerCommand(String customerId, String username, String pwd, String rol) {
        this.customerId = customerId;
        this.userName = username;
        this.pwd = pwd;
        this.rol = rol;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
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
