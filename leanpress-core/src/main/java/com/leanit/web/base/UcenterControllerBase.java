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
package com.leanit.web.base;

import com.jfinal.aop.Before;
import com.jfinal.core.NotAction;
import com.leanit.web.interceptor.CSRFInterceptor;
import com.leanit.web.interceptor.UserCenterInterceptor;
import com.leanit.web.interceptor.UserInterceptor;
import com.leanit.web.interceptor.UserMustLoginedInterceptor;

/**
 * @author Passheep
 * @version V1.0
 * @Package com.leanit.web
 */
@Before({
        CSRFInterceptor.class,
        UserInterceptor.class,
        UserMustLoginedInterceptor.class,
        UserCenterInterceptor.class
})
public abstract class UcenterControllerBase extends ControllerBase {


//    @Override
//    @NotAction
//    public void render(String view) {
//        if (view.startsWith("/")) {
//            super.render(view);
//        } else {
//            super.render("/WEB-INF/views/ucenter/" + view);
//        }
//    }

    @NotAction
    public int getPagePara() {
        return getParaToInt("page", 1);
    }


}
