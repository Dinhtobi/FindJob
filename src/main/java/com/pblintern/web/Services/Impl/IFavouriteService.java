package com.pblintern.web.Services.Impl;

import com.pblintern.web.Entities.Candidate;
import com.pblintern.web.Entities.Favourite;
import com.pblintern.web.Entities.Post;
import com.pblintern.web.Exceptions.NotFoundException;
import com.pblintern.web.Payload.Responses.BaseResponse;
import com.pblintern.web.Repositories.CandidateRepository;
import com.pblintern.web.Repositories.FavouriteRepository;
import com.pblintern.web.Repositories.PostRepository;
import com.pblintern.web.Services.FavouriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class IFavouriteService implements FavouriteService {

    @Autowired
    private FavouriteRepository favouriteRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Override
    public BaseResponse<String> addFavourite(int id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found!"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Candidate candidate = candidateRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Seeker not found!"));
        Optional<Favourite> favouriteOptional = favouriteRepository.findByCandidateAndPost(candidate.getId(), post.getId());
        if(favouriteOptional.isEmpty()){
            Favourite favourite = new Favourite();
            favourite.setCandidate_id(candidate.getId());
            favourite.setPost_id(post.getId());
            favourite.setCreateAt(new Date());
            favouriteRepository.save(favourite);
            return new BaseResponse<>(null, "Success!");
        }else{
            return new BaseResponse<>(null, "exits!");
        }
    }

    @Override
    public BaseResponse<String> deleteFavourite(int id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found!"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Candidate candidate = candidateRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Seeker not found!"));

        Favourite favourite = favouriteRepository.findByCandidateAndPost(candidate.getId(), post.getId()).orElseThrow(() -> new NotFoundException("Application not found!"));
        favouriteRepository.delete(favourite);
        return new BaseResponse<>(null, "Success!");
    }
}
