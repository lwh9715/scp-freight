package com.scp.base;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;

import org.operamasks.faces.annotation.DefineConverter;
import org.operamasks.faces.component.layout.UIFacelet;

@DefineConverter(id = "dateTimeFormatter")
public class DateTimeFormatter implements Converter {
    
    public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
        FacesContext context = FacesContext.getCurrentInstance();
        DateTimeConverter converter = (DateTimeConverter) context.getApplication().createConverter(DateTimeConverter.CONVERTER_ID);
        converter.setPattern("yyyy-MM-dd mm:ss");
        return converter.getAsObject(context, new UIFacelet(), value);
    }

    public String getAsString(FacesContext ctx, UIComponent component, Object value) {
        if (value instanceof Date) {
            SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getInstance();
            formatter.applyPattern("yyyy-MM-dd mm:ss");
            return formatter.format(value);
        }
        return value == null ? "" : value.toString();
    }
}