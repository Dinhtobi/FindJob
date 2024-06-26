package com.pblintern.web.Batch.Processor;

import com.pblintern.web.Entities.Notification;
import com.pblintern.web.Entities.Post;
import com.pblintern.web.Exceptions.NotFoundException;
import com.pblintern.web.Repositories.NotificationRepository;
import com.pblintern.web.Repositories.PostRepository;
import org.springframework.batch.item.ItemProcessor;

import java.util.Optional;

public class NotificationRecruiterProcessor implements ItemProcessor<Integer, Post> {

    private final PostRepository postRepository;

    private final NotificationRepository notificationRepository;

    public NotificationRecruiterProcessor(PostRepository postRepository, NotificationRepository notificationRepository) {
        this.postRepository = postRepository;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Post process(Integer item) throws Exception {
        Post post = postRepository.findById(item).orElseThrow(() -> new NotFoundException("Post not found!"));
        Optional<Notification> notification = notificationRepository.getNotificationByPostIdAndRecruiterId(post.getId(), post.getRecruiter().getId());
        if(notification.isEmpty()){
            return post;
        }
        return null;
    }
}
