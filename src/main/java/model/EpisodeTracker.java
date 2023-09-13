package model;

import scraper.AnimeScraper;

import java.util.HashMap;

public class EpisodeTracker {
    private int episode;
    private int season;
    private HashMap<Integer, Integer> table;
    private AnimeScraper animeScraper;

    public EpisodeTracker(AnimeScraper scraper) {
        episode = 1;
        season = 1;
        table = new HashMap<>();
        animeScraper = scraper;
    }

    private int getSeasonEpisodeCount(int season) {
        if (!table.containsKey(season)) {
            table.put(season, animeScraper.scrapeEpisodeCount(season));
        }
        return table.get(season);
    }

    public int getEpisode() {
        return episode;
    }

    public int getSeason() {
        return season;
    }

    public boolean set(int season, int episode) {
        if (animeScraper.getSeasonCount() < season || getSeasonEpisodeCount(season) < episode) {
            return false;
        }
        this.season = season;
        this.episode = episode;
        return true;
    }

    public boolean next() {
        if (getSeasonEpisodeCount(season) > episode) {
            episode++;
        }
        else if (animeScraper.getSeasonCount() > season) {
            season ++;
            episode = 1;
        }
        else {
            return false;
        }
        return true;
    }
}
