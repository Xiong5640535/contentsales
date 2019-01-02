package com.xiongyi.contentsales.service.impl;

import com.xiongyi.contentsales.dao.ContentDao;
import com.xiongyi.contentsales.entity.Content;
import com.xiongyi.contentsales.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xiongyi on 2018/12/30.
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private ContentDao contentDao;

    @Override
    public int insert(Content content) {
        return contentDao.insert(content);
    }

    @Override
    public Content getContentById(int id) {
        return contentDao.getContentById(id);
    }

    @Override
    public List<Content> getContentList() {
        return contentDao.getContentList();
    }

    @Override
    public List<Content> getUnpurchasedContentList() {
        return contentDao.getUnpurchasedContentList();
    }

    @Override
    public int updateContentById(Content content) {
        return contentDao.updateContentById(content);
    }

    @Override
    public int deleteContentById(int id) {
        return contentDao.deleteContentById(id);
    }

    @Override
    public int updateContentAmountById(int id, int amount) {
        return contentDao.updateContentAmountById(id,amount);
    }
}
