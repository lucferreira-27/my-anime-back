package com.lucferreira.myanimeback.service.scraper.mal.top;

import com.lucferreira.myanimeback.model.TopList;
import com.lucferreira.myanimeback.util.Regex;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopListFactory {
    public TopList createTopList(String url){

        final TopList.TopType topType = url.contains("topanime.php") ? TopList.TopType.ANIME : TopList.TopType.MANGA;
        if(url.endsWith(".php")){
            return new TopList(topType,TopList.TopSubtype.ALL,url);
        }

        final String urlSubType = Regex.match(url,"(?<=type=)[^&]+");
        final List<TopList.TopSubtype> subtypes = List.of(TopList.TopSubtype.values());
        final TopList.TopSubtype subtype = getSubType(urlSubType, subtypes);
        return new TopList(topType,subtype,url);

    }

    private TopList.TopSubtype getSubType(String urlSubType, List<TopList.TopSubtype> subtypes) {
        for (TopList.TopSubtype subtype: subtypes) {
            if(subtype.getPossiblesName().contains(urlSubType)){
                return subtype;
            }
        }
        return null;
    }
}
