package scraper;

import model.StreamData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class VideoScraper {
    private static String baseUrl = "https://aniworld.to";
    private static StreamData fetchStreamData(Element element) {
        Element a = element.getElementsByTag("a").first();
        Element i = a.getElementsByTag("i").first();

        String host = i.attr("title");
        String href = baseUrl + a.attr("href");
        String langKey = element.attr("data-lang-key");

        host = host.startsWith("Hoster ") ? host.substring(7) : host;

        return new StreamData(host, href, langKey);
    }

    public static List<StreamData> fetchStreams(String url) {
        List<StreamData> streamUrls = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Element streamContent = doc.getElementsByClass("hosterSiteVideo").first();
            Element list = streamContent.getElementsByTag("ul").first();
            Elements elements = list.getElementsByTag("li");
            for (Element element : elements) {
                streamUrls.add(fetchStreamData(element));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return streamUrls;
    }
}
