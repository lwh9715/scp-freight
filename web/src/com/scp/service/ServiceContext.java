package com.scp.service;

import javax.annotation.Resource;

import com.scp.service.api.*;
import com.scp.service.customer.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.DaoIbatisTemplateMySql;
import com.scp.dao.cache.CommonDBCache;
import com.scp.service.bpm.BpmAssignRefService;
import com.scp.service.bpm.BpmAssignService;
import com.scp.service.bpm.BpmCommentsTempService;
import com.scp.service.bpm.BpmProcessService;
import com.scp.service.bpm.BpmProcessinsVarService;
import com.scp.service.bpm.BpmProcessinstanceService;
import com.scp.service.bpm.BpmTaskService;
import com.scp.service.bpm.BpmTraceService;
import com.scp.service.bpm.BpmWorkItemService;
import com.scp.service.bus.AirBookcodeMgrService;
import com.scp.service.bus.AirPlaneTypeService;
import com.scp.service.bus.BusAirBillMgrService;
import com.scp.service.bus.BusAirExtfeeService;
import com.scp.service.bus.BusAirFlightMgrService;
import com.scp.service.bus.BusAirMgrService;
import com.scp.service.bus.BusBillnoMgrService;
import com.scp.service.bus.BusBookingrptService;
import com.scp.service.bus.BusCustomsMgrService;
import com.scp.service.bus.BusDocdefMgrService;
import com.scp.service.bus.BusDocexpLinkMgrService;
import com.scp.service.bus.BusDocexpMgrService;
import com.scp.service.bus.BusGoodsMgrService;
import com.scp.service.bus.BusInsuranceService;
import com.scp.service.bus.BusTrainBillMgrService;
import com.scp.service.bus.BusTrainMgrService;
import com.scp.service.bus.BusTruckMgrService;
import com.scp.service.bus.ShipScheduleService;
import com.scp.service.carmgr.CarMgrService;
import com.scp.service.carmgr.CarOilRecordMgrService;
import com.scp.service.carmgr.CarRepaireRecordMgrService;
import com.scp.service.carmgr.CarTypeMgrService;
import com.scp.service.carmgr.DriverMgrService;
import com.scp.service.commerce.BusCommerceService;
import com.scp.service.common.AttachmentService;
import com.scp.service.cs.CsBookingMgrService;
import com.scp.service.data.AccountMgrService;
import com.scp.service.data.BankMgrService;
import com.scp.service.data.CntypeMgrService;
import com.scp.service.data.CountryMgrService;
import com.scp.service.data.CurrencyMgrService;
import com.scp.service.data.Door2doordayMgrService;
import com.scp.service.data.EdiesiDtlCntService;
import com.scp.service.data.EdiesiService;
import com.scp.service.data.EdiinttrareportMgrService;
import com.scp.service.data.ExchangeRateMgrService;
import com.scp.service.data.FeeItemMgrService;
import com.scp.service.data.FiledataMgrService;
import com.scp.service.data.GoodsMgrService;
import com.scp.service.data.GoodstypeMgrService;
import com.scp.service.data.LineMgrService;
import com.scp.service.data.LinecodeMgrService;
import com.scp.service.data.PackageMgrService;
import com.scp.service.data.PortyMgrService;
import com.scp.service.data.UnitMgrService;
import com.scp.service.data.WarehouseMgrService;
import com.scp.service.data.WarehouseareaMgrService;
import com.scp.service.data.WarehouselocalMgrService;
import com.scp.service.del.DelLoadMgrService;
import com.scp.service.del.DelLoaddtlMgrService;
import com.scp.service.del.DeliveryMgrService;
import com.scp.service.del.DeliverydtlMgrService;
import com.scp.service.elogistics.data.ChannelService;
import com.scp.service.elogistics.data.ElogisticsService;
import com.scp.service.finance.ActPayRecService;
import com.scp.service.finance.AgenBillMgrService;
import com.scp.service.finance.AgenBilldtlMgrService;
import com.scp.service.finance.ArapMgrService;
import com.scp.service.finance.BillMgrService;
import com.scp.service.finance.CostMgrService;
import com.scp.service.finance.FeeTemplateMgrService;
import com.scp.service.finance.FinaBillCntdtlMgrService;
import com.scp.service.finance.FinaConfigarapService;
import com.scp.service.finance.FinaPrepaidMgrService;
import com.scp.service.finance.InvoiceMgrService;
import com.scp.service.finance.JobsMgrService;
import com.scp.service.finance.RpReqMgrService;
import com.scp.service.finance.StatementMgrService;
import com.scp.service.finance.cash.FsBankMgrService;
import com.scp.service.finance.cash.FsCashMgrService;
import com.scp.service.finance.doc.VchMgrService;
import com.scp.service.finance.doc.VchdtlMgrService;
import com.scp.service.finance.initcfg.AccountSetMgrService;
import com.scp.service.finance.initcfg.AcctRpMgrService;
import com.scp.service.finance.initcfg.ActperiodMgrService;
import com.scp.service.finance.initcfg.ActrateMgrService;
import com.scp.service.finance.initcfg.ArapSubjectMgrService;
import com.scp.service.finance.initcfg.AstMgrService;
import com.scp.service.finance.initcfg.FsAstMgrService;
import com.scp.service.finance.initcfg.FsAstrefMgrService;
import com.scp.service.finance.initcfg.FsConfigRpMgrService;
import com.scp.service.finance.initcfg.FsConfigShareMgrService;
import com.scp.service.finance.initcfg.PeriodXrateService;
import com.scp.service.finance.initcfg.SubjectMgrService;
import com.scp.service.finance.initcfg.VchdescMgrService;
import com.scp.service.finance.initcfg.VchtypeMgrService;
import com.scp.service.gps.BusGpsRefService;
import com.scp.service.gps.BusGpsService;
import com.scp.service.knowledge.SysKnowledgelibService;
import com.scp.service.oa.AppTravelService;
import com.scp.service.oa.BaseInfoService;
import com.scp.service.oa.JobChangeService;
import com.scp.service.oa.OaDatFiledataService;
import com.scp.service.oa.OaFeeService;
import com.scp.service.oa.OaLeaveapplicationService;
import com.scp.service.oa.OaTimeSheetService;
import com.scp.service.oa.OaUserContractService;
import com.scp.service.oa.OaUserInfoService;
import com.scp.service.oa.OaUserProtocolService;
import com.scp.service.oa.OaUserTransferService;
import com.scp.service.oa.OaUserVisaService;
import com.scp.service.oa.SalaryBillMgrService;
import com.scp.service.oa.SalaryDoubleService;
import com.scp.service.oa.SalaryTableService;
import com.scp.service.oa.SalaryWelfareService;
import com.scp.service.oa.UserChangeService;
import com.scp.service.oa.UserEvService;
import com.scp.service.oa.UserLogService;
import com.scp.service.oa.WorkReportService;
import com.scp.service.order.BusOrderMgrService;
import com.scp.service.price.BusPriceDtlService;
import com.scp.service.price.BusPriceService;
import com.scp.service.price.PriceAirService;
import com.scp.service.price.PriceFclBargeDtlService;
import com.scp.service.price.PriceFclBargeService;
import com.scp.service.price.PriceFclUseruleService;
import com.scp.service.price.PriceGroupService;
import com.scp.service.price.PriceHomepageMgrService;
import com.scp.service.price.PriceTrainService;
import com.scp.service.price.PriceTruckService;
import com.scp.service.price.PricefclMgrService;
import com.scp.service.price.PricefclfeeaddMgrService;
import com.scp.service.price.PricefclpubruleMgrService;
import com.scp.service.salesmgr.BlackListService;
import com.scp.service.ship.BusBookingMgrService;
import com.scp.service.ship.BusContainerMgrService;
import com.scp.service.ship.BusCreightChargeMgrService;
import com.scp.service.ship.BusCustomsTaxretMgrService;
import com.scp.service.ship.BusGoodstrackMgrService;
import com.scp.service.ship.BusGoodstrackTempMgrService;
import com.scp.service.ship.BusShipAddresMgrService;
import com.scp.service.ship.BusShipBillMgrService;
import com.scp.service.ship.BusShipBillregMgrService;
import com.scp.service.ship.BusShipContainerMgrService;
import com.scp.service.ship.BusShipCostMgrService;
import com.scp.service.ship.BusShipCostdtlMgrService;
import com.scp.service.ship.BusShipHoldMgrService;
import com.scp.service.ship.BusShipjoinMgrService;
import com.scp.service.ship.BusShipjoingoodsMgrService;
import com.scp.service.ship.BusShipjoinlinkMgrService;
import com.scp.service.ship.BusShippingMgrService;
import com.scp.service.ship.EdiMapMgrService;
import com.scp.service.ship.EdiTransMgrService;
import com.scp.service.ship.MgrTrainBookingService;
import com.scp.service.ship.VoyageService;
import com.scp.service.sysmgr.AddressListMgrMgrService;
import com.scp.service.sysmgr.BusNoRuleMgrService;
import com.scp.service.sysmgr.CorpvisitService;
import com.scp.service.sysmgr.FavoritesService;
import com.scp.service.sysmgr.PriceFclAskService;
import com.scp.service.sysmgr.RegisterService;
import com.scp.service.sysmgr.RoleMgrService;
import com.scp.service.sysmgr.SysAttachmentService;
import com.scp.service.sysmgr.SysCorpinvMgrService;
import com.scp.service.sysmgr.SysCorporationAgentnService;
import com.scp.service.sysmgr.SysCorporationApplyMonthService;
import com.scp.service.sysmgr.SysCorporationService;
import com.scp.service.sysmgr.SysCustLibMgrService;
import com.scp.service.sysmgr.SysDeptService;
import com.scp.service.sysmgr.SysEmailService;
import com.scp.service.sysmgr.SysEmailTemplateService;
import com.scp.service.sysmgr.SysFaqService;
import com.scp.service.sysmgr.SysFormdefService;
import com.scp.service.sysmgr.SysGridDefService;
import com.scp.service.sysmgr.SysLoginCtrlService;
import com.scp.service.sysmgr.SysMemoService;
import com.scp.service.sysmgr.SysMessageService;
import com.scp.service.sysmgr.SysMlService;
import com.scp.service.sysmgr.SysMsgBoardService;
import com.scp.service.sysmgr.SysReportConfigService;
import com.scp.service.sysmgr.SysReportMgrService;
import com.scp.service.sysmgr.SysSmsService;
import com.scp.service.sysmgr.SysTimeTaskService;
import com.scp.service.sysmgr.SysUserAssignMgrService;
import com.scp.service.sysmgr.SysformcfgService;
import com.scp.service.sysmgr.TemplateMgrService;
import com.scp.service.sysmgr.UnsubscribeService;
import com.scp.service.sysmgr.UserMgrService;
import com.scp.service.website.WebConfigService;
import com.scp.service.website.WebMenuService;
import com.scp.service.website.WebNewsService;
import com.scp.service.website.WebPagesService;
import com.scp.service.website.WebRegisterService;
import com.scp.service.wms.WmsDispatchService;
import com.scp.service.wms.WmsDispatchdtlService;
import com.scp.service.wms.WmsInMgrService;
import com.scp.service.wms.WmsIndtlMgrService;
import com.scp.service.wms.WmsOutMgrService;
import com.scp.service.wms.WmsOutdtlMgrService;
import com.scp.service.wms.WmsPriceGroupMgrService;
import com.scp.service.wms.WmsPriceMgrService;
import com.scp.service.wms.WmsTransMgrService;
import com.scp.service.wms.WmsTransdtlMgrService;
import com.scp.util.AppUtils;

@Component("serviceContext")
public class ServiceContext {

	Logger logger = Logger.getLogger(ServiceContext.class.getName());

	@Resource
	public LoginService loginService;

	@Resource
	public CommonDBService commonDBService;
	
	@Resource
	public CommonDBCache commonDBCache;

	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	@Resource
	public DaoIbatisTemplateMySql daoIbatisTemplateMySql;

	// date------start-------------------

	@Resource
	public AccountMgrService accountMgrService;
	
	@Resource
	public AcctRpMgrService acctRpMgrService;

	@Resource
	public BankMgrService bankMgrService;
	
	
	@Resource
	public LineMgrService lineMgrService;
	
	@Resource
	public Door2doordayMgrService door2doordayMgrService;

	@Resource
	public CntypeMgrService cntypeMgrService;

	@Resource
	public CountryMgrService countryMgrService;

	@Resource
	public CurrencyMgrService currencyMgrService;

	@Resource
	public ExchangeRateMgrService exchangeRateMgrService;

	@Resource
	public GoodsMgrService goodsMgrService;

	@Resource
	public GoodstypeMgrService goodstypeMgrService;

	@Resource
	public PortyMgrService portyMgrService;

	@Resource
	public UnitMgrService unitMgrService;

	@Resource
	public WarehouseareaMgrService warehouseareaMgrService;

	@Resource
	public WarehouselocalMgrService warehouselocalMgrService;

	@Resource
	public WarehouseMgrService warehouseMgrService;

	@Resource
	public FeeItemMgrService feeItemMgrService;
	
	
	@Resource
	public PackageMgrService packageMgrService;
	
	
	@Resource
	public FiledataMgrService filedataMgrService;
	
	
	// bpm------start-------------------
	@Resource
	public BpmTaskService bpmTaskService;
	
	@Resource
	public BpmProcessinstanceService bpmProcessinstanceService;
	
	@Resource
	public BpmProcessService bpmProcessService;
	
	@Resource
	public BpmTraceService bpmTraceService;
	
	@Resource
	public BpmAssignService bpmAssignService;
	
	@Resource
	public BpmAssignRefService bpmAssignRefService;
	
	@Resource
	public BpmWorkItemService bpmWorkItemService;
	
	@Resource
	public BpmProcessinsVarService bpmProcessinsVarService;
	
	@Resource
	public BpmCommentsTempService bpmCommentsTempService;
	
	
	
	
	

	// bus------start-------------------
	@Resource
	public ShipScheduleService shipScheduleService;
	
	@Resource
	public BusCustomsMgrService busCustomsMgrService;
	
	@Resource
	public BusTruckMgrService busTruckMgrService;
	
	@Resource
	public BusGoodsMgrService busGoodsMgrService;
	
	@Resource
	public BusCustomsTaxretMgrService busCustomsTaxretMgrService;
	
	@Resource
	public BusAirMgrService busAirMgrService;
	
	@Resource
	public BusAirFlightMgrService busAirFlightMgrService;
	

	// customer------start-------------------

	@Resource
	public CustomerMgrService customerMgrService;
	
	
	// blacklist------start-------------------
	@Resource
	public BlackListService blackListService;

	// sysmgr------start-------------------
	@Resource
	public  SysUserAssignMgrService sysUserAssignMgrService;
	
	@Resource
	public  SysCorpinvMgrService sysCorpinvMgrService;
	
	@Resource
	public  SysDeptService sysDeptService;

	@Resource
	public AddressListMgrMgrService addressListMgrMgrService;

	@Resource
	public RoleMgrService roleMgrService;

	@Resource
	public SysLoginCtrlService sysLoginCtrlService;

	@Resource(name = "sysMemoService")
	public SysMemoService sysMemoService;

	@Resource
	public SysMessageService sysMessageService;

	@Resource
	public SysMlService sysMlService;

	@Resource
	public SysMsgBoardService sysMsgBoardService;

	@Resource
	public SysSmsService sysSmsService;

	@Resource
	public SysFaqService sysFaqService;
	
	@Resource
	public SysTimeTaskService sysTimeTaskService;
	
	@Resource
	public RegisterService registerService;
	
	@Resource
	public SysEmailService sysEmailService;
	
	@Resource
	public SysAttachmentService sysAttachmentService;

	@Resource
	public TemplateMgrService templateMgrService;

	@Resource
	public BusNoRuleMgrService busNoRuleMgrService;

	@Resource
	public SysReportMgrService sysReportMgrService;
	
	@Resource
	public SysCustLibMgrService custLibMgrService;
	
	@Resource
	public SysGridDefService sysGridDefService;
	
	@Resource
	public UserMgrService userMgrService;
	
	@Resource
	public CustomerContactsMgrService customerContactsMgrService;

	@Resource
	public CustomerContractMgrService customerContractMgrService;
	
	@Resource
	public CustomerAccountMgrService customerAccountMgrService;
	
	@Resource
	public CustomerCareMgrService customerCareMgrService;
	
	@Resource
	public CustomerServiceMgrService customerServiceMgrService;
	
	@Resource
	public CustomerMemorialdayMgrService customerMemorialdayMgrService;

	@Resource
	public CustomerSysCorpextMgrService customerSysCorpextMgrService;

	@Resource
	public SysReportConfigService sysReportConfigService;
	
	@Resource
	public SysCorporationService sysCorporationService;
	
	
	@Resource
	public SysFormdefService sysFormdefService;
	
	@Resource
	public SysformcfgService sysformcfgService;

	// finace------start-------------------
	@Resource
	public ArapMgrService arapMgrService;


	@Resource
	public AgenBillMgrService agenBillMgrService;
	
	@Resource
	public AgenBilldtlMgrService agenBilldtlMgrService;
	
	@Resource
	public ActPayRecService actPayRecService;
	
	@Resource
	public BillMgrService billMgrService;
	
	@Resource
	public InvoiceMgrService invoiceMgrService;
	
	
	@Resource
	public StatementMgrService statementMgrService;
	
	@Resource
	public RpReqMgrService reqMgrService;
	
	@Resource
	public VchtypeMgrService vchtypeMgrService;
	
	@Resource
	public VchdescMgrService vchdescMgrService;

	@Resource
	public AttachmentService attachmentService;

	
	//--ff begin
	
//	@Resource
//	public RuntimeContext runtimeContext;
//	
//	//@Resource
//	public ModelMgrService modelMgrService(){;
//		return (ModelMgrService) AppUtils.getBeanFromSpringIoc("modelMgrService");
//	}
//	
////	@Resource
//	public TFfAssignService tFfAssignService(){;
//		return (TFfAssignService) AppUtils.getBeanFromSpringIoc("tFfAssignService");
//	}
//	
////	@Resource
//	public TFfAssignTempService tFfAssignTempService(){;
//		return (TFfAssignTempService) AppUtils.getBeanFromSpringIoc("tFfAssignTempService");
//	}
//	
////	@Resource
//	public TFfProcessRefService tFfProcessRefService(){;
//		return (TFfProcessRefService) AppUtils.getBeanFromSpringIoc("tFfProcessRefService");
//	}
//	
//	//@Resource
//	public TFfRtTaskinstanceDescMgrService tFfRtTaskinstanceDescMgrService(){;
//		return (TFfRtTaskinstanceDescMgrService) AppUtils.getBeanFromSpringIoc("tFfRtTaskinstanceDescMgrService");
//	}
//	
	//--ff end
	
	
	//--finance begin
	
	@Resource
	public SubjectMgrService subjectMgrService;
	//--end
	
	//finance fs begin
	@Resource
	public AccountSetMgrService accountsetMgrService;
	
	@Resource
	public AstMgrService astMgrService ;
	
	@Resource
	public ArapSubjectMgrService arapSubjectMgrService;
	
	  
	
	@Resource
	public ActperiodMgrService actperiodMgrService;
	  
	@Resource
	public ActrateMgrService actrateMgrService;
	
	@Resource
	public VchMgrService vchMgrService;
	
	@Resource
	public VchdtlMgrService vchdtlMgrService;
	
	@Resource
	public FsAstMgrService fsAstMgrService;
	
	@Resource
	public FsCashMgrService fsCashMgrService;
	
	@Resource
	public FsBankMgrService fsBankMgrService;
	
	@Resource
	public FsAstrefMgrService fsAstrefMgrService;
	
	@Resource
	public PeriodXrateService periodXrateService;
	
	@Resource
	public JobsMgrService jobsMgrService;
	
	@Resource
	public FsConfigRpMgrService fsConfigRpMgrService;
	@Resource
	public FsConfigShareMgrService fsConfigShareMgrService;
	
	
	//ship
	@Resource
	public BusShipContainerMgrService busShipContainerMgrService;
	
	@Resource
	public BusShipBillMgrService busShipBillMgrService;
	
	@Resource
	public BusShippingMgrService busShippingMgrService;

	@Resource
	public BusCommerceService busCommerceService;

	@Resource
	public BusShipjoinMgrService busShipjoinMgrService;
	
	@Resource
	public BusShipjoingoodsMgrService busShipjoingoodsMgrService;
	
	@Resource
	public BusShipjoinlinkMgrService busShipjoinlinkMgrService;
	
	@Resource
	public BusShipHoldMgrService busShipHoldMgrService;
	
	@Resource
	public EdiMapMgrService ediMapMgrService;
	
	@Resource
	public BusShipAddresMgrService busShipAddresMgrService;
	
	//bus
	@Resource
	public BusDocdefMgrService  busDocdefMgrService;
	@Resource
	public BusDocexpMgrService busDocexpMgrService;
	
	@Resource
	public BusDocexpLinkMgrService busDocexpLinkMgrService;
	
	@Resource
	public BusBookingMgrService busBookingMgrService;
	
	@Resource
	public FeeTemplateMgrService feeTemplateMgrService;
	
	@Resource
	public BusShipCostdtlMgrService busShipCostdtlMgrService;
	
	@Resource
	public BusShipCostMgrService busShipCostMgrService;
	
	@Resource
	public CostMgrService costMgrService;
	
	
	@Resource
	public BusGoodstrackMgrService busGoodstrackMgrService;
	
	@Resource
	public BusGoodstrackTempMgrService busGoodstrackTempMgrService;
	
	@Resource
	public BusShipBillregMgrService busShipBillregMgrService;
	
	@Resource
	public CorpvisitService corpvisitService;
	

//	@Resource
//	public  DzzService dzzService;
	
	@Resource
	public  PricefclMgrService pricefclMgrService;
	
	@Resource
	public  PriceAirService priceAirService;
	
	@Resource
	public  PricefclpubruleMgrService pricefclpubruleMgrService;
	
	@Resource
	public  PriceHomepageMgrService priceHomepageMgrService;
	
	@Resource
	public  PricefclfeeaddMgrService pricefclfeeaddMgrService;
	
	@Resource
	public  PriceFclBargeDtlService priceFclBargeDtlService;
	
	@Resource
	public  PriceFclBargeService priceFclBargeService;
	
	@Resource
	public  PriceGroupService priceGroupService;
	
	@Resource
	public BusOrderMgrService busOrderMgrService;
	
	@Resource
	public FinaBillCntdtlMgrService finaBillCntdtlMgrService;
	
	@Resource
	public FinaPrepaidMgrService finaPrepaidMgrService;
	
	@Resource
	public CsBookingMgrService csBookingMgrService;

	
	// WMS------start-------------------
	//@Resource
	public WmsIndtlMgrService wmsIndtlMgrService(){;
		return (WmsIndtlMgrService) AppUtils.getBeanFromSpringIoc("wmsIndtlMgrService");
	}

	//@Resource
	public WmsInMgrService wmsInMgrService(){;
		return (WmsInMgrService) AppUtils.getBeanFromSpringIoc("wmsInMgrService");
	}

	//@Resource
	public WmsOutdtlMgrService wmsOutdtlMgrService(){;
		return (WmsOutdtlMgrService) AppUtils.getBeanFromSpringIoc("wmsOutdtlMgrService");
	}

	//@Resource
	public WmsOutMgrService wmsOutMgrService(){;
		return (WmsOutMgrService) AppUtils.getBeanFromSpringIoc("wmsOutMgrService");
	}

	//@Resource
	public WmsPriceMgrService wmsPriceMgrService(){;
		return (WmsPriceMgrService) AppUtils.getBeanFromSpringIoc("wmsPriceMgrService");
	}

	//@Resource
	public WmsPriceGroupMgrService wmsPriceGroupMgrService(){;
		return (WmsPriceGroupMgrService) AppUtils.getBeanFromSpringIoc("wmsPriceGroupMgrService");
	}

	//@Resource
	public WmsDispatchdtlService wmsDispatchdtlService(){;
		return (WmsDispatchdtlService) AppUtils.getBeanFromSpringIoc("wmsDispatchdtlService");
	}

	//@Resource
	public WmsDispatchService wmsDispatchService(){;
		return (WmsDispatchService) AppUtils.getBeanFromSpringIoc("wmsDispatchService");
	}
	// WMS------end-------------------
	
	// OA------start-------------------
//	@Resource
	public WorkReportService workReportService(){;
		return (WorkReportService) AppUtils.getBeanFromSpringIoc("workReportService");
	}
	
//	@Resource
	public SalaryBillMgrService salaryBillMgrService(){;
		return (SalaryBillMgrService) AppUtils.getBeanFromSpringIoc("salaryBillMgrService");
	}
	
//	@Resource
	public OaLeaveapplicationService oaLeaveapplicationService(){;
		return (OaLeaveapplicationService) AppUtils.getBeanFromSpringIoc("oaLeaveapplicationService");
	}
	
//	@Resource
	public OaUserInfoService oaUserInfoService(){;
		return (OaUserInfoService) AppUtils.getBeanFromSpringIoc("oaUserInfoService");
	}
	
//	@Resource
	public OaUserContractService oaUserContractService(){;
		return (OaUserContractService) AppUtils.getBeanFromSpringIoc("oaUserContractService");
	}
	
//	@Resource
	public OaUserTransferService oaUserTransferService(){;
		return (OaUserTransferService) AppUtils.getBeanFromSpringIoc("oaUserTransferService");
	}
	
//	@Resource
	public OaUserVisaService oaUserVisaService(){;
		return (OaUserVisaService) AppUtils.getBeanFromSpringIoc("oaUserVisaService");
	}
	
//	@Resource
	public OaUserProtocolService oaUserProtocolService(){;
		return (OaUserProtocolService) AppUtils.getBeanFromSpringIoc("oaUserProtocolService");
	}
	
//	@Resource
	public UserChangeService userChangeService(){;
		return (UserChangeService) AppUtils.getBeanFromSpringIoc("userChangeService");
	}
	
//	@Resource
	public UserEvService userEvService(){;
		return (UserEvService) AppUtils.getBeanFromSpringIoc("userEvService");
	}
	
//	@Resource
	public UserLogService userLogService(){;
		return (UserLogService) AppUtils.getBeanFromSpringIoc("userLogService");
	}
	
//	@Resource
	public OaTimeSheetService oaTimeSheetService(){;
		return (OaTimeSheetService) AppUtils.getBeanFromSpringIoc("oaTimeSheetService");
	}
	
//	@Resource
	public JobChangeService jobChangeService(){;
		return (JobChangeService) AppUtils.getBeanFromSpringIoc("jobChangeService");
	}
	
	//@Resource
	public AppTravelService appTravelService(){;
		return (AppTravelService) AppUtils.getBeanFromSpringIoc("appTravelService");
	}
	
//	@Resource
	public OaFeeService oaFeeService(){;
		return (OaFeeService) AppUtils.getBeanFromSpringIoc("oaFeeService");
	}
	
//	@Resource
	public BaseInfoService baseInfoService(){;
		return (BaseInfoService) AppUtils.getBeanFromSpringIoc("baseInfoService");
	}
	
//	@Resource
	public SalaryTableService salaryTableService(){;
		return (SalaryTableService) AppUtils.getBeanFromSpringIoc("salaryTableService");
	}
	
//	@Resource
	public SalaryDoubleService salaryDoubleService(){;
		return (SalaryDoubleService) AppUtils.getBeanFromSpringIoc("salaryDoubleService");
	}
	
//	@Resource
	public SalaryWelfareService salaryWelfareService(){;
		return (SalaryWelfareService) AppUtils.getBeanFromSpringIoc("salaryWelfareService");
	}
	
//	@Resource
	public OaDatFiledataService oaDatFiledataService(){;
		return (OaDatFiledataService) AppUtils.getBeanFromSpringIoc("OaDatFiledataService");
	}
	// OA------end-------------------
	
	
	// del------begin-------------------
	@Resource
	public DelLoadMgrService delLoadMgrService;

	@Resource
	public DelLoaddtlMgrService delLoaddtlMgrService;



	@Resource
	public DeliveryMgrService deliveryMgrService;

	@Resource
	public DeliverydtlMgrService deliverydtlMgrService;
	// del------end-------------------
	
	@Resource
	public CarTypeMgrService carTypeMgrService;

	@Resource
	public CarMgrService carMgrService;

	@Resource
	public DriverMgrService driverMgrService;
	
	@Resource
	public CarOilRecordMgrService carOilRecordMgrService;

	@Resource
	public CarRepaireRecordMgrService carRepaireRecordMgrService;
	
	@Resource
	public BusContainerMgrService busContainerMgrService;
	
	@Resource
	public SysEmailTemplateService sysEmailTemplateService;
	
	@Resource
	public FinaConfigarapService finaConfigarapService;
	
	@Resource
	public SysKnowledgelibService sysKnowledgelibService;
	
	@Resource
	public LinecodeMgrService linecodeMgrService;
	
	@Resource
	public BusAirBillMgrService busAirBillMgrService;
	
	@Resource
	public BusPriceService busPriceService;
	
	@Resource
	public BusPriceDtlService busPriceDtlService;
	
	@Resource
	public PriceFclUseruleService priceFclUseruleService;
	
	@Resource
	public BusCreightChargeMgrService busCreightChargeMgrService;
	
	@Resource
	public WebRegisterService webRegisterService;
	
	@Resource
	public WebNewsService webNewsService;
	
	@Resource
	public WebConfigService webConfigService;
	
	@Resource
	public BusTrainMgrService busTrainMgrService;
	
	@Resource
	public WebMenuService webMenuService;
	
	@Resource
	public UnsubscribeService unsubscribeService;
	
	@Resource
	public WebPagesService webPagesService;
	
	@Resource
	public BusGpsRefService busGpsRefService;
	
	@Resource
	public BusGpsService busGpsService;
	
	@Resource
	public BusTrainBillMgrService busTrainBillMgrService;
	
	@Resource
	public WmsTransMgrService wmsTransMgrService;
	
	@Resource
	public WmsTransdtlMgrService wmsTransdtlMgrService;
	
	@Resource
	public PriceTrainService priceTrainService;
	
	@Resource
	public PriceTruckService priceTruckService;
	
	@Resource
	public SysCorporationAgentnService sysCorporationAgentnService;
	
	@Resource
	public ApiMaerskService apiMaerskService;
	
	@Resource
	public EdiTransMgrService ediTransMgrService;
	
	@Resource
	public BusBookingrptService busBookingrptService;
	
	@Resource
	public MgrTrainBookingService mgrTrainBookingService;
	
	@Resource
	public BusBillnoMgrService busBillnoMgrService;
	
	@Resource
	public BusInsuranceService datInsuranceService;
	
	@Resource
	public SysCorporationApplyMonthService sysCorporationApplyMonthService;
	
	@Resource
	public AirPlaneTypeService airPlaneTypeService;
	
	@Resource
	public BusAirExtfeeService busAirExtfeeService;
	
	@Resource
	public AirBookcodeMgrService airBookcodeMgrService;
	
	@Resource
	public ChannelService channelService;
	
	@Resource
	public ElogisticsService elogisticsService;
	
	@Resource
	public ApiCoscoBookdataService apiCoscoBookdataService;

	@Resource
	public ApiRobotBookPreService apiRobotBookpreService;
	
	@Resource
	public ApiRobotBookService apiRobotBookService;
	
	@Resource
	public ApiRobotEsiService apiRobotEsiService;
	@Resource
	public FavoritesService favoritesService;
	@Resource 
	public RobotFeeaddService robotFeeaddService;

	@Resource
	public ApiOoclBookdataService apiOoclBookdataService;
	@Resource 
	public PriceFclAskService priceFclAskService;
	
	@Resource 
	public EdiinttrareportMgrService ediinttrareportMgrService;
	
	@Resource
	public ApiVgmService apiVgmService;
	
	@Resource
	public ApiVgmdtlService apiVgmdtlService;
	
	@Resource
	public ApiDataMgrService apiDataMgrService;
	

	@Resource
	public EdiesiService ediesiService;

	@Resource
	public EdiesiDtlCntService ediesiDtlCntService;

	@Resource
	public VoyageService voyageService;

	@Resource
	public TarifasapiService tarifasapiService;
}
