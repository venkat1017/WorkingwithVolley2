package reader.android.com.newsx;

/**
 * Created by venkatesh on 24-09-2016.
 */
//Model Class for top_news and top_headlines
public class Topnews {
    private String topnews_url = "";
    private String topnews_headline="";

    public Topnews() {
    }

    public Topnews(String topnews_url, String topnews_headline) {
        this.topnews_url = topnews_url;
        this.topnews_headline = topnews_headline;
    }

    public String getTopnews_headline() {
        return topnews_headline;
    }

    public void setTopnews_headline(String topnews_headline) {
        this.topnews_headline = topnews_headline;
    }

    public String getTopnews_url() {
        return topnews_url;
    }

    public void setTopnews_url(String topnews_url) {
        this.topnews_url = topnews_url;
    }


}
