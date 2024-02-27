package com.pblintern.web.Services.Impl;

import com.pblintern.web.Entities.HistoryInteractive;
import com.pblintern.web.Entities.Post;
import com.pblintern.web.Entities.Seeker;
import com.pblintern.web.Exceptions.BadRequestException;
import com.pblintern.web.Exceptions.NotFoundException;
import com.pblintern.web.Payload.Responses.InteractiveResponse;
import com.pblintern.web.Payload.Responses.SearchResonse;
import com.pblintern.web.Repositories.HistoryInteractiveRepository;
import com.pblintern.web.Repositories.PostRepository;
import com.pblintern.web.Repositories.SeekerRepository;
import com.pblintern.web.Repositories.projection.HistoryInteractiveProjection;
import com.pblintern.web.Repositories.projection.HistorySearchProjection;
import com.pblintern.web.Services.HistoryInteractiveService;
import com.pblintern.web.Services.HistorySeachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class IHistoryInteractiveService implements HistoryInteractiveService {

    @Autowired
    private HistoryInteractiveRepository historyInteractiveRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SeekerRepository seekerRepository;

    @Override
    public InteractiveResponse addInteractive(int id) {
        HistoryInteractive historyInteractive = new HistoryInteractive();
        Post post = postRepository.findById(id).orElseThrow(()-> new NotFoundException("Post not found!"));
        historyInteractive.setJobPost(post);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seeker seeker = seekerRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Seek not found!"));
        historyInteractive.setSeeker(seeker);
        historyInteractive.setCreateAt(new Date());
        historyInteractiveRepository.save(historyInteractive);
        return new InteractiveResponse(historyInteractive.getId());
    }

    @Override
    public List<InteractiveResponse> getInteractive() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<HistoryInteractiveProjection> historyInteractiveProjections = historyInteractiveRepository.findFiveDESC(email);
        if(historyInteractiveProjections == null)
            throw new BadRequestException("Searches is empty!");
        List<InteractiveResponse> list = new ArrayList<>();

        historyInteractiveProjections.stream().forEach(h -> list.add(new InteractiveResponse(h.getIdPost())));
        return list;
    }
}
