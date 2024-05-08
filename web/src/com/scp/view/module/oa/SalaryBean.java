package com.scp.view.module.oa;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;

import com.scp.base.MultiLanguageBean;
import com.scp.dao.sys.SysUserDao;
import com.scp.model.oa.SalaryBill;
import com.scp.model.sys.SysUser;
import com.scp.service.LoginService;
import com.scp.service.sysmgr.UserMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.oa.salaryBean", scope = ManagedBeanScope.REQUEST)
public class SalaryBean extends GridFormView {

	@SaveState
	@Accessible
	public SalaryBill selectedRowData = new SalaryBill();
	
	@Inject(value = "l")
	protected MultiLanguageBean l;
	
	@Bind
	@SaveState
	public UIWindow verifyWindow;
	
	@ManagedProperty("#{loginService}")
	public LoginService loginService;
	
	@Resource
	public SysUserDao sysUserDao;

	@Resource
	public UserMgrService userMgrService;
	
	@Bind
	public String username;
	
	@Bind
	public String password;
	
	@Bind
	@SaveState
	public String vericode;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			dtlIFrame.setSrc("salarydtl.xhtml?id=0");
			verifyWindow.show();
			username=AppUtils.getUserSession().getUsername();
			////System.out.println("a"+username);
		}
	
	}
	
	@Action(id = "loginBtn")
	public void loginBtn() throws Exception {
		
	}
	
	@Action
    private void changeUserCom() {
		password = AppUtils.getReqParam("password");
		username=AppUtils.getUserSession().getUsername();
	
		username = username.replaceAll("'", "''");
		String usernameUpper = username.toUpperCase();
		List<SysUser> userList = sysUserDao.findAllByClauseWhere("UPPER(code)='" + usernameUpper + "' AND isinvalid = TRUE AND isdelete = FALSE");
		if (userList!=null && userList.size()>0){
			SysUser sysUser = userList.get(0);
			 long userid = sysUser.getId();
			String ciphertext = sysUser.getCiphertext();
			String secretkey = sysUser.getSecretkey();
			String encodePassword = userMgrService.decrypt(ciphertext, secretkey);
			if(AppUtils.isDebug == true) {
				//encodePassword = "123";
				
			}
			if(encodePassword.equals(password)){
				verifyWindow.close();
				vericode="abc";
				this.grid.reload();
			}else{
				MessageUtils.alert("密码错误,请重新输入！");
			}
		}else{
			
			MessageUtils.alert("请重新登陆！");
		}
    }



	@Action
	public void grid_ondblclick() {
		this.pkVal = getGridSelectId();
		showReportChoosen(this.pkVal);
	}
	
	@Bind
	private UIIFrame dtlIFrame;

	
	public void showReportChoosen(Long id) {
		dtlIFrame.setSrc("salarydtl.xhtml?id="+id);
		update.markAttributeUpdate(dtlIFrame, "src");
	}


	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		if(vericode==null){
			qry +="\n1=3";
		}
		else if(vericode.equals("abc")){
			qry += "\nAND namee = (SELECT namec FROM sys_user WHERE id=" + AppUtils.getUserSession().getUserid()+")";
		}else{
			qry +="\n1=2 ";
		}
		m.put("qry", qry);
		return m;
	}
}
