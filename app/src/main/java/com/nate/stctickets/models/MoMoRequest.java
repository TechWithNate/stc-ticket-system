package com.nate.stctickets.models;

public class MoMoRequest {
    private String amount;
    private String currency;
    private String externalId;
    private String payerMessage;
    private String payeeNote;
    private Payer payer;

    public MoMoRequest() {
    }

    public MoMoRequest(String amount, String currency, String externalId, String payerMessage, String payeeNote, Payer payer) {
        this.amount = amount;
        this.currency = currency;
        this.externalId = externalId;
        this.payerMessage = payerMessage;
        this.payeeNote = payeeNote;
        this.payer = payer;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getPayerMessage() {
        return payerMessage;
    }

    public void setPayerMessage(String payerMessage) {
        this.payerMessage = payerMessage;
    }

    public String getPayeeNote() {
        return payeeNote;
    }

    public void setPayeeNote(String payeeNote) {
        this.payeeNote = payeeNote;
    }

    public Payer getPayer() {
        return payer;
    }

    public void setPayer(Payer payer) {
        this.payer = payer;
    }
}
