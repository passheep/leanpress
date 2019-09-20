package com.leanit.module.article.service.search;

import com.jfinal.plugin.activerecord.Page;
import com.leanit.module.article.model.Article;


public interface ArticleSearcher {

    String HIGH_LIGHT_CLASS = "search-highlight";

    public void addArticle(Article article);

    public void deleteArticle(Object id);

    public void updateArticle(Article article);

    public Page<Article> search(String keyword, int pageNum, int pageSize);
}
