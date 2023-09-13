package scraper;

import model.StreamData;
import value.Host;
import value.LangKey;

import java.util.List;

public class StreamFilter {
    private String[] langKeyOrder;
    private String[] hostOrder;

    public StreamFilter(String[] langKeyOrder, String[] hostOrder) {
        this.langKeyOrder = langKeyOrder;
        this.hostOrder = hostOrder;
    }

    public StreamFilter() {
        this.langKeyOrder = new String[] {LangKey.Deutsch, LangKey.SubDeutsch, LangKey.SubEnglisch};
        this.hostOrder = new String[] {Host.VOE, Host.Doodstream, Host.Vidoza, Host.Streamtape};
    }

    public StreamData filter(List<StreamData> streams) {
        StreamData stream;

        for (int i = 0; i < langKeyOrder.length; i++) {
            int finalI = i;
            List<StreamData> langStreams = streams.stream().filter(s -> s.langKey() == langKeyOrder[finalI]).toList();
            if (!langStreams.isEmpty()) {
                streams = langStreams;
                break;
            }
        }

        for (int i = 0; i < hostOrder.length; i++) {
            int finalI = i;
            List<StreamData> hostStreams = streams.stream().filter(s -> s.host() == hostOrder[finalI]).toList();
            if (!hostStreams.isEmpty()) {
                streams = hostStreams;
                break;
            }
        }

        return streams.get(0);
    }
}
