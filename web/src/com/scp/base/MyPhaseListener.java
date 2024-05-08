package com.scp.base;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class MyPhaseListener implements PhaseListener {        
    public int indent = 0;  
    public static final int INDENTSIZE = 2;
    
    public void beforePhase(PhaseEvent pe){
        if (pe.getPhaseId() == PhaseId.RESTORE_VIEW){
            //System.out.println("Processing new  Request!"); 
        }
        //System.out.println("before - " + pe.getPhaseId().toString());
    }
    
    public void afterPhase(PhaseEvent pe){   
        //System.out.println("after - " + pe.getPhaseId().toString());    
        if (pe.getPhaseId() == PhaseId.RENDER_RESPONSE){
            //System.out.println("Done with Request! ");  
        }
        
        if(pe.getPhaseId() == PhaseId.PROCESS_VALIDATIONS) {
//            try{
//                FacesContextImpl context = (FacesContextImpl)pe.getFacesContext();
//                UIViewRoot viewRoot = context.getViewRoot();
//                HtmlGridView saGrid = (HtmlGridView)viewRoot.findComponent("salesAssistant:saGridView");
//                HtmlInputText saNo = (HtmlInputText)viewRoot.findComponent("salesAssistant:SaNo");
//                
//            } catch(Exception ex){
//                ex.printStackTrace();
//            }
            
        }
        
        
    //    printComponentTree(FacesContext.getCurrentInstance().getViewRoot());
    }  
    
    public PhaseId getPhaseId(){      
        return PhaseId.ANY_PHASE;  
    }
    
    public void printComponentTree(UIComponent comp){    
        printComponentInfo(comp);        
        List complist = (ArrayList)comp.getChildren();    
        if (complist.size()>0){
            indent++;    
        }
        for(int i = 0; i < complist.size(); i++){      
            UIComponent uicom = (UIComponent) complist.get(i);      
            printComponentTree(uicom);      
            if (i+1 == complist.size()){
                indent--;    
            }
        }      
    }  
    
    
    public void printComponentInfo(UIComponent comp){     
        if (comp.getId() == null){     
            //System.out.println("UIViewRoot" + " " + "(" + comp.getClass().getName() + ")");   
        } else {       
            printIndent();       
            //System.out.println("|");       
            printIndent();       
            //System.out.println(comp.getId() + " " + "(" + comp.getClass().getName() + ")");     
        }    
    }   
    
    public void printIndent(){    
        for (int i=0; i<indent; i++ ){
            for (int j=0;j<INDENTSIZE; j++){
                //System.out.print(" ");  
            }
        }
    }  
}