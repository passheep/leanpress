/**
 * Copyright (c) 2016-2019, Michael Yang 杨福海 (fuhai999@gmail.com).
 * <p>
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.leanit.module.article.sitemap;

import com.jfinal.aop.Inject;
import com.leanit.module.article.model.ArticleCategory;
import com.leanit.web.sitemap.Sitemap;
import com.leanit.web.sitemap.SitemapProvider;
import com.leanit.module.article.service.ArticleCategoryService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class ArticleTagSitemapProvider implements SitemapProvider {

    @Inject
    private ArticleCategoryService categoryService;

    @Override
    public String getName() {
        return "article_tags";
    }

    @Override
    public Date getLastmod() {
        List<Sitemap> sitemaps = getSitemaps();
        return sitemaps == null || sitemaps.isEmpty() ? null : sitemaps.get(0).getLastmod();
    }


    @Override
    public List<Sitemap> getSitemaps() {
        List<ArticleCategory> tagList = categoryService.findListByType(ArticleCategory.TYPE_TAG);
        if (tagList == null || tagList.isEmpty()) {
            return null;
        }
        return tagList.stream()
                .map(Util::toSitemap)
                .collect(Collectors.toList());
    }
}