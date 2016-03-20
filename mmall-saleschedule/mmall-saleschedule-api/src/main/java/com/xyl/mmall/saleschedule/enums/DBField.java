package com.xyl.mmall.saleschedule.enums;

/**
 * Meta字段名。和表字段相同。构造SQL时使用这些常量。避免写死。
 * 
 * @author hzzhanghui
 * 
 */
public class DBField {
	public static class ScheduleField {
		public static final String id = "id";

		public static final String userId = "userId";

		public static final String startTime = "startTime";

		public static final String endTime = "endTime";

		public static final String adjustTimeDesc = "adjustTimeDesc";

		public static final String curSupplierAreaId = "curSupplierAreaId";

		public static final String supplierId = "supplierId";

		public static final String supplierName = "supplierName";

		public static final String brandId = "brandId";

		public static final String brandName = "brandName";

		public static final String brandNameEn = "brandNameEn";

		public static final String brandLogo = "brandLogo";

		public static final String brandLogoSmall = "brandLogoSmall";

		public static final String categoryId = "categoryId";

		public static final String categoryName = "categoryName";

		public static final String title = "title";

		public static final String showOrder = "showOrder";

		public static final String adPosition = "adPosition";

		public static final String createTimeForLogic = "createTimeForLogic";

		public static final String createUser = "createUser";

		public static final String status = "status";

		public static final String statusMsg = "statusMsg";

		public static final String updateTimeForLogic = "updateTimeForLogic";

		public static final String scheduleUpdateDate = "scheduleUpdateDate";

		public static final String grossProfitRate = "grossProfitRate";

		public static final String cPrice = "cPrice";

		public static final String unlossFlag = "unlossFlag";

		public static final String platformSrvFeeRate = "platformSrvFeeRate";

		public static final String mortgageRate = "mortgageRate";

		public static final String deposit = "deposit";

		public static final String maxPriceAfterDiscount = "maxPriceAfterDiscount";

		public static final String minPriceAfterDiscount = "minPriceAfterDiscount";

		public static final String productTotalCnt = "productTotalCnt";

		public static final String maxDiscount = "maxDiscount";

		public static final String minDiscount = "minDiscount";

		public static final String unitCnt = "unitCnt";

		public static final String skuCnt = "skuCnt";

		public static final String saleAreaId = "saleAreaId";

		public static final String storeAreaId = "storeAreaId";

		public static final String jitMode = "jitMode";

		public static final String flagAuditPrdList = "flagAuditPrdList";

		public static final String flagAuditPage = "flagAuditPage";

		public static final String flagAuditBanner = "flagAuditBanner";

		public static final String pageId = "pageId";

		public static final String bannerId = "bannerId";

		public static final String chlFlag = "chlFlag";

		public static final String pageTitle = "pageTitle";

		public static final String normalShowPeriod = "normalShowPeriod";

		public static final String extShowPeriod = "extShowPeriod";

		public static final String saleSiteFlag = "saleSiteFlag";
	}

	public static class ViceField {
		public static final String id = "id";

		public static final String scheduleId = "scheduleId";

		public static final String userId = "userId";

		public static final String startTime = "startTime";

		public static final String endTime = "endTime";

		public static final String showOrder = "showOrder";

		public static final String adPosition = "adPosition";

		public static final String createUser = "createUser";

		public static final String statusMsg = "statusMsg";

		public static final String grossProfitRate = "grossProfitRate";

		public static final String cPrice = "cPrice";

		public static final String unlossFlag = "unlossFlag";

		public static final String platformSrvFeeRate = "platformSrvFeeRate";

		public static final String mortgageRate = "mortgageRate";

		public static final String deposit = "deposit";

		public static final String maxPriceAfterDiscount = "maxPriceAfterDiscount";

		public static final String minPriceAfterDiscount = "minPriceAfterDiscount";

		public static final String productTotalCnt = "productTotalCnt";

		public static final String maxDiscount = "maxDiscount";

		public static final String minDiscount = "minDiscount";

		public static final String unitCnt = "unitCnt";

		public static final String skuCnt = "skuCnt";

		public static final String saleAreaId = "saleAreaId";

		public static final String storeAreaId = "storeAreaId";

		public static final String jitMode = "jitMode";

		public static final String flagAuditPage = "flagAuditPage";

		public static final String flagAuditBanner = "flagAuditBanner";

		public static final String pageId = "pageId";

		public static final String bannerId = "bannerId";

		public static final String chlFlag = "chlFlag";

		public static final String pageTitle = "pageTitle";

		public static final String normalShowPeriod = "normalShowPeriod";

		public static final String extShowPeriod = "extShowPeriod";

		public static final String siteFlag = "siteFlag";

		public static final String poFollowerUserId = "poFollowerUserId";

		public static final String poFollowerUserName = "poFollowerUserName";

		public static final String supplierAcct = "supplierAcct";

		public static final String supplyMode = "supplyMode";

		public static final String supplierStoreId = "supplierStoreId";

		public static final String brandStoreId = "brandStoreId";
		
		public static final String supplierType = "supplierType";
		
		public static final String brandSupplierName = "brandSupplierName";
		
		public static final String brandSupplierId = "brandSupplierId";
		
		public static final String poType = "poType";
		
		public static final String flagAuditPrdzl = "flagAuditPrdzl";
		
		public static final String flagAuditPrdqd = "flagAuditPrdqd";
	}

	public static class BannerField {
		public static final String id = "id";

		public static final String userId = "userId";

		public static final String userName = "userName";

		public static final String scheduleId = "scheduleId";

		public static final String saleAreaId = "saleAreaId";

		public static final String supplierName = "supplierName";

		public static final String brandId = "brandId";

		public static final String brandName = "brandName";

		public static final String brandNameEn = "brandNameEn";

		public static final String bannerImg = "bannerImg";

		public static final String bannerPosition = "bannerPosition";

		public static final String homeBannerImgUrl = "homeBannerImgUrl";

		public static final String homeBannerImgId = "homeBannerImgId";

		public static final String preBannerImgUrl = "preBannerImgUrl";

		public static final String preBannerImgId = "preBannerImgId";

		public static final String supplierId = "supplierId";

		public static final String comment = "comment";

		public static final String status = "status";

		public static final String statusUpdateDate = "statusUpdateDate";

		public static final String submitDate = "submitDate";

		public static final String statusMsg = "statusMsg";

		public static final String updateDate = "updateDate";

		public static final String createDate = "createDate";

		public static final String auditUserId = "auditUserId";

		public static final String auditUserName = "auditUserName";
	}

	public static class SiteRelaField {
		public static final String id = "id";

		public static final String scheduleId = "scheduleId";

		public static final String saleSiteId = "saleSiteId";
	}

	public static class ChannelRelaField {
		public static final String scheduleId = "scheduleId";

		public static final String curSupplierAreaId = "curSupplierAreaId";

		public static final String homeFlag = "homeFlag";

		public static final String womenFlag = "womenFlag";

		public static final String menFlag = "menFlag";

		public static final String weaveFlag = "weaveFlag";

		public static final String shoeBagFlag = "shoeBagFlag";

		public static final String childrenFlag = "childrenFlag";

		public static final String rsv1Flag = "rsv1Flag";

		public static final String rsv2Flag = "rsv2Flag";

		public static final String rsv3Flag = "rsv3Flag";

		public static final String rsv4Flag = "rsv4Flag";

		public static final String rsv5Flag = "rsv5Flag";

		public static final String rsv6Flag = "rsv6Flag";

	}

	public static class LikeField {
		public static final String id = "id";

		public static final String userId = "userId";

		public static final String scheduleId = "scheduleId";

		public static final String createDate = "createDate";

		public static final String status = "status";

		public static final String statusUpdateDate = "statusUpdateDate";

	}

	public static class PageField {
		public static final String id = "id";

		public static final String userId = "userId";

		public static final String scheduleId = "scheduleId";

		public static final String saleAreaId = "saleAreaId";

		public static final String supplierName = "supplierName";

		public static final String supplierId = "supplierId";

		public static final String brandName = "brandName";

		public static final String brandNameEn = "brandNameEn";

		public static final String brandId = "brandId";

		public static final String createDate = "createDate";

		public static final String createPerson = "createPerson";

		public static final String title = "title";

		public static final String status = "status";

		public static final String prdListOrderType = "prdListOrderType";

		public static final String statusUpdateDate = "statusUpdateDate";

		public static final String statusMsg = "statusMsg";

		public static final String comment = "comment";

		public static final String updateCnt = "updateCnt";

		public static final String udProductIds = "udProductIds";

		public static final String udSetting = "udSetting";

		public static final String udImgIds = "udImgIds";

		public static final String bgImgId = "bgImgId";

		public static final String bgSetting = "bgSetting";

		public static final String headerImgId = "headerImgId";

		public static final String headerSetting = "headerSetting";

		public static final String allListPartVisiable = "allListPartVisiable";

		public static final String allListPartOthers = "allListPartOthers";

		public static final String mapPartVisiable = "mapPartVisiable";

		public static final String mapPartOthers = "mapPartOthers";

		public static final String partDisplayOrderList = "partDisplayOrderList";
	}

	public static class MagicCubeField {
		public static final String id = "id";

		public static final String scheduleId = "scheduleId";

		public static final String curSupplierAreaId = "curSupplierAreaId";

		public static final String supplierId = "supplierId";

		public static final String logDay = "logDay";

		public static final String sale = "sale";

		public static final String saleCnt = "saleCnt";

		public static final String buyerCnt = "buyerCnt";

		public static final String saleRate = "saleRate";

		public static final String supplyMoney = "supplyMoney";

		public static final String skuCnt = "skuCnt";

		public static final String uv = "uv";

		public static final String pv = "pv";

		public static final String rsv1 = "rsv1";

		public static final String rsv2 = "rsv2";

		public static final String rsv3 = "rsv3";
	}

	public static class QRQMField {

		public static final String id = "id";

		public static final String userId = "userId";

		public static final String lastLogin = "lastLogin";

	}
}
