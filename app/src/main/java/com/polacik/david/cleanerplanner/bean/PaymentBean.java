package com.polacik.david.cleanerplanner.bean;

public class PaymentBean {

    private String paymentId;
    private String clientId;
    private Double paymentCharged;
    private Double paymentPaid;
    private String paymentMethod;
    private String paymentReason;
    private String paymentDate;
    private Integer paymentStatus;


    public PaymentBean() {
    }

    public PaymentBean(String paymentId, String clientId, Double paymentCharged, Double paymentPaid, String paymentMethod,
                       String paymentDate, Integer paymentStatus) {
        this.paymentId = paymentId;
        this.clientId = clientId;
        this.paymentCharged = paymentCharged;
        this.paymentPaid = paymentPaid;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
    }

    public PaymentBean(String paymentId, String clientId, String paymentReason, String paymentDate, Integer paymentStatus) {
        this.paymentId = paymentId;
        this.clientId = clientId;
        this.paymentReason = paymentReason;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Double getPaymentCharged() {
        return paymentCharged;
    }

    public void setPaymentCharged(Double paymentCharged) {
        this.paymentCharged = paymentCharged;
    }

    public Double getPaymentPaid() {
        return paymentPaid;
    }

    public void setPaymentPaid(Double paymentPaid) {
        this.paymentPaid = paymentPaid;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentReason() {
        return paymentReason;
    }

    public void setPaymentReason(String paymentReason) {
        this.paymentReason = paymentReason;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Integer getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "PaymentBean{" +
                "paymentId='" + paymentId + '\'' +
                ", clientId='" + clientId + '\'' +
                ", paymentCharged=" + paymentCharged +
                ", paymentPaid=" + paymentPaid +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentReason='" + paymentReason + '\'' +
                ", paymentDate='" + paymentDate + '\'' +
                ", paymentStatus=" + paymentStatus +
                '}';
    }
}
