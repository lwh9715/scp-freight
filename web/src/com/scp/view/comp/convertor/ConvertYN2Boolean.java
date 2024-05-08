package com.scp.view.comp.convertor;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.operamasks.faces.annotation.DefineConverter;

@DefineConverter(id="yn2bool")
public class ConvertYN2Boolean implements Converter, Serializable   {
   
    
    @Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
    	//将页面中输入的String类型数据转换为数据模型中需要的类型和值。
		if("true".equalsIgnoreCase(value)){
			return "Y";
		}else{
			return "N";
		}
	}
	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		//将数据模型中的值转换为页面要显示的String类型字符串。
    	if("Y".equals(value)){
			return "true";
		}else if("N".equals(value)){
			return "false";
		}
    	return value.toString();
	}
}