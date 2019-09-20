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
package com.leanit.module.article.controller.api;

import com.jfinal.aop.Inject;
import com.leanit.web.base.ApiControllerBase;
import io.jboot.web.controller.annotation.RequestMapping;
import com.leanit.module.article.service.ArticleCategoryService;
import com.leanit.module.article.service.ArticleService;


/**
 * @author Passheep
 * @version V1.0
 * @Title: 文章前台页面Controller
 * @Package com.leanit.module.article.admin
 */
@RequestMapping("/api/article/comment")
public class CommentApiController extends ApiControllerBase {

    @Inject
    private ArticleService articleService;

    @Inject
    private ArticleCategoryService categoryService;


}
