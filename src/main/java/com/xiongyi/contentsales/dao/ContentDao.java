package com.xiongyi.contentsales.dao;

import com.xiongyi.contentsales.entity.Content;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by xiongyi on 2018/12/30.
 */
@Mapper
public interface ContentDao {
    @Insert(" insert into content(title, summary, image, price, text, amount) values (#{title,jdbcType=VARCHAR}, #{summary,jdbcType=VARCHAR}, #{image,jdbcType=VARCHAR}, #{price,jdbcType=INTEGER}, #{text,jdbcType=VARCHAR},#{amount,jdbcType=INTEGER})")
    int insert(Content content);

    @Select("select * from content where id = #{id} and valid = 1")
    Content getContentById(@Param("id")int id);

    @Select("select * from content where valid = 1")
    List<Content> getContentList();

    @Select("select * from content where amount = 0 and valid = 1")
    List<Content> getUnpurchasedContentList();

    @Update("update content set title=#{title}, summary=#{summary}, image=#{image}, price=#{price}, text=#{text}, amount=#{amount} where id=#{id}")
    int updateContentById(Content content);

    @Update("update content set valid=#{valid} where id = #{id}")
    int deleteContentById(@Param("id")int id);

    @Update("update content set amount=#{amount} where id = #{id}")
    int updateContentAmountById(@Param("id")int id, @Param("amount")int amount);
}
