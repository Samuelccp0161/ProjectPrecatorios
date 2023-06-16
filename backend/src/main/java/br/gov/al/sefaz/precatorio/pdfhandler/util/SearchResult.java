package br.gov.al.sefaz.precatorio.pdfhandler.util;

import java.util.List;
import java.util.stream.Collectors;

public class SearchResult {
    private final String searchText;
    private final List<Searcher.Result> results;

    public SearchResult(String searchText, List<Searcher.Result> results) {
        this.searchText = searchText;
        this.results = results;
    }

    public List<Area> getAreas() {
        return results.stream()
                .map(Searcher.Result::getArea)
                .collect(Collectors.toList());
    }

    public List<String> getLines() {
        return results.stream()
                .map(Searcher.Result::getLine)
                .collect(Collectors.toList());
    }

    public String getFirstLine() {
        return results.size() > 0 ? results.get(0).getLine() : "";
    }

    public Area getFirstArea() {
        return results.size() > 0 ? results.get(0).getArea() : null;
    }

    public String getLastLine() {
        return results.size() > 0 ? results.get(results.size() - 1).getLine() : "";
    }

    public Area getFirstAreaInside(Area area) {
        return results.stream().map(Searcher.Result::getArea).filter(area::contains).findFirst().orElse(null);
    }

    public Area getLastArea() {
        return results.size() > 0 ? results.get(results.size() - 1).getArea() : null;
    }
}
