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
package com.leanit.module.article.controller;

import com.jfinal.aop.Inject;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.Ret;
import com.jfinal.upload.UploadFile;
import com.leanit.module.article.model.Article;
import com.leanit.module.article.model.ArticleCategory;
import com.leanit.web.base.AdminControllerBase;
import io.jboot.utils.FileUtil;
import io.jboot.web.controller.annotation.RequestMapping;
import com.leanit.JPressConsts;
import com.leanit.commons.utils.AttachmentUtils;
import com.leanit.module.article.kit.markdown.MarkdownParser;
import com.leanit.module.article.service.ArticleCategoryService;
import com.leanit.module.article.service.ArticleService;

import java.io.File;
import java.text.ParseException;
import java.util.List;

/**
 * @author Ryan Wang（i@ryanc.cc）
 * @version V1.0
 * @Package com.leanit.module.article.controller
 */
@RequestMapping(value = "/admin/setting/tools/markdown", viewPath = JPressConsts.DEFAULT_ADMIN_VIEW)
public class _MarkdownImport extends AdminControllerBase {
    @Inject
    private ArticleService articleService;
    @Inject
    private ArticleCategoryService articleCategoryService;

    public void index() {
        render("article/markdown.html");
    }


    public void doMarkdownImport() {

        UploadFile ufile = getFile();
        if (ufile == null) {
            renderJson(Ret.fail("message", "您还未选择Markdown文件"));
            return;
        }

        if (!".md".equals(FileUtil.getSuffix(ufile.getFileName()))) {
            renderJson(Ret.fail("message", "请选择Markdown格式的文件"));
            return;
        }

        String newPath = AttachmentUtils.moveFile(ufile);
        File mdFile = AttachmentUtils.file(newPath);

        MarkdownParser markdownParser = new MarkdownParser();
        markdownParser.parse(mdFile);

        Article article = null;
        String[] categoryNames = null;

        try {
            article = markdownParser.getArticle();
            categoryNames = markdownParser.getCategories();
        } catch (ParseException e) {
            LogKit.error(e.toString(), e);
            renderJson(Ret.fail("message", "导入失败，可能markdown文件格式错误"));
        } finally {
            mdFile.delete();
        }

        if (null != article) {
            article.setUserId(getLoginedUser().getId());
            articleService.save(article);
        }

        if (null != article && null != categoryNames && categoryNames.length > 0) {
            List<ArticleCategory> categoryList = articleCategoryService.doNewOrFindByCategoryString(categoryNames);
            Long[] allIds = new Long[categoryList.size()];
            for (int i = 0; i < allIds.length; i++) {
                allIds[i] = categoryList.get(i).getId();
            }
            articleService.doUpdateCategorys(article.getId(), allIds);
        }

        renderOkJson();
    }
}
