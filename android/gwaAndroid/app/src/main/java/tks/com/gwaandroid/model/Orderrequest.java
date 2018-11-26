package tks.com.gwaandroid.model;

public class Orderrequest {
    private int id;
    private String billingAddress;
    private String cancelReason;
    private String orderDate;
    private int quantity;
    private String rejectReason;
    private String stateSetDate;
    private String status;
    private Account account;
    private Tradepost tradepost;
    private boolean rated;

    public Orderrequest() {
    }

    public Orderrequest(int id, String billingAddress, String cancelReason, String orderDate, int quantity, String rejectReason, String stateSetDate, String status, Account account, Tradepost tradepost, boolean rated) {
        this.id = id;
        this.billingAddress = billingAddress;
        this.cancelReason = cancelReason;
        this.orderDate = orderDate;
        this.quantity = quantity;
        this.rejectReason = rejectReason;
        this.stateSetDate = stateSetDate;
        this.status = status;
        this.account = account;
        this.tradepost = tradepost;
        this.rated = rated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getStateSetDate() {
        return stateSetDate;
    }

    public void setStateSetDate(String stateSetDate) {
        this.stateSetDate = stateSetDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Tradepost getTradepost() {
        return tradepost;
    }

    public void setTradepost(Tradepost tradepost) {
        this.tradepost = tradepost;
    }

    public boolean isRated() {
        return rated;
    }

    public void setRated(boolean rated) {
        this.rated = rated;
    }
}
