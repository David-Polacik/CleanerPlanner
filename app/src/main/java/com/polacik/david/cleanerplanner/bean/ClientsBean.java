package com.polacik.david.cleanerplanner.bean;

public class ClientsBean {

    private String clientId;
    private String clientName;
    private String clientAddress;
    private String clientPhone;
    private String clientEmail;
    private Double clientPayment;
    private Double clientBalance;
    private Double clientTotal;
    private Integer clientWorkRepeat;
    private String clientWorkStart;
    private String clientDescription;
    private Integer clientWeekWork;
    private Integer clientYearWork;

    public ClientsBean() {
    }

    public ClientsBean(String clientId, String clientName, String clientAddress, String clientPhone, String clientEmail,
                       Double clientPayment, Double clientBalance, Double clientTotal, Integer clientWorkRepeat, String clientWorkStart,
                       String clientDescription, Integer clientWeekWork, Integer clientYearWork) {

        this.clientId = clientId;
        this.clientName = clientName;
        this.clientAddress = clientAddress;
        this.clientPhone = clientPhone;
        this.clientEmail = clientEmail;
        this.clientPayment = clientPayment;
        this.clientBalance = clientBalance;
        this.clientTotal = clientTotal;
        this.clientWorkRepeat = clientWorkRepeat;
        this.clientWorkStart = clientWorkStart;
        this.clientDescription = clientDescription;
        this.clientWeekWork = clientWeekWork;
        this.clientYearWork = clientYearWork;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public Double getClientPayment() {
        return clientPayment;
    }

    public void setClientPayment(Double clientPayment) {
        this.clientPayment = clientPayment;
    }

    public Double getClientBalance() {
        return clientBalance;
    }

    public void setClientBalance(Double clientBalance) {
        this.clientBalance = clientBalance;
    }

    public Double getClientTotal() {
        return clientTotal;
    }

    public void setClientTotal(Double clientTotal) {
        this.clientTotal = clientTotal;
    }

    public Integer getClientWorkRepeat() {
        return clientWorkRepeat;
    }

    public void setClientWorkRepeat(Integer clientWorkRepeat) {
        this.clientWorkRepeat = clientWorkRepeat;
    }

    public String getClientWorkStart() {
        return clientWorkStart;
    }

    public void setClientWorkStart(String clientWorkStart) {
        this.clientWorkStart = clientWorkStart;
    }

    public String getClientDescription() {
        return clientDescription;
    }

    public void setClientDescription(String clientDescription) {
        this.clientDescription = clientDescription;
    }

    public Integer getClientWeekWork() {
        return clientWeekWork;
    }

    public void setClientWeekWork(Integer clientWeekWork) {
        this.clientWeekWork = clientWeekWork;
    }

    public Integer getClientYearWork() {
        return clientYearWork;
    }

    public void setClientYearWork(Integer clientYearWork) {
        this.clientYearWork = clientYearWork;
    }

    @Override
    public String toString() {
        return "ClientsBean{" +
                "clientId=" + clientId +
                ", clientName='" + clientName + '\'' +
                ", clientAddress='" + clientAddress + '\'' +
                ", clientPhone='" + clientPhone + '\'' +
                ", clientEmail='" + clientEmail + '\'' +
                ", clientPayment=" + clientPayment +
                ", clientWorkRepeat=" + clientWorkRepeat +
                ", clientWorkStart='" + clientWorkStart + '\'' +
                ", clientDescription='" + clientDescription + '\'' +
                ", clientWeekWork=" + clientWeekWork +
                ", clientYearWork=" + clientYearWork +
                '}';
    }
}
