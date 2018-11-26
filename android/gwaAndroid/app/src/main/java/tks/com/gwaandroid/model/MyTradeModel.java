package tks.com.gwaandroid.model;

public class MyTradeModel {
    public Tradepost myTradePost;
    public int numOfSucceedRequest;
    public int numOfPendingRequest ;
    public int numOfOnPaymentRequest;
    public String thumbnail ;

    public MyTradeModel() {
    }

    public MyTradeModel(Tradepost myTradePost, int numOfSucceedRequest, int numOfPendingRequest, int numOfOnPaymentRequest, String thumbnail) {
        this.myTradePost = myTradePost;
        this.numOfSucceedRequest = numOfSucceedRequest;
        this.numOfPendingRequest = numOfPendingRequest;
        this.numOfOnPaymentRequest = numOfOnPaymentRequest;
        this.thumbnail = thumbnail;
    }

    public Tradepost getMyTradePost() {
        return myTradePost;
    }

    public void setMyTradePost(Tradepost myTradePost) {
        this.myTradePost = myTradePost;
    }

    public int getNumOfSucceedRequest() {
        return numOfSucceedRequest;
    }

    public void setNumOfSucceedRequest(int numOfSucceedRequest) {
        this.numOfSucceedRequest = numOfSucceedRequest;
    }

    public int getNumOfPendingRequest() {
        return numOfPendingRequest;
    }

    public void setNumOfPendingRequest(int numOfPendingRequest) {
        this.numOfPendingRequest = numOfPendingRequest;
    }

    public int getNumOfOnPaymentRequest() {
        return numOfOnPaymentRequest;
    }

    public void setNumOfOnPaymentRequest(int numOfOnPaymentRequest) {
        this.numOfOnPaymentRequest = numOfOnPaymentRequest;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
