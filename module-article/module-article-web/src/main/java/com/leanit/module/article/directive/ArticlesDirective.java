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
package com.leanit.module.article.directive;

import com.jfinal.aop.Inject;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import com.leanit.module.article.model.Article;
import io.jboot.db.model.Columns;
import io.jboot.utils.StrUtil;
import io.jboot.web.directive.annotation.JFinalDirective;
import io.jboot.web.directive.base.JbootDirectiveBase;
import com.leanit.module.article.service.ArticleService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Passheep
 * @version V1.0
 * @Package com.leanit.module.page.directive
 */
@JFinalDirective("articles")
public class ArticlesDirective extends JbootDirectiveBase {

    @Inject
    private ArticleService service;


    @Override
    public void onRender(Env env, Scope scope, Writer writer) {

        String flag = getPara("flag", scope);
        String style = getPara("style", scope);
        Boolean hasThumbnail = getParaToBool("hasThumbnail", scope);
        String orderBy = getPara("orderBy", scope, "id desc");
        int count = getParaToInt("count", scope, 10);


        Columns columns = Columns.create("flag", flag);

        if (StrUtil.isNotBlank(style)) {
            if (style.contains(",")) {
                List<String> styleParas = Arrays.stream(style.split(","))
                        .filter(StrUtil::notBlank).map(s -> s.trim()).collect(Collectors.toList());
                columns.in("style", styleParas.toArray());
            } else {
                columns.add("style", style);
            }
        }

        columns.add("status", Article.STATUS_NORMAL);

        if (hasThumbnail != null) {
            if (hasThumbnail) {
                columns.is_not_null("thumbnail");
            } else {
                columns.is_null("thumbnail");
            }
        }

        List<Article> articles = service.findListByColumns(columns, orderBy, count);

        if (articles == null || articles.isEmpty()) {
            return;
        }

        scope.setLocal("articles", articles);
        renderBody(env, scope, writer);
    }


    @Override
    public boolean hasEnd() {
        return true;
    }
}
