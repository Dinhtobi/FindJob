package com.pblintern.web.Utils;

public class Constant {

    public static String ID_COLUMN = "id";

    public static String NAME_COLUMN = "name";

    public static String JOB_FIELD_COLUMN = "jobField";

    public static String DESCRIPTION_COLUMN = "description";

    public static String REQUIREMENTS_COLUMN = "requirement";

    public static final String DEFAULT_PAGE_NUMBER = "0";

    public static final String DEFAULT_PAGE_SIZE = "30";
    public static final String DEFAULT_FIELD = "0";

    public static final int MAX_PAGE_SIZE = 30;

    public static final String TYPE_SEARCH_TOP_COMPANY = "top";
    public static final String FILTER_SEARCH_CANDIDATE_ACCEPT = "ACCEPT";

    public static final String FILTER_SEARCH_CANDIDATE_REFUSE = "REFUSE";
    public static final String FILTER_SEARCH_CANDIDATE_READED = "READED";
    public static final String FILTER_SEARCH_CANDIDATE_NOTREAD = "NOTREAD";


    public static final String NOTIFICATION_REFUSE = "Kính gửi [candidateName],\n" +
            "\n" +
            "Cảm ơn bạn đã dành thời gian và quan tâm ứng tuyển vào [postName] tại [companyName]. Chúng tôi đã xem xét kỹ lưỡng hồ sơ và kinh nghiệm của bạn.\n" +
            "Sau khi xem xét cẩn thận tất cả các ứng viên, chúng tôi rất tiếc phải thông báo rằng bạn chưa được chọn cho vị trí này. Quyết định này không phản ánh khả năng của bạn mà chỉ là sự lựa chọn phù hợp nhất với yêu cầu cụ thể của vị trí hiện tại.\n" +
            "Chúng tôi đánh giá cao sự nhiệt huyết và kỹ năng của bạn và khuyến khích bạn tiếp tục theo đuổi sự nghiệp của mình. Chúng tôi sẽ giữ lại hồ sơ của bạn cho các vị trí phù hợp trong tương lai và hy vọng có cơ hội hợp tác với bạn sau này.\n" +
            "Một lần nữa, cảm ơn bạn đã quan tâm đến [companyName]. Chúng tôi chúc bạn nhiều thành công trong hành trình sự nghiệp của mình.\n" +
            "Trân trọng,\n" +
            "\n" +
            "[recruiterName]\n" +
            "[recruiterPosition]\n" +
            "[recruiterEmail]\n" +
            "[recruiterPhoneNumber]" +
            "[companyName]\n";
    public static final String NOTIICATION_ACCEPT = "[companyName]\n" +
            "Địa chỉ: [locationCompany]\n" +
            "Email: [emailCompany]\n" +
            "\n" +
            "Ngày [Ngày/Tháng/Năm]\n" +
            "\n" +
            "Kính gửi: [Tên ứng viên]\n" +
            "\n" +
            "THƯ MỜI PHỎNG VẤN\n" +
            "Kính chào [Tên ứng viên],\n" +
            "Chúng tôi rất vui mừng thông báo rằng hồ sơ của bạn đã vượt qua vòng sơ tuyển cho bài tuyển dụng [postName] tại [companyName]. Chúng tôi rất ấn tượng với kinh nghiệm và kỹ năng của bạn và mong muốn có cơ hội được gặp bạn để trao đổi thêm về vị trí này.\n" +
            "Chúng tôi xin trân trọng mời bạn tham gia buổi phỏng vấn vào:\n" +
            "Thời gian: [Giờ, Ngày/Tháng/Năm]\n" +
            "Địa điểm: [Địa điểm phỏng vấn]\n" +
            "Trong buổi phỏng vấn, bạn sẽ có cơ hội gặp gỡ [Tên người phỏng vấn và chức vụ nếu có] và trao đổi thêm về những chi tiết liên quan đến công việc cũng như những gì bạn có thể mong đợi khi làm việc tại [companyName].\n" +
            "Nếu bạn không thể tham dự buổi phỏng vấn vào thời gian đã nêu trên, xin vui lòng liên hệ với chúng tôi qua số điện thoại [Số điện thoại liên hệ] hoặc email [Email liên hệ] để sắp xếp lại thời gian phù hợp.\n" +
            "Chúng tôi rất mong chờ được gặp bạn và thảo luận thêm về cơ hội hợp tác này.\n" +
            "Trân trọng,\n" +
            "\n" +
            "Người gửi: [recruiterName]\n" +
            "Chức vụ: [recruiterPosition]\n" +
            "Email: [recruiterEmail]\n" +
            "Số điện thoại: [recruiterPhoneNumber]\n" +
            "Công ty: [companyName]";

    public static final String NOTIFCATION_POST_EXPIRED = "Kính gửi [recruiterName],\n" +
            "\n" +
            "Chúng tôi xin thông báo rằng bài đăng tuyển dụng của quý công ty cho vị trí [postName] trên website của chúng tôi đã hết hạn vào ngày [postExpire].\n" +
            "\n" +
            "Thông tin bài đăng:\n" +
            "     Tên công việc: [postName]\n" +
            "     Công ty: [companyName]\n" +
            "     Ngày đăng: [postCreateAt]\n" +
            "     Ngày hết hạn: [postExpire]\n" +
            "Hành động tiếp theo:\n" +
            "     Gia hạn bài đăng: Nếu quý công ty muốn gia hạn thời gian đăng tuyển, vui lòng truy cập vào tài khoản của quý công ty trên website để thực hiện gia hạn.\n" +
            "     Cập nhật thông tin: Nếu quý công ty muốn chỉnh sửa hoặc cập nhật thông tin bài đăng, vui lòng truy cập vào tài khoản của quý công ty trên website để thực hiện thay đổi.\n" +
            "     Tạo bài đăng mới: Nếu quý công ty có nhu cầu đăng tuyển vị trí mới, vui lòng truy cập vào tài khoản của quý công ty trên website để tạo bài đăng mới.\n" +
            "Chúng tôi luôn sẵn sàng hỗ trợ quý công ty trong quá trình tuyển dụng. Nếu có bất kỳ thắc mắc hoặc cần hỗ trợ thêm, vui lòng liên hệ với chúng tôi.\n" +
            "\n" +
            "Xin chân thành cảm ơn sự hợp tác của quý công ty.";
    public static final String NOTIFICATION_INVITE_APPLY = "Kính gửi [candidateName],\n" +
            "\n" +
            "Chúng tôi rất vui mừng thông báo rằng bạn đã được chọn vào danh sách ứng viên tiềm năng cho công việc [postName] tại [companyName]. Sau khi xem xét kỹ lưỡng hồ sơ của bạn, chúng tôi nhận thấy rằng bạn có những kỹ năng và kinh nghiệm phù hợp với những gì chúng tôi đang tìm kiếm.\n" +
            "Thông tin về vị trí công việc bạn có thể truy cập đường link bên dưới:\n" +
            "   [postName]: http://localhost:3000/home/job/[postId]\n" +
            "Chúng tôi tin rằng bạn sẽ là một bổ sung quý báu cho đội ngũ của chúng tôi và mong muốn có cơ hội để trao đổi thêm với bạn về vị trí này.Rất mong sớm nhận được phản hồi từ bạn.\n" +
            "Trân trọng,\n" +
            "\n" +
            "Người gửi: [recruiterName]\n" +
            "Vị trí [recruiterPosition]\n" +
            "Công ty: [companyName]\n" +
            "Địa chỉ: [companyLocation]\n" +
            "Email: [recruiterEmail]\n" +
            "Số điện thoại [recruiterPhoneNumber] ";

}
