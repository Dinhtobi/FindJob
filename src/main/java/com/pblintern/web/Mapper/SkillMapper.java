package com.pblintern.web.Mapper;

import com.pblintern.web.Entities.Skills;
import com.pblintern.web.Utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SkillMapper implements RowMapper<Skills> {

    @Autowired
    private Constant constant;

    @Override
    public Skills mapRow(ResultSet rs, int rowNum) throws SQLException {
        Skills skills = new Skills();
        skills.setId(rs.getInt(constant.ID_COLUMN));
        skills.setName(rs.getString(constant.NAME_COLUMN));
        return skills;
    }
}
