package com.scp.view.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ActionListener;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UITabLayout;
import org.operamasks.faces.component.widget.ExtConfig;
import org.operamasks.faces.component.widget.UISeparator;
import org.operamasks.faces.component.widget.UIToolBar;
import org.operamasks.faces.component.widget.menu.UICommandMenuItem;
import org.operamasks.faces.component.widget.menu.UIMenu;

import com.scp.base.MultiLanguageBean;
import com.scp.util.AppUtils;
import com.scp.util.CommonUtil;
import com.scp.util.TagPanelUtil;

@ManagedBean(name = "index.indexBean", scope = ManagedBeanScope.REQUEST)
public class IndexBean {

    private static ExtConfig config = new ExtConfig();
    private static ExtConfig config2 = new ExtConfig();

    static {
        config.set("closable", false);
        config.set("border", false);
        config2.set("border", false);
    }

    /**
     * 注入服务提供Bean，该Bean提供各种的业务操作对象
     */
//    @ManagedProperty("#{serviceBean}")
//    private ServiceBean service;

    @Bind
    private UITabLayout centerPanel;
    
    
    @Bind
	public UIIFrame showMessageIframe;

    /**
     * 绑定页面上的ToolBar组件
     */
    @Bind
    private UIToolBar toolBar;
    
    @Inject(value = "l")
	private MultiLanguageBean l;

    @BeforeRender
    protected void beforeRender(boolean isPostBack) {
//        initBaseResource();
        ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> session = eContext.getSessionMap();
//        User user = (User) session.get(IUserConstants.USER_BEAN);
        if (!isPostBack) {
//            List<ModuleItem> validItems = user.getRole().getModuleItems();

        	try {
				if (!AppUtils.isLogin()) {
					eContext.redirect("/../login_new/login.xhtml");
					return;
				}
			} catch (Exception e) {
				try {
					eContext.redirect("../login_new/login.xhtml");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return;
			}
			
        	
        	FacesContext context = FacesContext.getCurrentInstance();
            Application application = context.getApplication();
            
//            List<Module> modules = service.getModuleService().findAll();
            
            List<Module> modules = new ArrayList<Module>();
            Module moduleP = new Module(2334l,"操作中心");
            
            Set<Module> set = new HashSet();
            Module moduleC = new Module(2334010l,"FCL" , set);
            set.add(moduleC);
//            moduleP.setModule(set);
            
            
//            Set<Module> setL = new HashSet();
            Module moduleCL = new Module(23340102l,"LCL" , set);
            set.add(moduleCL);
            moduleP.setModule(set);
//            modules.add(moduleP);
            
            modules.add(moduleP);
            
            
            
            if (modules != null) {
                for (Module module : modules) {
//                    // 创建菜单
                    UIMenu menu = (UIMenu) application.createComponent(UIMenu.COMPONENT_TYPE);
                    menu.setLabel(module.getName());
                    menu.setImage(module.getIcon());
                    
                    Set<Module> pitem = module.getModule();
                    for (Module module2 : pitem) {
                    	UIMenu menu3 = (UIMenu) application.createComponent(UIMenu.COMPONENT_TYPE);
                        menu3.setLabel(module2.getName());
                        menu3.setImage(module2.getIcon());
                    	menu.getChildren().add(menu3);
					}
                    
//
//                    List<ModuleItem> items = service.getModuleItemService().listByModule(module);
                    List<ModuleItem> items = new ArrayList<ModuleItem>();
                    if (items != null) {
                        for (ModuleItem item : items) {
                            // 创建菜单下拉项
                            UICommandMenuItem uiItem = (UICommandMenuItem) application.createComponent(UICommandMenuItem.COMPONENT_TYPE);
                            uiItem.setLabel(item.getName());
                            uiItem.setImage(CommonUtil.getBasePath() + item.getIcon());
                            uiItem.setValue(item.getUrl());
                            // 添加事件监听
                            uiItem.addActionListener(CommonUtil.createMethodExpressionActionListener(context, "#{mdl.indexBean.redirect}"));
//                            boolean valid = false;
//                            for (ModuleItem moduleItem : validItems) {
//                                if (item.getId().equals(moduleItem.getId())) {
//                                    valid = true;
//                                    break;
//                                }
//                            }
//                            if (!valid) {
//                                uiItem.setDisabled(true);
//                            }
                            menu.getChildren().add(uiItem);
                        }
                    }

                    toolBar.getChildren().add(menu);
                    UISeparator separator = (UISeparator) application.createComponent(UISeparator.COMPONENT_TYPE);
                    toolBar.getChildren().add(separator);
                }

            }
        }
//            help();
//        }
//        String label = getMessages().get(ILocalStringsKey.WELCOME_LABEL);
//        if (label != null) {// 替换占位符，将登陆用户的名称载如标语
//            welcome = String.format(label, user.getName());
//        }
//        footerHelp = getMessages().get(ILocalStringsKey.HELP_LABEL);
//        footerTermsOfUse = getMessages().get(ILocalStringsKey.TERMS_OF_USE_LABEL);
//        footerDeclareOfLaw = getMessages().get(ILocalStringsKey.DECLARE_OF_LAW_LABEL);
    }

    /**
     * 绑定帮助按钮点击事件
     */
    @Action(id = "help")
    public void help() {
        //centerPanel.addTab(getMessages().get(ILocalStringsKey.MENU_HELP_LABEL), "help.xhtml", "help_tpcls", config);
    }

    /**
     * 事件监听函数，用于执行菜单按钮点击后的页面切换的脚本回调
     * 
     */
    @ActionListener
    public void redirect(ActionEvent e) {
        if (e.getComponent() instanceof UICommandMenuItem) {
            UICommandMenuItem item = (UICommandMenuItem) e.getComponent();
            Object obj = item.getValue();
            String url = (obj instanceof String ? (String) obj : obj.toString());
            centerPanel.addTab(item.getLabel(), url, TagPanelUtil.getTabPanelIconCls(item.getImage()), config2);
        }
    }

    /**
     * 绑定注销按钮的点击事件
     */
    @Action(id = "logout")
    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> session = context.getExternalContext().getSessionMap();
        session.put("UserSession", null);// 清空Session的USER_BEAN记录
        return "view:redirect:login";
    }

    /**
     * 绑定欢迎标语Text域的值
     */
    @Bind(id = "welcome", attribute = "value")
    private String welcome;

    /**
     * 绑定帮助链接的显示值
     */
    @Bind(id = "footerHelp", attribute = "value")
    private String footerHelp;

    /**
     * 绑定使用条款链接的显示值
     */
    @Bind(id = "footerTermsOfUse", attribute = "value")
    private String footerTermsOfUse;

    /**
     * 绑定法律申明链接的显示值
     */
    @Bind(id = "footerDeclareOfLaw", attribute = "value")
    private String footerDeclareOfLaw;
}
