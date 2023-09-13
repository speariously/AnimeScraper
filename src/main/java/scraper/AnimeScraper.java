package scraper;

import model.Episode;
import model.EpisodeTracker;
import model.StreamData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class AnimeScraper {
    public static String baseUrl = "https://aniworld.to/anime/stream/";
    private String anime;
    private int seasonCount;
    private EpisodeTracker tracker;

    public AnimeScraper(String name) {
        this.anime = nameToUrl(name);
        this.seasonCount = scrapeSeasonCount();
        this.tracker = new EpisodeTracker(this);
    }

    public int getSeasonCount() {
        return seasonCount;
    }

    private String nameToUrl(String name) {
        return name.toLowerCase().replace(" ", "-");
    }
    private int scrapeSeasonCount() {
        try {
            Document doc = Jsoup.connect(baseUrl + anime).get();
            Element animeNav = doc.getElementById("stream");
            Element seasonList = animeNav.getElementsByTag("ul").first();
            String lastSeasonStringIndex = seasonList.getElementsByTag("li").last().text();
            return Integer.parseInt(lastSeasonStringIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int scrapeEpisodeCount(int season) {
        try {
            Document doc = Jsoup.connect(baseUrl + anime + "/staffel-" + season).get();
            Element animeNav = doc.getElementById("stream");
            Element seasonList = animeNav.getElementsByTag("ul").last();
            String lastSeasonStringIndex = seasonList.getElementsByTag("li").last().text();
            return Integer.parseInt(lastSeasonStringIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getEpisodeUrl() {
        return String.format(baseUrl + anime + "/staffel-%d/episode-%d", tracker.getSeason(), tracker.getEpisode());
    }

    public List<Episode> scrapeEpisodes(StreamFilter filter, int startSeason, int startEpisode, int maxEpisodeCount) {
        if (!tracker.set(startSeason, startEpisode)) {
            System.out.println("episode not found");
            return new ArrayList<>();
        }

        List<Episode> episodes = new ArrayList<>();
        do {
            List<StreamData> streams = VideoScraper.fetchStreams(getEpisodeUrl());
            Episode e = new Episode(tracker.getSeason(), tracker.getEpisode(), filter.filter(streams).url());
            episodes.add(e);
            maxEpisodeCount --;
        } while (maxEpisodeCount > 0 && tracker.next());
        return episodes;
    }
}
