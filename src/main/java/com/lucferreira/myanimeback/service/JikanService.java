package com.lucferreira.myanimeback.service;

import org.springframework.stereotype.Service;

import com.lucferreira.myanimeback.model.media.MediaForm;
import com.lucferreira.myanimeback.util.Regex;
import net.sandrohc.jikan.Jikan;
import net.sandrohc.jikan.exception.JikanQueryException;
import net.sandrohc.jikan.model.anime.Anime;
import net.sandrohc.jikan.model.manga.Manga;

@Service
public class JikanService {

    private final Jikan jikan;

    public JikanService(Jikan jikan) {
        this.jikan = jikan;
    }

    public MediaForm getMediaInfo(String myanimelistUrl) throws JikanQueryException {
        // Implementation to get anime info using Jikan API
        int id = parseUrlToId(myanimelistUrl);
        System.out.println("Id: " + id + " " + myanimelistUrl);
        if(id < 0){
            return null;
        }
        if (isAnime(myanimelistUrl)) {
            Anime anime = jikan.query().anime().get(id).execute().block();
            return MediaForm.fromJikanMedia(anime);
        }
        Manga manga = jikan.query().manga().get(id).execute().block();
        return MediaForm.fromJikanMedia(manga);
    }


private boolean isAnime(String url) {
    String pattern = "https?://myanimelist\\.net/anime/\\d+.*";
    return Regex.match(url, pattern) != null;
}

private int parseUrlToId(String url) {
    String pattern = "https?://myanimelist\\.net/(anime|manga)/(\\d+).*";
    String match = Regex.match(url, pattern);
    if (match != null) {
        return Integer.parseInt(Regex.groups(match, pattern).get(2));
    }
    return -1; // or throw an exception if the URL format is invalid
}
}
