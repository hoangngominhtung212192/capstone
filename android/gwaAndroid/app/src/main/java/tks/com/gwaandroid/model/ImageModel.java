package tks.com.gwaandroid.model;

public class ImageModel {
    private String name;
    private String uri;

    public ImageModel() {
    }

    public ImageModel(String name, String uri) {
        this.name = name;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
