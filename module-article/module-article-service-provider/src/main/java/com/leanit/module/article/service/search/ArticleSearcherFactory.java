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
package com.leanit.module.article.service.search;


import com.jfinal.aop.Aop;
import com.leanit.JPressOptions;
import com.leanit.module.article.search.AliyunOpenSearcher;
import com.leanit.module.article.search.ElasticSearcher;
import com.leanit.module.article.searcher.DbSearcher;
import com.leanit.module.article.searcher.LuceneSearcher;
import io.jboot.core.spi.JbootSpiLoader;
import io.jboot.utils.StrUtil;

public class ArticleSearcherFactory {

    public static ArticleSearcher getSearcher() {

        boolean searchEnable = JPressOptions.isTrueOrEmpty("article_search_enable");
        if (!searchEnable) {
            return new NoneSearcher();
        }

        String engine = JPressOptions.get("article_search_engine");
        if (StrUtil.isBlank(engine)) {
            return Aop.get(DbSearcher.class);
        }

        switch (engine) {
            case "sql":
                return Aop.get(DbSearcher.class);
            case "lucene":
                return Aop.get(LuceneSearcher.class);
            case "es":
                return Aop.get(ElasticSearcher.class);
            case "aliopensearch":
                return Aop.get(AliyunOpenSearcher.class);
            default:
        }

        ArticleSearcher searcher = JbootSpiLoader.load(ArticleSearcher.class, engine);
        return searcher != null ? Aop.inject(searcher) : Aop.get(DbSearcher.class);

    }
}
