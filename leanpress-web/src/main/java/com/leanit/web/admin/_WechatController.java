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
package com.leanit.web.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.leanit.core.menu.annotation.AdminMenu;
import com.leanit.core.wechat.WechatAddonInfo;
import com.leanit.core.wechat.WechatAddonManager;
import com.leanit.web.base.AdminControllerBase;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jboot.web.validate.EmptyValidate;
import io.jboot.web.validate.Form;
import io.jboot.wechat.WechatApis;
import com.leanit.JPressConsts;
import com.leanit.commons.layer.SortKit;
import com.leanit.model.WechatMenu;
import com.leanit.model.WechatReply;
import com.leanit.service.OptionService;
import com.leanit.service.WechatMenuService;
import com.leanit.service.WechatReplyService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Passheep
 * @version V1.0
 * @Title: 首页
 * @Package com.leanit.web.admin
 */
@RequestMapping(value = "/admin/wechat", viewPath = JPressConsts.DEFAULT_ADMIN_VIEW)
public class _WechatController extends AdminControllerBase {

    @Inject
    private WechatReplyService replyService;

    @Inject
    private OptionService optionService;

    @Inject
    private WechatMenuService wechatMenuService;

    @AdminMenu(text = "默认回复", groupId = JPressConsts.SYSTEM_MENU_WECHAT_PUBULIC_ACCOUNT, order = 1)
    public void reply() {
        render("wechat/reply_base.html");
    }


    @AdminMenu(text = "自动回复", groupId = JPressConsts.SYSTEM_MENU_WECHAT_PUBULIC_ACCOUNT, order = 2)
    public void keyword() {
        Page<WechatReply> page = replyService._paginate(getPagePara(), 10, getPara("keyword"), getPara("content"));
        setAttr("page", page);
        render("wechat/reply_list.html");
    }

    @AdminMenu(text = "运营插件", groupId = JPressConsts.SYSTEM_MENU_WECHAT_PUBULIC_ACCOUNT, order = 5)
    public void addons() {
        List<WechatAddonInfo> wechatAddons = WechatAddonManager.me().getWechatAddons();
        setAttr("wechatAddons", wechatAddons);
        render("wechat/addons.html");
    }


    @AdminMenu(text = "菜单设置", groupId = JPressConsts.SYSTEM_MENU_WECHAT_PUBULIC_ACCOUNT, order = 12)
    public void menu() {
        List<WechatMenu> menus = wechatMenuService.findAll();
        SortKit.toLayer(menus);
        setAttr("menus", menus);

        int id = getParaToInt(0, 0);
        if (id > 0) {
            for (WechatMenu menu : menus) {
                if (menu.getId() == id) {
                    setAttr("menu", menu);
                }
            }
        }
        render("wechat/menu.html");
    }


    @AdminMenu(text = "基础设置", groupId = JPressConsts.SYSTEM_MENU_WECHAT_PUBULIC_ACCOUNT, order = 21)
    public void base() {
        render("wechat/setting_base.html");
    }


    @AdminMenu(text = "小程序", groupId = JPressConsts.SYSTEM_MENU_WECHAT_PUBULIC_ACCOUNT, order = 99)
    public void miniprogram() {
        render("wechat/miniprogram.html");
    }


    public void doDelReply() {
        Long id = getIdPara();
        replyService.deleteById(id);
        renderOkJson();
    }

    @EmptyValidate(@Form(name = "ids"))
    public void doDelReplyByIds() {
        Set<String> idsSet = getParaSet("ids");
        render(replyService.deleteByIds(idsSet.toArray()) ? OK : FAIL);
    }


    public void doEnableAddon(String id) {
        WechatAddonManager.me().doEnableAddon(id);
        renderOkJson();
    }

    public void doCloseAddon(String id) {
        WechatAddonManager.me().doCloseAddon(id);
        renderOkJson();
    }


    public void keywordWrite() {
        int id = getParaToInt(0, 0);

        WechatReply wechatReply = id > 0 ? replyService.findById(id) : null;
        setAttr("reply", wechatReply);

        Map map = wechatReply == null ? new HashMap<>() : wechatReply.getOptionMap();
        setAttr("option", map);

        render("wechat/reply_write.html");
    }

    public void doReplySave() {
        WechatReply reply = getBean(WechatReply.class, "");
        Map<String, String> map = getParas();
        if (map != null) {
            for (Map.Entry<String, String> e : map.entrySet()) {
                if (e.getKey() != null && e.getKey().startsWith("option.")) {
                    reply.putOption(e.getKey().substring(7), e.getValue());
                }
            }
        }

        replyService.saveOrUpdate(reply);
        renderOkJson();
//        redirect("/admin/wechat/keyword");
    }

    public void doMenuSave() {
        WechatMenu menu = getModel(WechatMenu.class, "menu");
        wechatMenuService.saveOrUpdate(menu);
        redirect("/admin/wechat/menu");
    }

    public void doMenuDel() {
        wechatMenuService.deleteById(getParaToLong());
        renderOkJson();
    }

    /**
     * 微信菜单同步
     */
    public void doMenuSync() {
        List<WechatMenu> wechatMenus = wechatMenuService.findAll();
        SortKit.toTree(wechatMenus);

        if (wechatMenus == null || wechatMenus.isEmpty()) {
            renderJson(Ret.fail().set("message", "微信菜单为空"));
            return;
        }

        JSONArray button = new JSONArray();
        for (WechatMenu wechatMenu : wechatMenus) {
            if (wechatMenu.hasChild()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", wechatMenu.getText());
                List<WechatMenu> childMenus = wechatMenu.getChilds();
                JSONArray sub_buttons = new JSONArray();
                for (WechatMenu child : childMenus) {
                    createJsonObjectButton(sub_buttons, child);
                }
                jsonObject.put("sub_button", sub_buttons);
                button.add(jsonObject);
            } else {
                createJsonObjectButton(button, wechatMenu);
            }
        }

        JSONObject wechatMenuJson = new JSONObject();
        wechatMenuJson.put("button", button);
        String jsonString = wechatMenuJson.toJSONString();

        ApiResult result = WechatApis.createMenu(jsonString);
        if (result.isSucceed()) {
            renderJson(Ret.ok().set("message", "微信菜单同步成功"));
        } else {
            renderJson(Ret.fail().set("message", "错误码：" + result.getErrorCode() + "，" + result.getErrorMsg()));
        }

    }

    private void createJsonObjectButton(JSONArray button, WechatMenu content) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", content.getType());
        jsonObject.put("name", content.getText());

        //跳转网页
        if ("view".equals(content.getType())) {
            jsonObject.put("url", content.getKeyword());
        }
        //跳转微信小程序
        else if ("miniprogram".equals(content.getType())) {
            String[] appIdAndPage = content.getKeyword().split(":");
            jsonObject.put("appid", appIdAndPage[0]);
            jsonObject.put("pagepath", appIdAndPage[1]);
            jsonObject.put("url", getBaseUrl());
        }
        //其他
        else {
            jsonObject.put("key", content.getKeyword());
        }
        button.add(jsonObject);
    }

}
