package com.pblintern.web.Services;

import com.pblintern.web.Payload.Responses.SearchResonse;

import java.util.List;

public interface HistorySeachService {
    SearchResonse addSearch(String keyword);

    List<SearchResonse> getSearch();
}
