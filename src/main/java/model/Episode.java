package model;

public class Episode {
    private int season;
    private int episode;
    private String url;

    public Episode(int season, int episode, String url) {
        this.season = season;
        this.episode = episode;
        this.url = url;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "season=" + season +
                ", episode=" + episode +
                ", url=" + url +
                '}';
    }
}
