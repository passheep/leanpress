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
package com.leanit.web.interceptor;

import com.jfinal.aop.Inject;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.leanit.core.menu.MenuGroup;
import com.leanit.core.menu.MenuManager;
import io.jboot.utils.CookieUtil;
import io.jboot.utils.StrUtil;
import com.leanit.JPressConfig;
import com.leanit.JPressConsts;
import com.leanit.model.User;
import com.leanit.service.RoleService;
import com.leanit.service.UserService;
import com.leanit.web.handler.JPressHandler;

import java.util.List;

/**
 * @author Passheep
 * @version V1.0
 * @Title: 管理后台的拦截器
 * @Package com.leanit.web
 */
public class AdminInterceptor implements Interceptor {


    @Inject
    private UserService userService;

    @Inject
    private RoleService roleService;


    public void intercept(Invocation inv) {

        if (JPressHandler.getCurrentTarget().equals(JPressConfig.me.getAdminLoginPage())) {
            inv.getController().forwardAction("/admin/login");
            return;
        }

        if (JPressHandler.getCurrentTarget().equals(JPressConfig.me.getAdminLoginAction())) {
            inv.getController().forwardAction("/admin/doLogin");
            return;
        }

        String uid = CookieUtil.get(inv.getController(), JPressConsts.COOKIE_UID);
        if (StrUtil.isBlank(uid)) {

            //当用户未配置自定义登录页面，直接跳转到登录页面
            if (JPressConfig.DEFAULT_LOGIN_PAGE.equals(JPressConfig.me.getAdminLoginPage())) {
                inv.getController().redirect(JPressConfig.DEFAULT_LOGIN_PAGE);
            }
            //如果用户配置了自定义的登录页面，则直接渲染404，否则会暴露用户配置的登录页面
            //这样一来，用户配置的后台登录页面就没有意义了
            else {
                inv.getController().renderError(404);
            }

            return;
        }

        User user = userService.findById(uid);
        if (user == null || !user.isStatusOk()) {
            inv.getController().renderError(404);
            return;
        }

        //不允许没有任何权限的用户访问后台
        if (!roleService.hasAnyRole(user.getId())) {
            inv.getController().renderError(404);
            return;
        }

        List<MenuGroup> systemMenuGroups = MenuManager.me().getSystemMenus();
        List<MenuGroup> moduleMenuGroups = MenuManager.me().getModuleMenus();

        inv.getController().setAttr("systemMenuGroups", systemMenuGroups);
        inv.getController().setAttr("moduleMenuGroups", moduleMenuGroups);

        inv.getController().setAttr(JPressConsts.ATTR_LOGINED_USER, user);

        inv.invoke();
    }


}
