package com.scp.view.module.price;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.scp.base.CommonComBoxBean;
import com.scp.model.data.DatCntype;
import com.scp.model.data.DatFeeitem;
import com.scp.model.price.PriceFclFeeadd;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
import com.scp.vo.price.FclFeeAdd;

@ManagedBean(name = "pages.module.price.addfeetemplatenameBean", scope = ManagedBeanScope.REQUEST)
public class AddFeeTemplatenameBean extends GridView{
    
    @SaveState
	@Accessible
	public PriceFclFeeadd selectedRowData = new PriceFclFeeadd();
    
    @Bind
    @SaveState
    public String jsonArrayText;
    
    @Bind
    @SaveState
    public String batchRefIds;
    
    
    @Bind
    @SaveState
    public ArrayList<Long> idsOldArray = new ArrayList<Long>();
    
    @Bind
    @SaveState
    public ArrayList<Long> idsNewArray = new ArrayList<Long>();
    
    @Bind
	@SaveState
	public String language;
    
    @BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			this.language = AppUtils.getUserSession().getMlType().toString();
			initData();
		}
	}
    
    
    @Action
    public void refresh(){
    	Browser.execClientScript("removeTableRow();");
    	templateComBo = "";
    	update.markUpdate(true, UpdateLevel.Data, "templateComBo");
    	initData();
    	update.markUpdate(true, UpdateLevel.Data, "jsonArrayText");
    	Browser.execClientScript("initData();");
    }
    
    private void initData() {
    	idsOldArray.clear();
        List<PriceFclFeeadd> list = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao.findAllByClauseWhere("templatename='"+templateComBo + "' AND isdelete = false AND istemplate = TRUE ORDER BY id");
		
		List<FclFeeAdd> voList = new ArrayList<FclFeeAdd>();
		for (PriceFclFeeadd priceFclFeeadd : list) {
			FclFeeAdd fclFeeAdd = new FclFeeAdd();
			fclFeeAdd.setAmt(priceFclFeeadd.getAmt());
			fclFeeAdd.setAmt20(priceFclFeeadd.getAmt20());
			fclFeeAdd.setAmt40gp(priceFclFeeadd.getAmt40gp());
			fclFeeAdd.setAmt40hq(priceFclFeeadd.getAmt40hq());
			fclFeeAdd.setAmt45hq(priceFclFeeadd.getAmt45hq());
			fclFeeAdd.setCurrency(priceFclFeeadd.getCurrency());
			fclFeeAdd.setFclid(priceFclFeeadd.getFclid());
			fclFeeAdd.setFeeitemid(priceFclFeeadd.getFeeitemid());
			fclFeeAdd.setFeeitemcode(priceFclFeeadd.getFeeitemcode());
			fclFeeAdd.setFeeitemname(priceFclFeeadd.getFeeitemname());
			try {
				DatFeeitem datFeeitem = serviceContext.feeItemMgrService.datFeeitemDao.findById(priceFclFeeadd.getFeeitemid());
				fclFeeAdd.setFeeitemname(this.language.equals("en") ? (datFeeitem.getNamee()):(priceFclFeeadd.getFeeitemname()));
			} catch (Exception e) {
				fclFeeAdd.setFeeitemname(priceFclFeeadd.getFeeitemname());
			}
			fclFeeAdd.setId(priceFclFeeadd.getId());
			fclFeeAdd.setInputer(priceFclFeeadd.getInputer());
			fclFeeAdd.setPpcc(priceFclFeeadd.getPpcc());
			fclFeeAdd.setUnit(priceFclFeeadd.getUnit());
			fclFeeAdd.setAmtother(priceFclFeeadd.getAmtother());
			fclFeeAdd.setCntypeid(priceFclFeeadd.getCntypeid());
			DatCntype datCntype = serviceContext.cntypeMgrService.datcntypeDao.findById(priceFclFeeadd.getCntypeid()!=null?priceFclFeeadd.getCntypeid():-1);
			fclFeeAdd.setCntypecode(datCntype!=null?datCntype.getCode():"");
			
			idsOldArray.add(priceFclFeeadd.getId());
			
			voList.add(fclFeeAdd);
		}
		Gson gson = new Gson();
		JsonArray jsonArray = new JsonArray();
		for (FclFeeAdd vo : voList) {
			String str = gson.toJson(vo);
			JsonParser parser = new JsonParser();
			JsonElement el = parser.parse(str);
			jsonArray.add(el);
		}
		////AppUtils.debug(jsonArray);
		jsonArrayText = jsonArray.toString();
	}
    
    
    @Bind(id="template")
    public List<SelectItem> getTemplate() {
    	try {
    		Map m = serviceContext.daoIbatisTemplate.
    		queryWithUserDefineSql4OnwRow("SELECT string_agg(corpid||'' , ',') AS corpid FROM sys_user_corplink  WHERE  ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid());
        	String corpid = m.get("corpid").toString();
			return CommonComBoxBean.getComboxItems("DISTINCT templatename","templatename","price_fcl_feeadd AS d","WHERE istemplate=true AND isdelete = false AND templatename is not null AND corpid in ("+corpid+")","ORDER BY templatename");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
    
    @Bind
    @SaveState
    public String templateComBo;
    
    @Action(id = "templateComBo", event = "onselect")
    public void doselect() {
        ////System.out.println(templateComBo);
    	Map m = serviceContext.daoIbatisTemplate.
		queryWithUserDefineSql4OnwRow("SELECT string_agg(corpid||'' , ',') AS corpid FROM sys_user_corplink  WHERE  ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid());
    	String corpid = m.get("corpid").toString();
        if(StrUtils.isNull(templateComBo))return;
        List<PriceFclFeeadd> list = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao.findAllByClauseWhere("templatename='"+templateComBo + "' AND isdelete = false AND corpid in ("+corpid+") AND istemplate = TRUE ORDER BY id");
		
		List<FclFeeAdd> voList = new ArrayList<FclFeeAdd>();
		for (PriceFclFeeadd priceFclFeeadd : list) {
			FclFeeAdd fclFeeAdd = new FclFeeAdd();
			fclFeeAdd.setAmt(priceFclFeeadd.getAmt());
			fclFeeAdd.setAmt20(priceFclFeeadd.getAmt20());
			fclFeeAdd.setAmt40gp(priceFclFeeadd.getAmt40gp());
			fclFeeAdd.setAmt40hq(priceFclFeeadd.getAmt40hq());
			fclFeeAdd.setAmt45hq(priceFclFeeadd.getAmt45hq());
			fclFeeAdd.setCurrency(priceFclFeeadd.getCurrency());
			fclFeeAdd.setFclid(priceFclFeeadd.getFclid());
			fclFeeAdd.setFeeitemid(priceFclFeeadd.getFeeitemid());
			fclFeeAdd.setFeeitemcode(priceFclFeeadd.getFeeitemcode());
			try {
				DatFeeitem datFeeitem = serviceContext.feeItemMgrService.datFeeitemDao.findById(priceFclFeeadd.getFeeitemid());
				fclFeeAdd.setFeeitemname(this.language.equals("en") ? (datFeeitem.getNamee()):(priceFclFeeadd.getFeeitemname()));
			} catch (Exception e) {
				fclFeeAdd.setFeeitemname(priceFclFeeadd.getFeeitemname());
			}
			fclFeeAdd.setAmtother(priceFclFeeadd.getAmtother());
			fclFeeAdd.setCntypeid(priceFclFeeadd.getCntypeid());
			DatCntype datCntype = serviceContext.cntypeMgrService.datcntypeDao.findById(priceFclFeeadd.getCntypeid()!=null?priceFclFeeadd.getCntypeid():-1);
			fclFeeAdd.setCntypecode(datCntype!=null?datCntype.getCode():"");
			fclFeeAdd.setTemplatename("");
			fclFeeAdd.setIstemplate(false);
			//fclFeeAdd.setId(null);
			fclFeeAdd.setInputer(priceFclFeeadd.getInputer());
			fclFeeAdd.setPpcc(priceFclFeeadd.getPpcc());
			fclFeeAdd.setUnit(priceFclFeeadd.getUnit());
			
			voList.add(fclFeeAdd);
		}
		Gson gson = new Gson();
		JsonArray jsonArray = new JsonArray();
		for (FclFeeAdd vo : voList) {
			String str = gson.toJson(vo);
			JsonParser parser = new JsonParser();
			JsonElement el = parser.parse(str);
			jsonArray.add(el);
		}
		////AppUtils.debug("template json data:"+jsonArray);
		jsonArrayText = jsonArray.toString();
		
		update.markUpdate(true, UpdateLevel.Data, "jsonArrayText");
		Browser.execClientScript("removeTableRow();initData();");
    }
    
    
    @Action
    private void delTemplate() {
    	 if(StrUtils.isNull(templateComBo))return;
         List<PriceFclFeeadd> list = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao.findAllByClauseWhere("templatename='"+templateComBo + "' AND isdelete = false AND istemplate = TRUE ORDER BY id");
         for (PriceFclFeeadd priceFclFeeadd : list) {
        	 serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao.remove(priceFclFeeadd);
		 }
         this.alert("OK");
         templateComBo = "";
         update.markUpdate(true, UpdateLevel.Data, "templateComBo");
    }
    
    
    @Action
    private void saveAsTemplate() {
    	Browser.execClientScript("var text = window.prompt('请输入模板名称！');if(text != null){templateNameText.setValue(text);saveAsTemplate();}");
    }
    
    
    @Action
    private void saveAsTemplateSubmit() {
    	String data = AppUtils.getReqParam("data");
    	String templatename = AppUtils.getReqParam("templatename");
    	//AppUtils.debug("json submit:"+data);
    	if(!StrUtils.isNull(data)){
    		Type listType = new TypeToken<ArrayList<FclFeeAdd>>(){}.getType();//TypeToken内的泛型就是Json数据中的类型
        	Gson gson = new Gson();
        	//String json = "{\"id\":123123,\"feeitemcode\":gggg\"\"}";
        	ArrayList<FclFeeAdd> list = gson.fromJson(data, listType);//使用该class属性，获取的对象均是list类型的
        	
        	List<PriceFclFeeadd> moduleList = new ArrayList<PriceFclFeeadd>();
            for (FclFeeAdd fclFeeadd : list) {
            	PriceFclFeeadd priceFclFeeadd = new PriceFclFeeadd();
            	
            	priceFclFeeadd.setIstemplate(true);
            	priceFclFeeadd.setTemplatename(templatename);
            	priceFclFeeadd.setAmt(fclFeeadd.getAmt());
            	priceFclFeeadd.setAmt20(fclFeeadd.getAmt20());
            	priceFclFeeadd.setAmt40gp(fclFeeadd.getAmt40gp());
            	priceFclFeeadd.setAmt40hq(fclFeeadd.getAmt40hq());
            	priceFclFeeadd.setAmt45hq(fclFeeadd.getAmt45hq());
            	priceFclFeeadd.setCurrency(fclFeeadd.getCurrency());
            	priceFclFeeadd.setFclid(null);
            	priceFclFeeadd.setFeeitemid(fclFeeadd.getFeeitemid());
            	priceFclFeeadd.setFeeitemcode(fclFeeadd.getFeeitemcode());
            	try {//英文状态下前台传过来的是英文，由于之前都是保存中文名，所以这里查一下中文
    				DatFeeitem datFeeitem = serviceContext.feeItemMgrService.datFeeitemDao
    						.findById(priceFclFeeadd.getFeeitemid());
    				priceFclFeeadd.setFeeitemname(datFeeitem.getName());
    			} catch (Exception e) {
    				priceFclFeeadd.setFeeitemname(priceFclFeeadd.getFeeitemname());
    			}
    			priceFclFeeadd.setPpcc(fclFeeadd.getPpcc());
    			priceFclFeeadd.setUnit(fclFeeadd.getUnit());
    			priceFclFeeadd.setAmtother(fclFeeadd.getAmtother());
    			priceFclFeeadd.setCntypeid(fclFeeadd.getCntypeid());
    			priceFclFeeadd.setCorpid(AppUtils.getUserSession().getCorpid());
    			if(StrUtils.isNull(fclFeeadd.getFeeitemcode()) || StrUtils.isNull(fclFeeadd.getFeeitemname())){
    			}else{
    				moduleList.add(priceFclFeeadd);
    			}
			}
            serviceContext.pricefclfeeaddMgrService.saveOrModify(moduleList);
            this.alert("OK");
            update.markUpdate(true, UpdateLevel.Data, "templateComBo");
    	}
    }

	@Action
    private void saveAjaxSubmit() {
		idsNewArray.clear();
		String data = AppUtils.getReqParam("data");
		String template = AppUtils.getReqParam("template");
		//AppUtils.debug("json submit:"+data);
		if(!StrUtils.isNull(data)){
			List<PriceFclFeeadd> removeList = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao.findAllByClauseWhere("templatename='"+templateComBo + "' AND isdelete = false AND istemplate = TRUE ORDER BY id");
			for (PriceFclFeeadd remove : removeList) {//先删除之前的附加费
				serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao.remove(remove);
			}
			if(!StrUtils.isNull(template)&&!template.equals(templateComBo)){//说明修改了模板名字
				templateComBo = template;
			}
    		Type listType = new TypeToken<ArrayList<FclFeeAdd>>(){}.getType();//TypeToken内的泛型就是Json数据中的类型
        	Gson gson = new Gson();
        	ArrayList<FclFeeAdd> list = gson.fromJson(data, listType);//使用该class属性，获取的对象均是list类型的
        	
        	List<PriceFclFeeadd> moduleList = new ArrayList<PriceFclFeeadd>();
            for (FclFeeAdd fclFeeadd : list) {
            	PriceFclFeeadd priceFclFeeadd = new PriceFclFeeadd();
            	priceFclFeeadd.setIstemplate(true);
            	priceFclFeeadd.setTemplatename(templateComBo);
            	priceFclFeeadd.setAmt(fclFeeadd.getAmt());
            	priceFclFeeadd.setAmt20(fclFeeadd.getAmt20());
            	priceFclFeeadd.setAmt40gp(fclFeeadd.getAmt40gp());
            	priceFclFeeadd.setAmt40hq(fclFeeadd.getAmt40hq());
            	priceFclFeeadd.setAmt45hq(fclFeeadd.getAmt45hq());
            	priceFclFeeadd.setCurrency(fclFeeadd.getCurrency());
            	priceFclFeeadd.setFclid(null);
            	priceFclFeeadd.setFeeitemid(fclFeeadd.getFeeitemid());
            	priceFclFeeadd.setFeeitemcode(fclFeeadd.getFeeitemcode());
            	try {//英文状态下前台传过来的是英文，由于之前都是保存中文名，所以这里查一下中文
    				DatFeeitem datFeeitem = serviceContext.feeItemMgrService.datFeeitemDao
    						.findById(priceFclFeeadd.getFeeitemid());
    				priceFclFeeadd.setFeeitemname(datFeeitem.getName());
    			} catch (Exception e) {
    				priceFclFeeadd.setFeeitemname(priceFclFeeadd.getFeeitemname());
    			}
    			priceFclFeeadd.setPpcc(fclFeeadd.getPpcc());
    			priceFclFeeadd.setUnit(fclFeeadd.getUnit());
    			priceFclFeeadd.setAmtother(fclFeeadd.getAmtother());
    			priceFclFeeadd.setCntypeid(fclFeeadd.getCntypeid());
    			priceFclFeeadd.setIstemplate(true);
    			priceFclFeeadd.setCorpid(AppUtils.getUserSession().getCorpid());
    			if(StrUtils.isNull(fclFeeadd.getFeeitemcode()) || StrUtils.isNull(fclFeeadd.getFeeitemname())){
    			}else{
    				moduleList.add(priceFclFeeadd);
    			}
			}
            serviceContext.pricefclfeeaddMgrService.saveOrModify(moduleList);
            this.alert("OK");
            update.markUpdate(true, UpdateLevel.Data, "templateComBo");
    	}
	}
	
	/**
     * 获取两个List的不同元素
     * @param list1
     * @param list2
     * @return
     */
    private List<Long> getDiffrent(List<Long> list1, List<Long> list2) {
        long st = System.nanoTime();
        List<Long> diff = new ArrayList<Long>();
        for(Long str:list1){
            if(!list2.contains(str)){
                diff.add(str);
            }
        }
        ////System.out.println("getDiffrent total times "+(System.nanoTime()-st));
        return diff;
    }
	
}
