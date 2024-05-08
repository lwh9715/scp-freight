package com.scp.view.base;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.base.CommonComBoxBean;
import com.scp.view.comp.FormView;


@ManagedBean(name = "main.showmoremsgBean", scope = ManagedBeanScope.REQUEST)
public class ShowmoremsgBean extends FormView{
	
	
	@Resource(name="comboxBean")
	public CommonComBoxBean comboxBean;
	
	@Accessible
	public List<SelectItem> trackType = new ArrayList<SelectItem>();
	
	@BeforeRender 
	public void beforeRender(boolean isPostback){
		if(!isPostback){
			
			trackType = comboxBean.getTmptypes();
			//System.out.println(trackType.get(0).getLabel());
//			trackType.add("海运出口");
//			trackType.add("海运出口");
//			trackType.add("海运出口");
//			trackType.add("海运出口");
//			trackType.add("海运出口");
//			trackType.add("海运出口");
//			trackType.add("海运出口");
//			trackType.add("海运出口");
		}
	}
	
	
	
}
