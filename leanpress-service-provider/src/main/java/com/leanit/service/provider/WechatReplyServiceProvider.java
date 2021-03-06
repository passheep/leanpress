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
package com.leanit.service.provider;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import io.jboot.aop.annotation.Bean;
import io.jboot.db.model.Column;
import io.jboot.db.model.Columns;
import io.jboot.service.JbootServiceBase;
import com.leanit.commons.utils.SqlUtils;
import com.leanit.model.WechatReply;
import com.leanit.service.WechatReplyService;

@Bean
public class WechatReplyServiceProvider extends JbootServiceBase<WechatReply> implements WechatReplyService {

    @Override
    public boolean deleteByIds(Object... ids) {
        return Db.update("delete from wechat_reply where id in  " + SqlUtils.buildInSqlPara(ids)) > 0;
    }

    @Override
    public Page<WechatReply> _paginate(int page, int pageSize, String keyword, String content) {
        Columns columns = new Columns();
        columns.likeAppendPercent("keyword",keyword);
        columns.likeAppendPercent("content",content);
        return DAO.paginateByColumns(page, pageSize, columns, "id desc");
    }

    @Override
    public WechatReply findByKey(String keyword) {
        return DAO.findFirstByColumn(Column.create("keyword", keyword));
    }
}