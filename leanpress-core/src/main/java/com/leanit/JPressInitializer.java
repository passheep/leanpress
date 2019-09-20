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
package com.leanit;

import com.jfinal.config.Constants;
import com.jfinal.config.Interceptors;
import com.jfinal.config.Routes;
import com.jfinal.kit.PathKit;
import com.jfinal.template.Engine;
import com.leanit.core.addon.AddonManager;
import com.leanit.core.addon.controller.AddonControllerProcesser;
import com.leanit.core.addon.handler.AddonHandlerProcesser;
import com.leanit.core.addon.interceptor.AddonInterceptorProcesser;
import com.leanit.core.install.InstallHandler;
import com.leanit.core.menu.MenuManager;
import com.leanit.core.support.ehcache.EhcacheManager;
import com.leanit.core.wechat.WechatAddonManager;
import com.leanit.web.JPressShareFunctions;
import com.leanit.web.captcha.JPressCaptchaCache;
import com.leanit.web.handler.JPressHandler;
import com.leanit.web.interceptor.JPressInterceptor;
import com.leanit.web.interceptor.UTMInterceptor;
import com.leanit.web.render.JPressRenderFactory;
import com.leanit.web.sitemap.SitemapHandler;
import com.leanit.web.sitemap.SitemapManager;
import io.jboot.aop.jfinal.JfinalHandlers;
import io.jboot.core.listener.JbootAppListenerBase;
import io.jboot.web.fixedinterceptor.FixedInterceptors;
import com.leanit.commons.utils.JPressJson;

import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author Passheep
 * @version V1.0
 * @Title: JPress 初始化工具
 * @Package com.leanit
 */
public class JPressInitializer extends JbootAppListenerBase {

    @Override
    public void onInit() {
        try {
            URL resourceUrl = JPressInitializer.class.getResource("/");
            if (resourceUrl != null) {
                PathKit.setWebRootPath(resourceUrl.toURI().getPath());
            }
            EhcacheManager.init();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConstantConfig(Constants constants) {

        constants.setRenderFactory(new JPressRenderFactory());
        constants.setCaptchaCache(new JPressCaptchaCache());
        constants.setJsonFactory(() -> new JPressJson());

    }

    @Override
    public void onRouteConfig(Routes routes) {
        routes.setClearAfterMapping(false);
    }

    @Override
    public void onFixedInterceptorConfig(FixedInterceptors fixedInterceptors) {
        fixedInterceptors.add(new AddonInterceptorProcesser());
    }


    @Override
    public void onHandlerConfig(JfinalHandlers handlers) {
        handlers.add(new InstallHandler());
        handlers.add(new SitemapHandler());
        handlers.add(new JPressHandler());
        handlers.add(new AddonHandlerProcesser());

        handlers.setActionHandler(new AddonControllerProcesser());
    }

    @Override
    public void onEngineConfig(Engine engine) {
        engine.addSharedStaticMethod(JPressShareFunctions.class);
    }

    @Override
    public void onInterceptorConfig(Interceptors interceptors) {
        interceptors.add(new UTMInterceptor());
        interceptors.add(new JPressInterceptor());
    }

    @Override
    public void onStart() {

        SitemapManager.me().init();
        MenuManager.me().init();
        WechatAddonManager.me().init();
        AddonManager.me().init();

    }

}
