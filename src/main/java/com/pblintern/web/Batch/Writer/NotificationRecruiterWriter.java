package com.pblintern.web.Batch.Writer;

import com.pblintern.web.Entities.Notification;
import com.pblintern.web.Entities.Post;
import com.pblintern.web.Enums.NotificationEnum;
import com.pblintern.web.Repositories.NotificationRepository;
import com.pblintern.web.Repositories.PostRepository;
import com.pblintern.web.Utils.Constant;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationRecruiterWriter implements ItemWriter<Post> {

    private final NotificationRepository notificationRepository;


    public NotificationRecruiterWriter(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void write(Chunk<? extends Post> chunk) throws Exception {
        for(Post p : chunk){
            Notification notification = new Notification();
            notification.setIsRead(false);
            notification.setUser(p.getRecruiter().getUser());
            notification.setPost(p);
            notification.setSource(NotificationEnum.EXPIREDPOSTSTATUS);
            notification.setCreateAt(new Date());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String meassge = Constant.NOTIFCATION_POST_EXPIRED.replaceAll("\\[recruiterName\\]",p.getRecruiter().getUser().getFullName())
                            .replaceAll("\\[postName\\]", p.getName())
                            .replaceAll("\\[postExpire\\]", formatter.format(p.getExpire()))
                            .replaceAll("\\[postCreateAt\\]",  formatter.format(p.getCreateAt()))
                            .replaceAll("\\[companyName\\]", p.getCompany().getName());
            notification.setMessage(meassge);
            notificationRepository.save(notification);
        }
    }
}
