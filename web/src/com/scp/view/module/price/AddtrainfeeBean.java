package com.scp.view.module.price;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.scp.model.price.PriceFclFeeadd;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
import com.scp.vo.price.FclFeeAdd;

@ManagedBean(name = "pages.module.price.addtrainfeeBean", scope = ManagedBeanScope.REQUEST)
public class AddtrainfeeBean extends GridView {
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
	public Long fclid = -100l;

	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String id = AppUtils.getReqParam("id");
			if (!StrUtils.isNull(id) && Long.parseLong(id) > 0l) {
				this.fclid = Long.parseLong(id);
			}
			;
			String ids = AppUtils.getReqParam("batchRefIds");
			if (!StrUtils.isNull(ids)) {
				batchRefIds = ids;
				update.markUpdate(true, UpdateLevel.Data, "batchRefIds");
			} else {
				Browser.execClientScript("buttonhide()");
			}
			initData();
		}
	}

	@Action
	public void refresh() {
		Browser.execClientScript("removeTableRow();");
		templateComBo = "";
		update.markUpdate(true, UpdateLevel.Data, "templateComBo");
		initData();
		update.markUpdate(true, UpdateLevel.Data, "jsonArrayText");
		Browser.execClientScript("initData();");
	}

	private void initData() {
		idsOldArray.clear();
		List<PriceFclFeeadd> list = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao.findAllByClauseWhere("fclid=" + fclid+ " AND isdelete = false ORDER BY id");

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
			fclFeeAdd.setId(priceFclFeeadd.getId());
			fclFeeAdd.setInputer(priceFclFeeadd.getInputer());
			fclFeeAdd.setPpcc(priceFclFeeadd.getPpcc());
			fclFeeAdd.setUnit(priceFclFeeadd.getUnit());
			fclFeeAdd.setAmtother(priceFclFeeadd.getAmtother());
			fclFeeAdd.setCntypeid(priceFclFeeadd.getCntypeid());
			DatCntype datCntype = serviceContext.cntypeMgrService.datcntypeDao.findById(priceFclFeeadd.getCntypeid() != null ? priceFclFeeadd.getCntypeid(): -1);
			fclFeeAdd.setCntypecode(datCntype != null ? datCntype.getCode()
					: "");

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
		// //AppUtils.debug(jsonArray);
		jsonArrayText = jsonArray.toString();
	}

	@Bind(id = "template")
	public List<SelectItem> getTemplate() {
		try {
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT string_agg(corpid||'' , ',') AS corpid FROM sys_user_corplink  WHERE  ischoose = TRUE AND userid ="
							+ AppUtils.getUserSession().getUserid());
			String corpid = m.get("corpid").toString();
			return CommonComBoxBean
					.getComboxItems(
							"DISTINCT templatename",
							"templatename",
							"price_fcl_feeadd AS d",
							"WHERE istemplate=true AND isdelete = false AND templatename is not null AND corpid in ("+ corpid + ")", "ORDER BY templatename");
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
		// //System.out.println(templateComBo);
		if (StrUtils.isNull(templateComBo))
			return;
		List<PriceFclFeeadd> list = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao.findAllByClauseWhere("templatename='"
						+ templateComBo
						+ "' AND isdelete = false AND istemplate = TRUE ORDER BY id");

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
			fclFeeAdd.setAmtother(priceFclFeeadd.getAmtother());
			fclFeeAdd.setCntypeid(priceFclFeeadd.getCntypeid());
			DatCntype datCntype = serviceContext.cntypeMgrService.datcntypeDao.findById(priceFclFeeadd.getCntypeid() != null ? priceFclFeeadd.getCntypeid(): -1);
			fclFeeAdd.setCntypecode(datCntype != null ? datCntype.getCode()
					: "");
			fclFeeAdd.setTemplatename("");
			fclFeeAdd.setIstemplate(false);
			// fclFeeAdd.setId(null);
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
		// //AppUtils.debug("template json data:"+jsonArray);
		jsonArrayText = jsonArray.toString();

		update.markUpdate(true, UpdateLevel.Data, "jsonArrayText");
		Browser.execClientScript("initData();");
	}

	@Action
	private void delTemplate() {
		if (StrUtils.isNull(templateComBo))
			return;
		List<PriceFclFeeadd> list = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao
				.findAllByClauseWhere("templatename='"
						+ templateComBo
						+ "' AND isdelete = false AND istemplate = TRUE ORDER BY id");
		for (PriceFclFeeadd priceFclFeeadd : list) {
			serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao
					.remove(priceFclFeeadd);
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
		// AppUtils.debug("json submit:"+data);
		if (!StrUtils.isNull(data)) {
			Type listType = new TypeToken<ArrayList<FclFeeAdd>>() {
			}.getType();// TypeToken内的泛型就是Json数据中的类型
			Gson gson = new Gson();
			// String json = "{\"id\":123123,\"feeitemcode\":gggg\"\"}";
			ArrayList<FclFeeAdd> list = gson.fromJson(data, listType);// 使用该class属性，获取的对象均是list类型的

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
				priceFclFeeadd.setFeeitemname(fclFeeadd.getFeeitemname());
				priceFclFeeadd.setPpcc(fclFeeadd.getPpcc());
				priceFclFeeadd.setUnit(fclFeeadd.getUnit());
				priceFclFeeadd.setAmtother(fclFeeadd.getAmtother());
				priceFclFeeadd.setCntypeid(fclFeeadd.getCntypeid());
				priceFclFeeadd.setCorpid(AppUtils.getUserSession().getCorpid());
				if (StrUtils.isNull(fclFeeadd.getFeeitemcode())
						|| StrUtils.isNull(fclFeeadd.getFeeitemname())) {
				} else {
					moduleList.add(priceFclFeeadd);
				}
			}
			serviceContext.pricefclfeeaddMgrService.saveOrModify(moduleList);
			this.alert("OK");
			update.markUpdate(true, UpdateLevel.Data, "templateComBo");
		}
	}

	/**
	 * 1743 批量修改附加费调整
	 */
	@Action
	private void batchupdateSubmit() {
		String data = AppUtils.getReqParam("data");
		if (!StrUtils.isNull(data)) {
			Type listType = new TypeToken<ArrayList<FclFeeAdd>>() {
			}.getType();// TypeToken内的泛型就是Json数据中的类型
			Gson gson = new Gson();
			ArrayList<FclFeeAdd> list = gson.fromJson(data, listType);// 使用该class属性，获取的对象均是list类型的
			FclFeeAdd fclFeeadd = list.get(0);
			String[] ids = batchRefIds.split(",");
			for (String id : ids) {
				List<PriceFclFeeadd> removeList = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao
						.findAllByClauseWhere("fclid=" + id
								+ " AND isdelete = false AND feeitemid="
								+ fclFeeadd.getFeeitemid());
				for (PriceFclFeeadd remove : removeList) {
					PriceFclFeeadd priceFclFeeadd = remove;
					priceFclFeeadd.setAmt(fclFeeadd.getAmt());
					priceFclFeeadd.setAmt20(fclFeeadd.getAmt20());
					priceFclFeeadd.setAmt40gp(fclFeeadd.getAmt40gp());
					priceFclFeeadd.setAmt40hq(fclFeeadd.getAmt40hq());
					priceFclFeeadd.setAmt45hq(fclFeeadd.getAmt45hq());
					priceFclFeeadd.setAmtother(fclFeeadd.getAmtother());
					priceFclFeeadd.setCurrency(fclFeeadd.getCurrency());
					priceFclFeeadd.setFclid(fclFeeadd.getFclid());
					priceFclFeeadd.setFeeitemid(fclFeeadd.getFeeitemid());
					priceFclFeeadd.setFeeitemcode(fclFeeadd.getFeeitemcode());
					priceFclFeeadd.setFeeitemname(fclFeeadd.getFeeitemname());
					priceFclFeeadd.setPpcc(fclFeeadd.getPpcc());
					priceFclFeeadd.setUnit(fclFeeadd.getUnit());
					priceFclFeeadd.setFclid(Long.valueOf(id));
					priceFclFeeadd.setInputer(AppUtils.getUserSession()
							.getUsercode());
					priceFclFeeadd.setInputtime(Calendar.getInstance()
							.getTime());
					priceFclFeeadd.setCntypeid(fclFeeadd.getCntypeid());
					serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao
							.modify(remove);
				}
			}
			this.alert("OK");
			update.markUpdate(true, UpdateLevel.Data, "templateComBo");
		}
	}

	/**
	 * 移除匹配费用 如果已经存在则删除这条附加费
	 */
	@Action
	private void deleteSubmiteubmit() {
		String data = AppUtils.getReqParam("data");
		if (!StrUtils.isNull(data)) {
			Type listType = new TypeToken<ArrayList<FclFeeAdd>>() {
			}.getType();// TypeToken内的泛型就是Json数据中的类型
			Gson gson = new Gson();
			ArrayList<FclFeeAdd> list = gson.fromJson(data, listType);// 使用该class属性，获取的对象均是list类型的
			// FclFeeAdd fclFeeadd = list.get(0);
			String[] ids = batchRefIds.split(",");
			for (FclFeeAdd fclFeeadd : list) {
				for (String id : ids) {
					List<PriceFclFeeadd> removeList = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao
							.findAllByClauseWhere("fclid=" + id
									+ " AND isdelete = false AND feeitemid="
									+ fclFeeadd.getFeeitemid());
					for (PriceFclFeeadd remove : removeList) {
						serviceContext.pricefclfeeaddMgrService.removes(remove
								.getId());
					}
				}
			}
			this.alert("OK");
			update.markUpdate(true, UpdateLevel.Data, "templateComBo");
		}
	}

	/**
	 * 追加或覆盖 如果已经存在，更新记录，如果不存在，新增一条附加费
	 */
	@Action
	private void addupdateSubmiteSubmit() {
		String data = AppUtils.getReqParam("data");
		if (!StrUtils.isNull(data)) {
			Type listType = new TypeToken<ArrayList<FclFeeAdd>>() {
			}.getType();// TypeToken内的泛型就是Json数据中的类型
			Gson gson = new Gson();
			// String json = "{\"id\":123123,\"feeitemcode\":gggg\"\"}";
			ArrayList<FclFeeAdd> list = gson.fromJson(data, listType);// 使用该class属性，获取的对象均是list类型的
			List<PriceFclFeeadd> moduleList = new ArrayList<PriceFclFeeadd>();
			// 追加或覆盖
			if (!StrUtils.isNull(batchRefIds)) {
				String[] ids = batchRefIds.split(",");
				for (String id : ids) {
					for (FclFeeAdd fclFeeadd : list) {
						List<PriceFclFeeadd> addfees = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao
								.findAllByClauseWhere("fclid="
										+ id
										+ " AND isdelete = false AND feeitemid="
										+ fclFeeadd.getFeeitemid());// 找到此运价包含此费用名称的附加费
						if (addfees.size() > 0) {// 找到就把此运价的所有这个名称的附加费更新
							for (PriceFclFeeadd addfee : addfees) {
								addfee.setAmt(fclFeeadd.getAmt());
								addfee.setAmt20(fclFeeadd.getAmt20());
								addfee.setAmt40gp(fclFeeadd.getAmt40gp());
								addfee.setAmt40hq(fclFeeadd.getAmt40hq());
								addfee.setAmt45hq(fclFeeadd.getAmt45hq());
								addfee.setAmtother(fclFeeadd.getAmtother());
								addfee.setCurrency(fclFeeadd.getCurrency());
								addfee.setFclid(fclFeeadd.getFclid());
								addfee.setFeeitemid(fclFeeadd.getFeeitemid());
								addfee.setFeeitemcode(fclFeeadd
										.getFeeitemcode());
								addfee.setFeeitemname(fclFeeadd
										.getFeeitemname());
								addfee.setPpcc(fclFeeadd.getPpcc());
								addfee.setUnit(fclFeeadd.getUnit());
								addfee.setFclid(Long.valueOf(id));
								addfee.setInputer(AppUtils.getUserSession()
										.getUsercode());
								addfee.setInputtime(Calendar.getInstance()
										.getTime());
								addfee.setCntypeid(fclFeeadd.getCntypeid());
								if (StrUtils.isNull(fclFeeadd.getFeeitemcode())
										|| StrUtils.isNull(fclFeeadd
												.getFeeitemname())) {
								} else {
									moduleList.add(addfee);
								}
							}
						} else {// 没有此费用就新增一条
							PriceFclFeeadd priceFclFeeadd = new PriceFclFeeadd();
							priceFclFeeadd.setAmt(fclFeeadd.getAmt());
							priceFclFeeadd.setAmt20(fclFeeadd.getAmt20());
							priceFclFeeadd.setAmt40gp(fclFeeadd.getAmt40gp());
							priceFclFeeadd.setAmt40hq(fclFeeadd.getAmt40hq());
							priceFclFeeadd.setAmt45hq(fclFeeadd.getAmt45hq());
							priceFclFeeadd.setAmtother(fclFeeadd.getAmtother());
							priceFclFeeadd.setCurrency(fclFeeadd.getCurrency());
							priceFclFeeadd.setFclid(fclFeeadd.getFclid());
							priceFclFeeadd.setFeeitemid(fclFeeadd
									.getFeeitemid());
							priceFclFeeadd.setFeeitemcode(fclFeeadd
									.getFeeitemcode());
							priceFclFeeadd.setFeeitemname(fclFeeadd
									.getFeeitemname());
							priceFclFeeadd.setPpcc(fclFeeadd.getPpcc());
							priceFclFeeadd.setUnit(fclFeeadd.getUnit());
							priceFclFeeadd.setFclid(Long.valueOf(id));
							priceFclFeeadd.setInputer(AppUtils.getUserSession()
									.getUsercode());
							priceFclFeeadd.setInputtime(Calendar.getInstance()
									.getTime());
							priceFclFeeadd.setCntypeid(fclFeeadd.getCntypeid());
							priceFclFeeadd.setCorpid(AppUtils.getUserSession()
									.getCorpid());
							if (StrUtils.isNull(fclFeeadd.getFeeitemcode())
									|| StrUtils.isNull(fclFeeadd
											.getFeeitemname())) {
							} else {
								moduleList.add(priceFclFeeadd);
							}
						}
					}
				}
				serviceContext.pricefclfeeaddMgrService
						.saveOrModify(moduleList);
			}
			this.alert("OK");
			update.markUpdate(true, UpdateLevel.Data, "templateComBo");
		}
	}

	@Action
	private void saveAjaxSubmit() {
		idsNewArray.clear();
		String data = AppUtils.getReqParam("data");
		// AppUtils.debug("json submit:"+data);
		if (!StrUtils.isNull(data)) {
			Type listType = new TypeToken<ArrayList<FclFeeAdd>>() {
			}.getType();// TypeToken内的泛型就是Json数据中的类型
			Gson gson = new Gson();
			// String json = "{\"id\":123123,\"feeitemcode\":gggg\"\"}";
			ArrayList<FclFeeAdd> list = gson.fromJson(data, listType);// 使用该class属性，获取的对象均是list类型的

			List<PriceFclFeeadd> moduleList = new ArrayList<PriceFclFeeadd>();

			// 批量修改新建
			if (!StrUtils.isNull(batchRefIds)) {
				String[] ids = batchRefIds.split(",");
				for (String id : ids) {
					for (FclFeeAdd fclFeeadd : list) {

						PriceFclFeeadd priceFclFeeadd = new PriceFclFeeadd();

						priceFclFeeadd.setAmt(fclFeeadd.getAmt());
						priceFclFeeadd.setAmt20(fclFeeadd.getAmt20());
						priceFclFeeadd.setAmt40gp(fclFeeadd.getAmt40gp());
						priceFclFeeadd.setAmt40hq(fclFeeadd.getAmt40hq());
						priceFclFeeadd.setAmt45hq(fclFeeadd.getAmt45hq());
						priceFclFeeadd.setAmtother(fclFeeadd.getAmtother());
						priceFclFeeadd.setCurrency(fclFeeadd.getCurrency());
						priceFclFeeadd.setFclid(fclFeeadd.getFclid());
						priceFclFeeadd.setFeeitemid(fclFeeadd.getFeeitemid());
						priceFclFeeadd.setFeeitemcode(fclFeeadd
								.getFeeitemcode());
						priceFclFeeadd.setFeeitemname(fclFeeadd
								.getFeeitemname());
						priceFclFeeadd.setPpcc(fclFeeadd.getPpcc());
						priceFclFeeadd.setUnit(fclFeeadd.getUnit());
						priceFclFeeadd.setFclid(Long.valueOf(id));
						priceFclFeeadd.setInputer(AppUtils.getUserSession()
								.getUsercode());
						priceFclFeeadd.setInputtime(Calendar.getInstance()
								.getTime());
						priceFclFeeadd.setCntypeid(fclFeeadd.getCntypeid());
						priceFclFeeadd.setCorpid(AppUtils.getUserSession()
								.getCorpid());
						if (StrUtils.isNull(fclFeeadd.getFeeitemcode())
								|| StrUtils.isNull(fclFeeadd.getFeeitemname())) {
						} else {
							moduleList.add(priceFclFeeadd);
						}
					}
					List<PriceFclFeeadd> removeList = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao
							.findAllByClauseWhere("fclid=" + id
									+ " AND isdelete = false");
					for (PriceFclFeeadd remove : removeList) {
						serviceContext.pricefclfeeaddMgrService.removes(remove
								.getId());
					}
				}
				serviceContext.pricefclfeeaddMgrService
						.saveOrModify(moduleList);
			} else {
				for (FclFeeAdd fclFeeadd : list) {
					// AppUtils.debug("priceFclFeeadd:  = " +
					// fclFeeadd.getCurrency());//从list中取得内容即可

					PriceFclFeeadd priceFclFeeadd = new PriceFclFeeadd();
					if (fclFeeadd.getId() > 0) {
						priceFclFeeadd = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao
								.findById(fclFeeadd.getId());
						idsNewArray.add(fclFeeadd.getId());
					}
					priceFclFeeadd.setAmt(fclFeeadd.getAmt());
					priceFclFeeadd.setAmt20(fclFeeadd.getAmt20());
					priceFclFeeadd.setAmt40gp(fclFeeadd.getAmt40gp());
					priceFclFeeadd.setAmt40hq(fclFeeadd.getAmt40hq());
					priceFclFeeadd.setAmt45hq(fclFeeadd.getAmt45hq());
					priceFclFeeadd.setAmtother(fclFeeadd.getAmtother());
					priceFclFeeadd.setCurrency(fclFeeadd.getCurrency());
					// priceFclFeeadd.setFclid(fclFeeadd.getFclid());
					if (fclFeeadd.getFeeitemid() != null)
						priceFclFeeadd.setFeeitemid(fclFeeadd.getFeeitemid());
					priceFclFeeadd.setFeeitemcode(fclFeeadd.getFeeitemcode());
					priceFclFeeadd.setFeeitemname(fclFeeadd.getFeeitemname());
					priceFclFeeadd.setPpcc(fclFeeadd.getPpcc());
					priceFclFeeadd.setUnit(fclFeeadd.getUnit());
					if (Long.valueOf(fclid) > 0) {
						priceFclFeeadd.setFclid(Long.valueOf(fclid));
					} else {
						priceFclFeeadd.setFclid(null);
					}
					if (fclFeeadd.getCntypeid() != null)
						priceFclFeeadd.setCntypeid(fclFeeadd.getCntypeid());
					priceFclFeeadd.setInputer(AppUtils.getUserSession()
							.getUsercode());
					priceFclFeeadd.setInputtime(Calendar.getInstance()
							.getTime());
					priceFclFeeadd.setCorpid(AppUtils.getUserSession()
							.getCorpid());
					if (StrUtils.isNull(fclFeeadd.getFeeitemcode())
							|| StrUtils.isNull(fclFeeadd.getFeeitemname())) {
					} else {
						moduleList.add(priceFclFeeadd);
					}
				}
				serviceContext.pricefclfeeaddMgrService
						.saveOrModify(moduleList);

				List<Long> lists = getDiffrent(idsOldArray, idsNewArray);
				if (!lists.isEmpty()) {
					serviceContext.pricefclfeeaddMgrService.removes(lists);
				}
			}

			this.alert("OK");
		}
	}

	/**
	 * 获取两个List的不同元素
	 * 
	 * @param list1
	 * @param list2
	 * @return
	 */
	private List<Long> getDiffrent(List<Long> list1, List<Long> list2) {
		long st = System.nanoTime();
		List<Long> diff = new ArrayList<Long>();
		for (Long str : list1) {
			if (!list2.contains(str)) {
				diff.add(str);
			}
		}
		// //System.out.println("getDiffrent total times "+(System.nanoTime()-st));
		return diff;
	}
}
