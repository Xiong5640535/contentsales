package com.xiongyi.contentsales.service;

import com.xiongyi.contentsales.entity.Content;

import java.util.List;

/**
 * Created by xiongyi on 2018/12/30.
 */
public interface ContentService {

    int insert(Content content);

    Content getContentById(int id);

    List<Content> getContentList();

    List<Content> getUnpurchasedContentList();

    int updateContentById(Content content);

    int deleteContentById(int id);

    int updateContentNumById(int id, int amount);
}
