package core;

public class Price {
    private String shop_name;
    private String shop_url;
    private double price;
    private double price_with_shipping;
    private String currency;
    private String url;


    public Price() {}

    public String getShop_name() {
        return shop_name;
    }

    public String getShop_url() {
        return shop_url;
    }

    public double getPrice() {
        return price;
    }

    public double getPrice_with_shipping() {
        return price_with_shipping;
    }

    public String getCurrency() {
        return currency;
    }

    public String getUrl() {
        return url;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public void setShop_url(String shop_url) {
        this.shop_url = shop_url;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPrice_with_shipping(double price_with_shipping) {
        this.price_with_shipping = price_with_shipping;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toString() {
        String s = "";
        s += "\n" + shop_name;
        s += "\n" + shop_url;
        s += "\n" + url;
        s += "\n" + price + " " + currency;
        s += "\n" + price_with_shipping + " " + currency;

        return s;
    }

}
