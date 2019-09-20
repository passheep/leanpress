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

import com.jfinal.aop.Aop;
import com.jfinal.kit.Ret;
import com.jfinal.upload.UploadFile;
import com.leanit.core.attachment.AttachmentDownloader;
import com.leanit.module.article.model.Article;
import com.leanit.web.base.AdminControllerBase;
import io.jboot.utils.ArrayUtil;
import io.jboot.utils.FileUtil;
import io.jboot.web.controller.annotation.RequestMapping;
import com.leanit.JPressConsts;
import com.leanit.commons.utils.AttachmentUtils;
import com.leanit.model.Attachment;
import com.leanit.module.article.kit.wordpress.WordPressXmlParser;
import com.leanit.module.article.service.ArticleService;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @author Passheep
 * @version V1.0
 * @Package com.leanit.module.article.controller
 */
@RequestMapping(value = "/admin/setting/tools/wordpress", viewPath = JPressConsts.DEFAULT_ADMIN_VIEW)
public class _WordpressImport extends AdminControllerBase {

    public void index() {
        render("article/wordpress.html");
    }


    public void doWordPressImport() {

        UploadFile ufile = getFile();
        if (ufile == null) {
            renderJson(Ret.fail("message", "您还未选择WordPress文件"));
            return;
        }

        if (!".xml".equals(FileUtil.getSuffix(ufile.getFileName()))) {
            renderJson(Ret.fail("message", "请选择从WordPress导出的XML文件"));
            return;
        }

        String newPath = AttachmentUtils.moveFile(ufile);
        File xmlFile = AttachmentUtils.file(newPath);

        WordPressXmlParser wordPressXmlParser = new WordPressXmlParser();
        wordPressXmlParser.parse(xmlFile);


        List<Article> contents = wordPressXmlParser.getArticles();
        if (ArrayUtil.isNotEmpty(contents)) {
            doSaveArticles(contents);
        }

        List<Attachment> attachments = wordPressXmlParser.getAttachments();
        if (ArrayUtil.isNotEmpty(attachments)) {
            doSaveAttachements(attachments);
        }

        renderOkJson();
    }


    private void doSaveArticles(List<Article> articles) {

        ArticleService service = Aop.get(ArticleService.class);

        for (Article article : articles) {

            if (article.getCreated() == null) {
                article.setCreated(new Date());
                article.setModified(new Date());
            }

            article.setUserId(getLoginedUser().getId());
            service.save(article);
        }
    }

    private void doSaveAttachements(List<Attachment> attachments) {
        for (Attachment attachment : attachments) {

            attachment.setUserId(getLoginedUser().getId());
            attachment.save();

            AttachmentDownloader.download(attachment);
        }
    }


}
