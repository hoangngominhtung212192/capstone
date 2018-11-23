package tks.com.gwaandroid.model;

public class TradeDetailsResponse {
    private Tradepost tradepost;
    private String[] images;

    public TradeDetailsResponse() {
    }

    public TradeDetailsResponse(Tradepost tradepost, String[] images) {
        this.tradepost = tradepost;
        this.images = images;
    }

    public Tradepost getTradepost() {
        return tradepost;
    }

    public void setTradepost(Tradepost tradepost) {
        this.tradepost = tradepost;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }
}
