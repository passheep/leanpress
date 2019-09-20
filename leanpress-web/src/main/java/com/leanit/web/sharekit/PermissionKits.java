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
package com.leanit.web.sharekit;

import com.jfinal.aop.Aop;
import com.leanit.web.interceptor.UserInterceptor;
import io.jboot.Jboot;
import com.leanit.model.Role;
import com.leanit.model.User;
import com.leanit.service.PermissionService;
import com.leanit.service.RoleService;

/**
 * @author Passheep
 * @version V1.0
 * @Package com.leanit.core.web.sharekit
 */

public class PermissionKits {

    public static boolean hasPermission(Role role, long permissionId) {
        RoleService service = Jboot.bean(RoleService.class);
        return service.hasPermission(role.getId(), permissionId);
    }

    public static boolean hasPermission(User user, long permissionId) {
        PermissionService service = Jboot.bean(PermissionService.class);
        return service.hasPermission(user.getId(), permissionId);
    }


    public static final boolean hasRole(long userId, long roleId) {
        RoleService roleService = Aop.get(RoleService.class);
        return roleService.hasRole(userId, roleId);
    }

    public static final boolean hasRole(long roleId) {
        User user = UserInterceptor.getThreadLocalUser();
        RoleService roleService = Aop.get(RoleService.class);
        return roleService.hasRole(user.getId(), roleId);
    }

    public static final boolean hasRole(String roleFlag) {
        User user = UserInterceptor.getThreadLocalUser();
        RoleService roleService = Aop.get(RoleService.class);
        return roleService.hasRole(user.getId(), roleFlag);
    }

    public static final boolean hasRole(long userId, String roleFlag) {
        RoleService roleService = Aop.get(RoleService.class);
        return roleService.hasRole(userId, roleFlag);
    }

    public static final boolean isSupperAdmin() {
        User user = UserInterceptor.getThreadLocalUser();
        RoleService roleService = Aop.get(RoleService.class);
        return roleService.isSupperAdmin(user.getId());
    }
}
