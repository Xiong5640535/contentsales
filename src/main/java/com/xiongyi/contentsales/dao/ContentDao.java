package com.xiongyi.contentsales.dao;

import com.xiongyi.contentsales.entity.Content;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by xiongyi on 2018/12/30.
 */
@Mapper
public interface ContentDao {
    @Insert(" insert into content(title, summary, image, price, text) values (#{title,jdbcType=VARCHAR}, #{summary,jdbcType=VARCHAR}, #{image,jdbcType=VARCHAR}, #{price,jdbcType=INTEGER}, #{text,jdbcType=VARCHAR})")
    int insert(Content content);

    @Select("select * from content where id = #{id} and valid = 1")
    Content getContentById(@Param("id")int id);

    @Select("select * from content where valid = 1")
    List<Content> getContentList();

    @Select("select * from content where num = 0 and valid = 1")
    List<Content> getUnpurchasedContentList();

    @Update("update content set title=#{title}, summary=#{summary}, image=#{image}, price=#{price}, text=#{text} where id=#{id}")
    int updateContentById(Content content);

    @Update("update content set valid=0 where id = #{id}")
    int deleteContentById(@Param("id")int id);

    @Update("update content set num=#{num} where id = #{id}")
    int updateContentNumById(@Param("id")int id, @Param("num")int num);
}
