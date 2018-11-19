package com.tks.gwa.transformer;

import com.tks.gwa.entity.Article;
import org.springframework.stereotype.Service;

@Service
public class ArticleTransformer {

    public Article convertToEntity(com.tks.gwa.jaxb.article.Article jaxb_article) {
        Article entity_article = null;

        if (jaxb_article != null) {
            entity_article = new Article();

            entity_article.setTitle(jaxb_article.getTitle());
            entity_article.setThumbImage(jaxb_article.getThumbImage());
            entity_article.setCategory(jaxb_article.getCategory());
            entity_article.setContent(jaxb_article.getContent());
        }

        return entity_article;
    }
}
