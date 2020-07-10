package com.zixuan007.society.window.society.president;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseData;
import cn.nukkit.form.window.FormWindow;
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
public class SetJobWindow extends CustomWindow implements WindowLoader {
    public SetJobWindow() {
        super(PluginUtils.getWindowConfigInfo("setJobWindow.title"));
    }

    @Override
    public FormWindow init(Object... objects) {
        addElement(new ElementInput("", "§e玩家名称"));
        ElementDropdown elementDropdown = new ElementDropdown("§e职位列表", SocietyUtils.getAllPost());
        addElement(elementDropdown);
        return this;
    }


    @Override
    public void onClick(FormResponseCustom response, Player player) {
        String playerName = response.getInputResponse(0);
        FormResponseData dropdownResponse = response.getDropdownResponse(1);
        final String jobName = dropdownResponse.getElementContent();
        final int jobGrade = SocietyUtils.getPostGradeByName(jobName);
        FormWindow setJobWindow = WindowManager.getFormWindow(WindowType.SET_JOB_WINDOW);
        String backButtonName = PluginUtils.getWindowConfigInfo("messageWindow.back.button");
        String backButtonImage = PluginUtils.getWindowConfigInfo("messageWindow.back.button.imgPath");

        if (player.getName().equals(playerName)) {
            player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, PluginUtils.getLanguageInfo("message.setJobWindow.isMe"), setJobWindow, backButtonName, backButtonImage));
            return;
        }
        if (!SocietyUtils.isJoinSociety(playerName)) {
            player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW,  PluginUtils.getLanguageInfo("message.setJobWindow.isJoinSociety"), setJobWindow, backButtonName, backButtonImage));
            return;
        }
        Society society = SocietyUtils.getSocietyByPlayerName(playerName);
        if (society == null || society.getPost() == null || !society.getPost().containsKey(playerName)) {
            player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, PluginUtils.getLanguageInfo("message.setJobWindow.notSocietyMember"), setJobWindow, backButtonName, backButtonImage));
            return;
        }
        society.getPost().put(playerName, new ArrayList() {
            {
                add(jobName);
                add(jobGrade);
            }
        });
        SocietyUtils.saveSociety(society);

        if(PluginUtils.isOnlineByName(playerName)){
            Server.getInstance().getPlayer(playerName).sendTitle(PluginUtils.getLanguageInfo("message.setJobWindow.upJob",new String[]{"${jobName}"},new String[]{jobName}));
        }
        player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, PluginUtils.getLanguageInfo("message.setJobWindow.setJob",new String[]{"${playerName}","${jobName}"},new String[]{playerName,jobName}), setJobWindow, backButtonName, backButtonImage));
    }


}


