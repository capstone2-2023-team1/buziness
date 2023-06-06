package com.example.nerfbiz.src.convert;

import com.example.nerfbiz.src.convert.model.GetRenderingRes;
import com.example.nerfbiz.src.convert.model.GetVideoRes;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ConvertDao {

    private JdbcTemplate jdbcTemplate;

    public ConvertDao(DataSource dataSource){this.jdbcTemplate = new JdbcTemplate(dataSource);}

    public int lastInsertId(){
        String lastInsertIdQuery = "select last_insert_id() from object";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public List<GetVideoRes> getVideos(int userIdx){
        String getVideosQuery = "select * from object where user_idx = ?";
        int getVideosParams = userIdx;
        return this.jdbcTemplate.query(getVideosQuery,
                (rs, rowNum) -> new GetVideoRes(
                    rs.getString("id"),
                        rs.getString("video_url")),
                getVideosParams
        );
    }


    public List<GetRenderingRes> getRenderings(int userIdx){
        String getRenderingsQuery = "select id, mesh_url from object where user_idx = ?";
        int getRenderingsParams = userIdx;
        return this.jdbcTemplate.query(getRenderingsQuery,
                (rs, rowNum)->new GetRenderingRes(
                        rs.getString("id"),
                        rs.getString("mesh_url")
                ), getRenderingsParams);
    }

    public void createObject(String objectId, int userIdx, String video_url){
        String createObjectQuery = "insert into object (id, user_idx, video_url) VALUES (?, ?, ?)";
        Object[] createObjectParams = new Object[]{objectId, userIdx, video_url};
        this.jdbcTemplate.update(createObjectQuery, createObjectParams);
    }

    public void saveObjUrl(String objectId, String obj_url){
        String saveObjUrlQuery = "update object set obj_url = ? where id = ?";
        Object[] saveObjUrlParams = new Object[]{objectId, obj_url};
        this.jdbcTemplate.update(saveObjUrlQuery, saveObjUrlParams);
    }



}
