package com.xiongyi.contentsales.service;

import com.xiongyi.contentsales.entity.Content;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by xiongyi on 2018/12/30.
 */
public interface ContentService {

    int insert(Content content);

    Content getContentById(@Param("id")int id);

    List<Content> getContentList();

    int updateContentById(Content content);

    int deleteContentById(@Param("id")String id);
}
