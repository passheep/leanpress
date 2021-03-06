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
package com.leanit.web.directive;

import com.jfinal.aop.Inject;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import com.leanit.web.interceptor.UserInterceptor;
import io.jboot.utils.StrUtil;
import io.jboot.web.directive.annotation.JFinalDirective;
import io.jboot.web.directive.base.JbootDirectiveBase;
import com.leanit.model.User;
import com.leanit.service.RoleService;

import java.util.Set;

/**
 * @author Passheep
 * @version V1.0
 * @Package com.leanit.core.directives
 */
@JFinalDirective("role")
public class RoleDirective extends JbootDirectiveBase {

    @Inject
    private RoleService roleService;

    @Override
    public void onRender(Env env, Scope scope, Writer writer) {

        User user = UserInterceptor.getThreadLocalUser();
        if (user == null || !user.isStatusOk()) {
            return;
        }


        Set<String> roles = StrUtil.splitToSet(getPara(0, scope), ",");
        if (roles == null || roles.size() == 0) {
            throw new IllegalArgumentException("#role(...) argument must not be empty");
        }

        if (roleService.hasRole(user.getId(), roles.toArray(new String[]{}))) {
            renderBody(env, scope, writer);
        }

    }
}

