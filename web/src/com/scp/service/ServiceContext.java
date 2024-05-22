package com.scp.service;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.DaoIbatisTemplateMySql;
import com.scp.dao.cache.CommonDBCache;
import com.scp.service.api.*;
import com.scp.service.common.AttachmentService;
import com.scp.service.customer.*;
import com.scp.service.data.*;
import com.scp.service.price.*;
import com.scp.service.salesmgr.BlackListService;
import com.scp.service.sysmgr.*;
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

    @Resource
    public CorpvisitService corpvisitService;

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

    @Resource
    public SysEmailTemplateService sysEmailTemplateService;

    @Resource
    public LinecodeMgrService linecodeMgrService;

    @Resource
    public BusPriceService busPriceService;

    @Resource
    public BusPriceDtlService busPriceDtlService;

    @Resource
    public PriceFclUseruleService priceFclUseruleService;

    @Resource
    public UnsubscribeService unsubscribeService;

    @Resource
    public PriceTrainService priceTrainService;

    @Resource
    public PriceTruckService priceTruckService;

    @Resource
    public SysCorporationAgentnService sysCorporationAgentnService;

    @Resource
    public ApiMaerskService apiMaerskService;

    @Resource
    public SysCorporationApplyMonthService sysCorporationApplyMonthService;

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
    public TarifasapiService tarifasapiService;
}
