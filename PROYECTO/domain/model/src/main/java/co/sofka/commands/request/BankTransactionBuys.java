package co.sofka.commands.request;


import co.sofka.generic.Command;

import java.math.BigDecimal;

public class BankTransactionBuys extends Command {

    private String customerId;

    private String custumerTransactionSenderId;

    private String custumerTransactionResiverId;

    private String numberAccountTransactionSenderId;

    private String numberAccountTransactionResiverId;

    private BigDecimal amountTransaction;

    private int typeBuys;

    private  String llaveSimetrica;

    private  String vectorInicializacion;

    public BankTransactionBuys() {
    }

    public String getLlaveSimetrica() {
        return llaveSimetrica;
    }

    public void setLlaveSimetrica(String llaveSimetrica) {
        this.llaveSimetrica = llaveSimetrica;
    }

    public String getVectorInicializacion() {
        return vectorInicializacion;
    }

    public void setVectorInicializacion(String vectorInicializacion) {
        this.vectorInicializacion = vectorInicializacion;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustumerTransactionSenderId() {
        return custumerTransactionSenderId;
    }

    public void setCustumerTransactionSenderId(String custumerTransactionSenderId) {
        this.custumerTransactionSenderId = custumerTransactionSenderId;
    }

    public String getCustumerTransactionResiverId() {
        return custumerTransactionResiverId;
    }

    public void setCustumerTransactionResiverId(String custumerTransactionResiverId) {
        this.custumerTransactionResiverId = custumerTransactionResiverId;
    }

    public String getNumberAccountTransactionSenderId() {
        return numberAccountTransactionSenderId;
    }

    public void setNumberAccountTransactionSenderId(String numberAccountTransactionSenderId) {
        this.numberAccountTransactionSenderId = numberAccountTransactionSenderId;
    }

    public String getNumberAccountTransactionResiverId() {
        return numberAccountTransactionResiverId;
    }

    public void setNumberAccountTransactionResiverId(String numberAccountTransactionResiverId) {
        this.numberAccountTransactionResiverId = numberAccountTransactionResiverId;
    }

    public BigDecimal getAmountTransaction() {
        return amountTransaction;
    }

    public void setAmountTransaction(BigDecimal amountTransaction) {
        this.amountTransaction = amountTransaction;
    }

    public int getTypeBuys() {
        return typeBuys;
    }

    public void setTypeBuys(int typeBuys) {
        this.typeBuys = typeBuys;
    }
}
