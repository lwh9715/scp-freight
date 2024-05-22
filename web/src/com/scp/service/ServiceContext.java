package com.scp.service;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.DaoIbatisTemplateMySql;
import com.scp.dao.cache.CommonDBCache;
import com.scp.service.api.*;
import com.scp.service.bus.*;
import com.scp.service.common.AttachmentService;
import com.scp.service.customer.*;
import com.scp.service.data.*;
import com.scp.service.elogistics.data.ChannelService;
import com.scp.service.elogistics.data.ElogisticsService;
import com.scp.service.oa.*;
import com.scp.service.price.*;
import com.scp.service.salesmgr.BlackListService;
import com.scp.service.ship.*;
import com.scp.service.sysmgr.*;
import com.scp.service.website.*;
import com.scp.util.AppUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author CIMC
 */
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
    public FeeItemMgrService feeItemMgrService;


    @Resource
    public PackageMgrService packageMgrService;

    @Resource
    public FiledataMgrService filedataMgrService;

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
    public SysUserAssignMgrService sysUserAssignMgrService;

    @Resource
    public SysCorpinvMgrService sysCorpinvMgrService;

    @Resource
    public SysDeptService sysDeptService;

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
    public RegisterService registerService;

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

    @Resource
    public AttachmentService attachmentService;

    //ship
    @Resource
    public BusShipContainerMgrService busShipContainerMgrService;
    @Resource
    public BusShippingMgrService busShippingMgrService;

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
    public BusDocdefMgrService busDocdefMgrService;
    @Resource
    public BusDocexpMgrService busDocexpMgrService;

    @Resource
    public BusDocexpLinkMgrService busDocexpLinkMgrService;

    @Resource
    public BusBookingMgrService busBookingMgrService;

    @Resource
    public BusShipCostdtlMgrService busShipCostdtlMgrService;

    @Resource
    public BusShipCostMgrService busShipCostMgrService;

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
    public PricefclMgrService pricefclMgrService;

    @Resource
    public PriceAirService priceAirService;

    @Resource
    public PricefclpubruleMgrService pricefclpubruleMgrService;

    @Resource
    public PriceHomepageMgrService priceHomepageMgrService;

    @Resource
    public PricefclfeeaddMgrService pricefclfeeaddMgrService;

    @Resource
    public PriceFclBargeDtlService priceFclBargeDtlService;

    @Resource
    public PriceFclBargeService priceFclBargeService;

    @Resource
    public PriceGroupService priceGroupService;

    // OA------start-------------------
//	@Resource
    public WorkReportService workReportService() {
        ;
        return (WorkReportService) AppUtils.getBeanFromSpringIoc("workReportService");
    }

    //	@Resource
    public SalaryBillMgrService salaryBillMgrService() {
        ;
        return (SalaryBillMgrService) AppUtils.getBeanFromSpringIoc("salaryBillMgrService");
    }

    //	@Resource
    public OaLeaveapplicationService oaLeaveapplicationService() {
        ;
        return (OaLeaveapplicationService) AppUtils.getBeanFromSpringIoc("oaLeaveapplicationService");
    }

    //	@Resource
    public OaUserInfoService oaUserInfoService() {
        ;
        return (OaUserInfoService) AppUtils.getBeanFromSpringIoc("oaUserInfoService");
    }

    //	@Resource
    public OaUserContractService oaUserContractService() {
        ;
        return (OaUserContractService) AppUtils.getBeanFromSpringIoc("oaUserContractService");
    }

    //	@Resource
    public OaUserTransferService oaUserTransferService() {
        ;
        return (OaUserTransferService) AppUtils.getBeanFromSpringIoc("oaUserTransferService");
    }

    //	@Resource
    public OaUserVisaService oaUserVisaService() {
        ;
        return (OaUserVisaService) AppUtils.getBeanFromSpringIoc("oaUserVisaService");
    }

    //	@Resource
    public OaUserProtocolService oaUserProtocolService() {
        ;
        return (OaUserProtocolService) AppUtils.getBeanFromSpringIoc("oaUserProtocolService");
    }

    //	@Resource
    public UserChangeService userChangeService() {
        ;
        return (UserChangeService) AppUtils.getBeanFromSpringIoc("userChangeService");
    }

    //	@Resource
    public UserEvService userEvService() {
        ;
        return (UserEvService) AppUtils.getBeanFromSpringIoc("userEvService");
    }

    //	@Resource
    public UserLogService userLogService() {
        ;
        return (UserLogService) AppUtils.getBeanFromSpringIoc("userLogService");
    }

    //	@Resource
    public OaTimeSheetService oaTimeSheetService() {
        ;
        return (OaTimeSheetService) AppUtils.getBeanFromSpringIoc("oaTimeSheetService");
    }

    //	@Resource
    public JobChangeService jobChangeService() {
        ;
        return (JobChangeService) AppUtils.getBeanFromSpringIoc("jobChangeService");
    }

    //@Resource
    public AppTravelService appTravelService() {
        ;
        return (AppTravelService) AppUtils.getBeanFromSpringIoc("appTravelService");
    }

    //	@Resource
    public OaFeeService oaFeeService() {
        ;
        return (OaFeeService) AppUtils.getBeanFromSpringIoc("oaFeeService");
    }

    //	@Resource
    public BaseInfoService baseInfoService() {
        ;
        return (BaseInfoService) AppUtils.getBeanFromSpringIoc("baseInfoService");
    }

    //	@Resource
    public SalaryTableService salaryTableService() {
        ;
        return (SalaryTableService) AppUtils.getBeanFromSpringIoc("salaryTableService");
    }

    //	@Resource
    public SalaryDoubleService salaryDoubleService() {
        ;
        return (SalaryDoubleService) AppUtils.getBeanFromSpringIoc("salaryDoubleService");
    }

    //	@Resource
    public SalaryWelfareService salaryWelfareService() {
        ;
        return (SalaryWelfareService) AppUtils.getBeanFromSpringIoc("salaryWelfareService");
    }

    //	@Resource
    public OaDatFiledataService oaDatFiledataService() {
        ;
        return (OaDatFiledataService) AppUtils.getBeanFromSpringIoc("OaDatFiledataService");
    }
    // OA------end-------------------

    @Resource
    public BusContainerMgrService busContainerMgrService;

    @Resource
    public SysEmailTemplateService sysEmailTemplateService;

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
    public BusTrainBillMgrService busTrainBillMgrService;

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
