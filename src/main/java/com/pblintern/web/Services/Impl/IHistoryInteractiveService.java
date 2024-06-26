package com.pblintern.web.Services.Impl;

import com.pblintern.web.Entities.HistoryInteractive;
import com.pblintern.web.Entities.Post;
import com.pblintern.web.Entities.Candidate;
import com.pblintern.web.Exceptions.BadRequestException;
import com.pblintern.web.Exceptions.NotFoundException;
import com.pblintern.web.Payload.Responses.BaseResponse;
import com.pblintern.web.Payload.Responses.InteractiveResponse;
import com.pblintern.web.Repositories.HistoryInteractiveRepository;
import com.pblintern.web.Repositories.PostRepository;
import com.pblintern.web.Repositories.CandidateRepository;
import com.pblintern.web.Repositories.projection.HistoryInteractiveProjection;
import com.pblintern.web.Services.HistoryInteractiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class IHistoryInteractiveService implements HistoryInteractiveService {

    @Autowired
    private HistoryInteractiveRepository historyInteractiveRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Override
    public BaseResponse<String> addInteractive(int id) {
        Post post = postRepository.findById(id).orElseThrow(()-> new NotFoundException("Post not found!"));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Candidate candidate = candidateRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Seek not found!"));
        Optional<HistoryInteractive> historyInteractiveOptional = historyInteractiveRepository.findBySeekerIdAndPostId(candidate.getId(), post.getId());
        if(historyInteractiveOptional.isEmpty()){
            HistoryInteractive historyInteractive = new HistoryInteractive();
            historyInteractive.setPost_id(post.getId());
            historyInteractive.setCandidate_id(candidate.getId());
            historyInteractive.setCreateAt(new Date());
            historyInteractiveRepository.save(historyInteractive);
            return new BaseResponse<>(null, "success");

        }else{
            return new BaseResponse<>(null, "exits!");
        }
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
