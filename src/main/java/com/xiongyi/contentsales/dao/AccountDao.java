package com.xiongyi.contentsales.dao;

import com.xiongyi.contentsales.entity.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by xiongyi on 2018/12/30.
 */
@Mapper
public interface AccountDao {

    @Insert(" insert into account(contentId, title, image, price, amount, time) values (#{contentId,jdbcType=INTEGER}, #{title,jdbcType=VARCHAR}, #{image,jdbcType=VARCHAR}, #{price,jdbcType=INTEGER}, #{amount,jdbcType=INTEGER}, #{time,jdbcType=TIMESTAMP})")
    int insert(Account order);

    @Select("select * from account")
    List<Account> getAccounts();

    @Select("select * from account where id = #{id}")
    Account getAccountById(@Param("id")int id);

    @Select("select * from account where contentId = #{contentId}")
    Account getAccountByContentId(@Param("contentId")int contentId);
}
