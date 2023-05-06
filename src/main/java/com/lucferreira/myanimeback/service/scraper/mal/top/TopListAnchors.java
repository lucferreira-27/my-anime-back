package com.lucferreira.myanimeback.service.scraper.mal.top;

import com.lucferreira.myanimeback.service.scraper.DocSelector;
import com.lucferreira.myanimeback.service.scraper.TextSelector;

import java.util.List;

public enum TopListAnchors {
    TOP_LIST_INIT("text",
            new DocSelector(".ranking-list",false,"[0-9]+"),
            new DocSelector("#content tbody tr"),
            new DocSelector("#contentWrapper tbody tr"),
            new DocSelector("#rightcontent_nopad tbody tr"),
            new DocSelector("#rightcontent td:nth-child(1) tbody tr:not(:first-child)")
    ),
    TOP_LIST_SCORE("score",
            new DocSelector(".score-label",true,"(\\d+\\.\\d{2})"),
            new DocSelector(".score.ac",true,"(\\d+\\.\\d{2})"),
            new DocSelector(".Lightbox_AddEdit ~ div",true,"scored\\s(?<score>[\\d.]+)"),
            new DocSelector(".spaceit_pad",true,"scored\\s(?<score>[\\d.]+)"),
            new DocSelector("td:nth-child(4)",true,"(\\d+\\.\\d{2})")
    ),
    TOP_LIST_RANK("rank",
            new DocSelector(".ac",true,"[0-9]+"),
            new DocSelector("td > .lightLink",true,"[0-9]+"),
            new DocSelector("td:nth-child(1)",true,"[0-9]+")
    ),
    TOP_LIST_TITLE("title",
            new DocSelector(".anime_ranking_h3 a",true),
            new DocSelector(".title .hoverinfo_trigger",true),
            new DocSelector(".hoverinfo_trigger strong",true),
            new DocSelector("td:nth-child(3)",true)
    ),
    TOP_LIST_URL("url",
            new DocSelector(".anime_ranking_h3 a",true),
            new DocSelector(".title .hoverinfo_trigger",true),
            new DocSelector(".picSurround a.hoverinfo_trigger",true),
            new DocSelector("td:nth-child(2) a:nth-child(2)",true)
    ),
    TOP_LIST_TYPE("type",
            new DocSelector(".information",true,"^(?<type>[\\w\\s]+)"),
            new DocSelector(".Lightbox_AddEdit ~ div",true,"^(?<type>[\\w\\s]+),"),
            new DocSelector(".spaceit_pad",true,"^(?<type>[\\w\\s]+),")
    ),
    TOP_LIST_COUNT("count",
            new DocSelector(".information",true,"\\((?<count>\\d+|\\?)\\s\\w+\\)"),
            new DocSelector(".Lightbox_AddEdit ~ div",true,"(?<=,\\s)(?<count>\\d+)(?=\\seps)"),
            new DocSelector(".spaceit_pad",true,"(?<=,\\s)(?<count>\\d+)(?=\\seps)"),
            new DocSelector("td:nth-child(5)",true)
    ),
    TOP_LIST_DATE("date",
            new DocSelector(".information",true,"(?<startDate>\\w{3}\\s\\d{4}) - (?<endDate>(?:(?<endMonth>\\s?\\w{3})\\s?)?(?<endYear>\\d{4}))")
    ),
    TOP_LIST_MEMBERS("members",
            new DocSelector(".information",true,"(?<=\\s)(?<members>[\\d,]+)\\smembers"),
            new DocSelector(".Lightbox_AddEdit ~ div",true,"(?<=\\s)(?<members>[\\d,]+)\\smembers"),
            new DocSelector(".spaceit_pad",true,"(?<=\\s)(?<members>[\\d,]+)\\smembers")
    );

    private final List<DocSelector> selectors;
    private final String type;

    /**
     * Constructor for TopListAnchors.
     * @param type the type of the TopList anchor
     * @param selectors the list of selectors for the TopList anchor
     */
    TopListAnchors(String type, DocSelector ... selectors) {
        this.selectors = selectors != null ? List.of(selectors) : null;
        this.type = type;
    }

    /**
     * Getter method for type.
     * @return the type of the TopList anchor
     */
    public String getType() {
        return type;
    }

    /**
     * Getter method for selectors.
     * @return the list of selectors for the TopList anchor
     */
    public List<DocSelector> getSelectors() {
        return selectors;
    }
}
