package com.pblintern.web.Services.Impl;

import com.pblintern.web.Entities.HistorySearch;
import com.pblintern.web.Entities.Seeker;
import com.pblintern.web.Exceptions.BadRequestException;
import com.pblintern.web.Exceptions.NotFoundException;
import com.pblintern.web.Payload.Responses.SearchResonse;
import com.pblintern.web.Repositories.HistorySearchRepository;
import com.pblintern.web.Repositories.SeekerRepository;
import com.pblintern.web.Repositories.projection.HistorySearchProjection;
import com.pblintern.web.Services.HistorySeachService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class IHistorySearchService implements HistorySeachService {

    @Autowired
    private HistorySearchRepository historySearchRepository;

    @Autowired
    private SeekerRepository seekerRepository;

    @Override
    public SearchResonse addSearch(String  keyword) {
        HistorySearch historySearch = new HistorySearch();
        if(keyword.isBlank()){
            throw new BadRequestException("KeyWord is null");
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seeker seeker = seekerRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Seeker not found!"));
        historySearch.setSeeker(seeker);
        historySearch.setKeyWord(keyword);
        historySearch.setCreateAt(new Date());
        historySearchRepository.save(historySearch);
        return new SearchResonse(historySearch.getKeyWord());

    }

    @Override
    public List<SearchResonse> getSearch() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<HistorySearchProjection> historySearchProjections = historySearchRepository.findFiveDESC(email);
        if(historySearchProjections == null)
            throw new BadRequestException("Searches is empty!");
        List<SearchResonse> list = new ArrayList<>();

        historySearchProjections.stream().forEach(h -> list.add(new SearchResonse(h.getKeyWord())));
        return list;
    }
}
