package com.scp.view.base;

import java.awt.Graphics;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.widget.UIDrawImage;

import com.scp.model.sys.Register;
import com.scp.util.AppUtils;
import com.scp.util.CommonUtil;
import com.scp.util.MessageUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "web.registerBean", scope = ManagedBeanScope.REQUEST)
public class RegisterBean extends MastDtlView{
	
	@SaveState
	@Accessible
	public Register selectedRowData = new Register();
	
	@SaveState
	@Accessible
	public Boolean type = false;
	
	@SaveState
	@Accessible
	public String name;
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
			
		}
	}
	
	@Action
	public void register(){
			queryName(name);
			if(type==true){
				return;
			}
		if(checkValidateCode()==true && type== false){
			//selectedRowData.setIscheck(true);
			try {
				serviceContext.registerService.saveData(selectedRowData);
			} catch (Exception e) {
				selectedRowData.setId(0);
				MessageUtils.showException(e);
				return;
			}
			MessageUtils.alert("注册成功!正在审核中...如果审核通过,注意查看注册邮箱邮件!");
			selectedRowData = new Register();
			}
	}
	
	@Action
	public void changname(){
		  this.name = AppUtils.getReqParam("name");
			queryName(name);
		
	}
	
	public void queryName(String name){
		String sql = "SELECT 1 FROM sys_corporation WHERE isdelete = FALSE AND isofficial = TRUE AND iscustomer = TRUE AND abbr = '" + name + "'";
		String sql2 = "SELECT 1 FROM cs_register WHERE isdelete = FALSE AND name = '" + name + "'";
		 List list = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
		 List list2 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql2);
		 if(list.size() > 0 && list2.size() > 0){
			 MessageUtils.alert("您的名称在系统已存在,请重新输入!");
			 type = true;
		 }else{
			 type = false;
		 }
	}
	
	/**
	 * 验证码
	 */
	@Bind
	public String validateCode;
	
	/**
	 * 创建验证码
	 */
	private void createValidateCode() {
		Map<String, Object> session = FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap();
		String randCode = CommonUtil.getRandom(4);//RandomStr.randomStr(4);
		session.put("LOGIN_VALIDATE_CODE", randCode);
	}

	/**
	 * 页面DrawImage组件draw属性绑定的绘图操作函数
	 * 
	 * @param 
	 *            Graphics
	 * @param width
	 *            图片宽度
	 * @param height
	 *            图片高度
	 */
	public void draw(Graphics g, int width, int height) {
		Map<String, Object> session = FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap();
		String randomCode = (String) session
				.get("LOGIN_VALIDATE_CODE");

		if (randomCode != null) {
			CommonUtil.drawRandomPicture(g, width, height, randomCode);
		}
	}

	@Bind(id = "validateCodeImg", attribute = "binding")
	private UIDrawImage validateImg;

	/**
	 * 绑定页面id为validateImg的DrawImage组件的onclick事件， 实现验证码图片重新刷新
	 */
	@Action(id = "refreshValidateCode", immediate = true)
	public void change() {
		createValidateCode();
		validateImg.refresh();
	}
	
	private boolean checkValidateCode() {
		Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		String randomCode = (String)session.get("LOGIN_VALIDATE_CODE");
		if(!randomCode.equalsIgnoreCase(validateCode)) {
			MessageUtils.alert("输入的验证码有误,请重新输入!");
			return false;
		}else{return true;}
	}
	

	@Override
	public void doServiceSaveMaster() {
		
	}

	@Override
	public void init() {
		selectedRowData = new Register();
		createValidateCode();
	}

	@Override
	public void refreshMasterForm() {
		
	}

	@Override
	protected void doServiceFindData() {
		
	}

	@Override
	protected void doServiceSave() {
		
	}

}
