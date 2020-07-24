package com.zixuan007.society.window.society.president;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseData;
import cn.nukkit.form.window.FormWindow;
import com.zixuan007.society.SocietyPlugin;
import com.zixuan007.society.domain.Society;
import com.zixuan007.society.utils.PluginUtils;
import com.zixuan007.society.utils.SocietyUtils;
import com.zixuan007.society.window.CustomWindow;
import com.zixuan007.society.window.WindowLoader;
import com.zixuan007.society.window.WindowManager;
import com.zixuan007.society.window.WindowType;

import java.util.ArrayList;

/**
 * @author zixuan007
 */
public class SendSocietyWarWindow extends CustomWindow implements WindowLoader {

    public SendSocietyWarWindow() {
        super( PluginUtils.getWindowConfigInfo("sendSocietyWarWindow.title"));
    }

    @Override
    public FormWindow init(Object... objects) {
        getElements().clear();
        Player player = (Player) objects[0];
        ArrayList<String> arrayList = new ArrayList<>();

        for (Society configSociety : SocietyUtils.societies) {
            if(configSociety.getPresidentName().equals(player.getName())){
               continue;
            }
            arrayList.add(configSociety.getSid()+" "+configSociety.getSocietyName());
        }
        addElement(new ElementDropdown("列表",arrayList));
        addElement(new ElementInput("","发起公会战争的金额"));

        return this;
    }

    @Override
    public void onClick(FormResponseCustom response, Player player) {
        Integer config = (Integer) SocietyPlugin.getInstance().getConfig().get("money");
        FormResponseData dropdownResponse = response.getDropdownResponse(0);
        String sid=dropdownResponse.getElementContent().split(" ")[0];
        String societyName = dropdownResponse.getElementContent().split(" ")[1];
        String moneyStr = response.getInputResponse(1);
        String backButtonName = PluginUtils.getWindowConfigInfo("messageWindow.back.button");
        String backButtonImage = PluginUtils.getWindowConfigInfo("messageWindow.back.button.imgPath");

        String closeButtonName = PluginUtils.getWindowConfigInfo("messageWindow.close.button");
        String closeButtonImage = PluginUtils.getWindowConfigInfo("messageWindow.close.button.imgPath");
        FormWindow formWindow = WindowManager.getFormWindow(WindowType.SEND_SOCIETY_WAR_WINDOW);
        if(!SocietyUtils.isNumeric(moneyStr)){
            player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, "当前输入的发送金额不是数字", formWindow, backButtonName, backButtonImage));
            return;
        }

        int money = Integer.parseInt(moneyStr);
        if(money < config){
            player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, "当前发起公会战金额过低,最低发起金额需要: "+config, formWindow, backButtonName, backButtonImage));
            return;
        }

        Society society = SocietyUtils.getSocietysByID(Long.parseLong(sid));


        player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW,"成功向: "+societyName+" 发起公会战!",null,closeButtonName,closeButtonImage));
    }
}
