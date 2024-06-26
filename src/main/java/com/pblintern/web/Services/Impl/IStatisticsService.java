package com.pblintern.web.Services.Impl;

import com.pblintern.web.Entities.Post;
import com.pblintern.web.Entities.Recruiter;
import com.pblintern.web.Exceptions.NotFoundException;
import com.pblintern.web.Payload.Responses.*;
import com.pblintern.web.Repositories.ApplicationRepository;
import com.pblintern.web.Repositories.PostRepository;
import com.pblintern.web.Repositories.RecruiterRepository;
import com.pblintern.web.Repositories.projection.ApplicationStatisticChartProjection;
import com.pblintern.web.Repositories.projection.ApplicationSumProjection;
import com.pblintern.web.Repositories.projection.StatisticApplicationProjection;
import com.pblintern.web.Repositories.projection.StatisticPostProjection;
import com.pblintern.web.Services.ApplicationService;
import com.pblintern.web.Services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IStatisticsService implements StatisticsService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationService applicationService;

    @Override
    public StatisticsforRecruiterResponse statisticsForRecruiter() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Recruiter recruiter = recruiterRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Recruiter not found!"));

        List<Post> posts = postRepository.getMyJob(recruiter.getId());
        List<Integer> postIds = new ArrayList<>();
        posts.stream().forEach(p -> postIds.add(p.getId()));
        int sumApplication = applicationService.getSumApplicationForRecruiter(postIds);
        List<Post> showPost = posts.stream().filter(p -> p.getExpire().after(new Date())).collect(Collectors.toList());


        return new StatisticsforRecruiterResponse(posts.size(), sumApplication, showPost.size(),0);
    }

    @Override
    public StatisticChartResponse statisticsChart(String startDate, String endDate, String type) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Recruiter recruiter = recruiterRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Recruiter not foudn!"));
        StatisticChartResponse response = new StatisticChartResponse();
        if(type == null){
            type = "daily";
        }
        Date startDateFormat = new Date();
        Date endDateFormat = new Date();
        if(startDate != null && endDate != null){
            try{
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                startDateFormat = simpleDateFormat.parse(startDate);
                endDateFormat = simpleDateFormat.parse(endDate);
            }catch(Exception e){

            }
        }
        Map<Integer, Integer> totalRows = new HashMap<>();
        Map<Integer, Integer> totalAccept = new HashMap<>();
        Map<Integer, Integer> totalWaiting = new HashMap<>();
        Map<Integer, Integer> totalRefuse =  new HashMap<>();

        int sumApply = 0;
        int sumAccept = 0;
        int sumWait = 0;
        int sumRefuse = 0;
        List<StatisticResponse> statistic = new ArrayList<>();
        if(type.equals("daily")){
            List<ApplicationStatisticChartProjection> lists = applicationRepository.statisticChartDaylyForRecruiter(startDateFormat, endDateFormat, recruiter.getId());


            for (ApplicationStatisticChartProjection application : lists) {
                int id = application.getPostId();
                String status = application.getStatus();

                totalRows.put(id, totalRows.getOrDefault(id, 0) + 1);

                if (status.equals("ACCEPT")) {
                    totalAccept.put(id, totalAccept.getOrDefault(id, 0) + 1);
                }

                if (status.equals("WAITING")) {
                    totalWaiting.put(id, totalWaiting.getOrDefault(id, 0) + 1);
                }

                if (status.equals("REFUSE")) {
                    totalRefuse.put(id, totalRefuse.getOrDefault(id, 0) + 1);
                }
            }
            for (Integer id : totalRows.keySet()) {
                Post post = postRepository.findById(id).orElseThrow(()-> new NotFoundException("Post not found!"));
                int total =  totalRows.get(id) == null ? 0 : totalRows.get(id);
                int numberAccept =  totalAccept.get(id)  == null ? 0 : totalAccept.get(id);
                int numberWaiting =  totalWaiting.get(id) == null ? 0 : totalWaiting.get(id);
                int numberRefuse =  totalRefuse.get(id) == null ? 0 : totalRefuse.get(id);
                statistic.add(new StatisticResponse(post.getName(),total ,numberAccept,numberWaiting,numberRefuse ));
                sumApply += total;
                sumAccept += numberAccept;
                sumWait += numberWaiting;
                sumRefuse += numberRefuse;
            }

        }else if(type.equals("monthly")){
            List<ApplicationStatisticChartProjection> lists = applicationRepository.statisticChartMonthlyForRecruiter(startDateFormat, endDateFormat, recruiter.getId());

            for (ApplicationStatisticChartProjection application : lists) {
                int id = application.getPostId();
                String status = application.getStatus();

                totalRows.put(id, totalRows.getOrDefault(id, 0) + 1);

                if (status.equals("ACCEPT")) {
                    totalAccept.put(id, totalAccept.getOrDefault(id, 0) + 1);
                }

                if (status.equals("WAITING")) {
                    totalWaiting.put(id, totalWaiting.getOrDefault(id, 0) + 1);
                }

                if (status.equals("REFUSE")) {
                    totalRefuse.put(id, totalRefuse.getOrDefault(id, 0) + 1);
                }
            }

            for (Integer id : totalRows.keySet()) {
                Post post = postRepository.findById(id).orElseThrow(()-> new NotFoundException("Post not found!"));
                int total =  totalRows.get(id) == null ? 0 : totalRows.get(id);
                int numberAccept =  totalAccept.get(id)  == null ? 0 : totalAccept.get(id);
                int numberWaiting =  totalWaiting.get(id) == null ? 0 : totalWaiting.get(id);
                int numberRefuse =  totalRefuse.get(id) == null ? 0 : totalRefuse.get(id);
                statistic.add(new StatisticResponse(post.getName(),total ,numberAccept,numberWaiting,numberRefuse ));
                sumApply += total;
                sumAccept += numberAccept;
                sumWait += numberWaiting;
                sumRefuse += numberRefuse;
            }

        }else{
            List<ApplicationStatisticChartProjection> lists = applicationRepository.statisticChartYearlyForRecruiter(startDateFormat, endDateFormat, recruiter.getId());
            for (ApplicationStatisticChartProjection application : lists) {
                int id = application.getPostId();
                String status = application.getStatus();

                totalRows.put(id, totalRows.getOrDefault(id, 0) + 1);

                if (status.equals("ACCEPT")) {
                    totalAccept.put(id, totalAccept.getOrDefault(id, 0) + 1);
                }

                if (status.equals("WAITING")) {
                    totalWaiting.put(id, totalWaiting.getOrDefault(id, 0) + 1);
                }

                if (status.equals("REFUSE")) {
                    totalRefuse.put(id, totalRefuse.getOrDefault(id, 0) + 1);
                }
            }

            for (Integer id : totalRows.keySet()) {
                Post post = postRepository.findById(id).orElseThrow(()-> new NotFoundException("Post not found!"));
                int total =  totalRows.get(id) == null ? 0 : totalRows.get(id);
                int numberAccept =  totalAccept.get(id)  == null ? 0 : totalAccept.get(id);
                int numberWaiting =  totalWaiting.get(id) == null ? 0 : totalWaiting.get(id);
                int numberRefuse =  totalRefuse.get(id) == null ? 0 : totalRefuse.get(id);
                statistic.add(new StatisticResponse(post.getName(),total ,numberAccept,numberWaiting,numberRefuse ));
                sumApply += total;
                sumAccept += numberAccept;
                sumWait += numberWaiting;
                sumRefuse += numberRefuse;
            }
        }
        response.setStatistic(statistic);
        response.setSumAccept(sumAccept);
        response.setSumApply(sumApply);
        response.setSumWait(sumWait);
        response.setSumRefuse(sumRefuse);
        return response;
    }

    @Override
    public StatisticChartForAdminResponse statisticForAmin(String startDate, String endDate, String type) {
        StatisticChartForAdminResponse response = new StatisticChartForAdminResponse();
        if(type == null){
            type = "daily";
        }
        Date startDateFormat = new Date();
        Date endDateFormat = new Date();
        if(startDate != null && endDate != null){
            try{
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                startDateFormat = simpleDateFormat.parse(startDate);
                endDateFormat = simpleDateFormat.parse(endDate);
            }catch(Exception e){

            }
        }
        Map<String, Integer> totalPost = new HashMap<>();
        Map<String , Integer> totalApply = new HashMap<>();

        int sumPost = 0;
        double avgPost = 0;
        int sumApply = 0;
        double avgApply = 0;
        int sumPostHN = 0;
        int sumPostHCM = 0;
        int sumPostDN = 0;
        int sumOrther = 0;
        List<StatisticForAdminResponse> statistic = new ArrayList<>();
        if(type.equals("daily")){
            List<StatisticPostProjection> listsPost = postRepository.getStatisticForDayPost(startDateFormat, endDateFormat);

            for(int i =0 ; i< listsPost.size() ;i++){
                sumPost += listsPost.get(i).getCount();
                totalPost.put(listsPost.get(i).getDay(), totalPost.getOrDefault(listsPost.get(i).getDay(), 0) + listsPost.get(i).getCount());
            }
            List<StatisticApplicationProjection> listsApplication = applicationRepository.getStatisticForDayPost(startDateFormat,endDateFormat);
            for(int i =0 ; i< listsApplication.size() ;i++){
                sumApply += listsApplication.get(i).getCount();
                totalApply.put(listsApplication.get(i).getDay(), totalApply.getOrDefault(listsApplication.get(i).getDay(), 0) + listsApplication.get(i).getCount());
            }
            for (String id : totalPost.keySet()) {
                statistic.add(new StatisticForAdminResponse(id, totalPost.get(id), totalApply.get(id)));
            }
            avgPost = sumPost / totalPost.size();
            avgApply = sumApply / totalApply.size();
            sumPostDN = postRepository.getStatisticLocationForDayPost(startDateFormat, endDateFormat,"%Đà Nẵng%").getCount();
            sumPostHCM = postRepository.getStatisticLocationForDayPost(startDateFormat, endDateFormat, "%HCM%").getCount();
            sumPostHN = postRepository.getStatisticLocationForDayPost(startDateFormat, endDateFormat, "%Hà Nội%").getCount();
            sumOrther = postRepository.getStatisticLocationOtherForDayPost(startDateFormat,endDateFormat).getCount();
        }else if(type.equals("monthly")){
            List<StatisticPostProjection> listsPost = postRepository.getStatisticForMonthPost(startDateFormat, endDateFormat);
            Calendar calFrom = Calendar.getInstance();
            calFrom.setTime(startDateFormat);
            int monthFrom = calFrom.get(Calendar.MONTH) + 1;

            Calendar calTo= Calendar.getInstance();
            calTo.setTime(endDateFormat);
            int monthTo = calTo.get(Calendar.MONTH);

            for(int i =0 ; i< listsPost.size() ;i++){
                sumPost += listsPost.get(i).getCount();
                totalPost.put(String.valueOf(listsPost.get(i).getDay()), totalPost.getOrDefault(String.valueOf(listsPost.get(i).getDay()), 0) + listsPost.get(i).getCount());
            }
            List<StatisticApplicationProjection> listsApplication = applicationRepository.getStatisticForMonthPost(startDateFormat,endDateFormat);
            for(int i =0 ; i< listsApplication.size() ;i++){
                sumApply += listsApplication.get(i).getCount();
                totalApply.put(String.valueOf(listsApplication.get(i).getDay()), totalApply.getOrDefault(String.valueOf(listsApplication.get(i).getDay()), 0) + listsApplication.get(i).getCount());
            }
            for (String id : totalPost.keySet()) {
                statistic.add(new StatisticForAdminResponse(id, totalPost.get(id), totalApply.get(id)));
            }
            avgPost = sumPost / totalPost.size();
            avgApply = sumApply / totalApply.size();
            sumPostDN = postRepository.getStatisticLocationForMonthPost(monthFrom, monthTo, "%Đà Nẵng%").getCount();
            sumPostHCM = postRepository.getStatisticLocationForMonthPost(monthFrom, monthTo, "%HCM%").getCount();
            sumPostHN = postRepository.getStatisticLocationForMonthPost(monthFrom, monthTo, "%Hà Nội%").getCount();
            sumOrther = postRepository.getStatisticLocationOtherForMonthPost(monthFrom,monthTo).getCount();

        }else{
            List<StatisticPostProjection> listsPost = postRepository.getStatisticForYearPost(startDateFormat, endDateFormat);
            Calendar calFrom = Calendar.getInstance();
            calFrom.setTime(startDateFormat);
            int yearFrom = calFrom.get(Calendar.YEAR);

            Calendar calTo= Calendar.getInstance();
            calTo.setTime(endDateFormat);
            int yearTo = calTo.get(Calendar.YEAR);
            for(int i =0 ; i< listsPost.size() ;i++){
                sumPost += listsPost.get(i).getCount();
                totalPost.put(String.valueOf(listsPost.get(i).getDay()), totalPost.getOrDefault(String.valueOf(listsPost.get(i).getDay()), 0) + listsPost.get(i).getCount());
            }
            List<StatisticApplicationProjection> listsApplication = applicationRepository.getStatisticForYearPost(startDateFormat,endDateFormat);
            for(int i =0 ; i< listsApplication.size() ;i++){
                sumApply += listsApplication.get(i).getCount();
                totalApply.put(String.valueOf(listsApplication.get(i).getDay()), totalApply.getOrDefault(String.valueOf(listsApplication.get(i).getDay()), 0) + listsApplication.get(i).getCount());
            }
            for (String id : totalPost.keySet()) {
                statistic.add(new StatisticForAdminResponse(id, totalPost.get(id), totalApply.get(id)));
            }
            avgPost = sumPost / totalPost.size();
            avgApply = sumApply / totalApply.size();
            sumPostDN = postRepository.getStatisticLocationForYearPost(yearFrom, yearTo, "%Đà Nẵng%").getCount();
            sumPostHCM = postRepository.getStatisticLocationForYearPost(yearFrom, yearTo, "%HCM%").getCount();
            sumPostHN = postRepository.getStatisticLocationForYearPost(yearFrom, yearTo, "%Hà Nội%").getCount();
            sumOrther = postRepository.getStatisticLocationOtherForYearPost(yearFrom,yearTo).getCount();
        }
        response.setStatistic(statistic);
        response.setSumPost(sumPost);
        response.setAvgPost(avgPost);
        response.setSumApply(sumApply);
        response.setAvgApply(avgApply);
        response.setSumPostHN(sumPostHN);
        response.setSumPostHCM(sumPostHCM);
        response.setSumPostDN(sumPostDN);
        response.setSumOther(sumOrther);
        return response;
    }
}
