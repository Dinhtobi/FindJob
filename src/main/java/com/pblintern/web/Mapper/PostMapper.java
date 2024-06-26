package com.pblintern.web.Mapper;

import com.pblintern.web.Payload.Requests.CSVRequest;
import com.pblintern.web.Utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostMapper implements RowMapper<CSVRequest> {

    @Autowired
    private Constant constant;

    @Override
    public CSVRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
        CSVRequest post = new CSVRequest();
        post.setId(rs.getInt(constant.ID_COLUMN));
        post.setName(rs.getString(constant.NAME_COLUMN));
        post.setJobField(rs.getString(constant.JOB_FIELD_COLUMN));
        post.setDescription(rs.getString(constant.DESCRIPTION_COLUMN));
        post.setRequirements(rs.getString(constant.REQUIREMENTS_COLUMN));
        return post;
    }
}
