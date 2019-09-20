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

import com.jfinal.plugin.activerecord.Page;
import com.leanit.model.Utm;
import com.leanit.service.UtmService;
import com.leanit.service.task.UtmBatchSaveTask;
import io.jboot.aop.annotation.Bean;
import io.jboot.db.model.Column;
import io.jboot.service.JbootServiceBase;

@Bean
public class UtmServiceProvider extends JbootServiceBase<Utm> implements UtmService {

    @Override
    public void doRecord(Utm utm) {
        UtmBatchSaveTask.record(utm);
    }

    @Override
    public Page<Utm> _paginateByUserId(int page, int pagesize, long userId) {
        return DAO.paginateByColumn(page, pagesize, Column.create("user_id", userId), "created desc");
    }
}