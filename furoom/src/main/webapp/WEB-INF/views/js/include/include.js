function include(path){ 
    var a=$('<script></script>')
    a.attr("type","text/javascript"); 
    a.attr('src',path); 
   $('head').append(a);
}


include('/resources/FuRoomClient.js');

include('/furoom/gpps.service.ILenderService?json');
include('/furoom/gpps.service.IBorrowerService?json');
include('/furoom/gpps.service.IProductSeriesService?json');
include('/furoom/gpps.service.IGovermentOrderService?json');
include('/furoom/gpps.service.IProductService?json');
include('/furoom/gpps.service.INoticeService?json');
include('/furoom/gpps.service.INewsService?json');
include('/furoom/gpps.service.IStatisticsService?json');
include('/furoom/gpps.service.IMyAccountService?json');
include('/furoom/gpps.service.IActivityRefService?json');
include('/furoom/gpps.service.IActivityService?json');
include('/furoom/gpps.service.IAdminService?json');
include('/furoom/gpps.service.IHelpService?json');

include('/furoom/gpps.service.IAccountService?json');
include('/furoom/gpps.service.ISubmitService?json');
include('/furoom/gpps.service.IPayBackService?json');
include('/furoom/gpps.service.ILetterService?json');
include('/furoom/gpps.service.thirdpay.IThirdPaySupportService?json');
include('/furoom/gpps.service.IPurchaseService?json');
include('/furoom/gpps.dao.ILenderAccountDao?json');
include('/furoom/gpps.dao.IBorrowerAccountDao?json');

include('/furoom/gpps.token.service.ITokenService?json');

include('/furoom/gpps.service.IContractService?json');
include('/furoom/gpps.service.IInviteService?json');

include('/furoom/gpps.dao.IGovermentOrderDao?json');
include('/furoom/gpps.service.IWaitToDoStatisticsService?json');
include('/furoom/gpps.service.message.IMessageSupportService?json');

include('/furoom/gpps.service.IProductActionService?json');
include('/furoom/gpps.service.ISubscribeService?json');
//include('/furoom/gpps.dao.ISubscribeDao?json');
include('/furoom/weixin.service.IBindService?json');
include('/furoom/weixin.service.ICentralService?json');


    				
var service = FuRoomClient.getRemoteProxy('/furoom/gpps.service.ILenderService');
var bservice = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IBorrowerService');
var myaccountService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IMyAccountService');
var orderService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IGovermentOrderService');
var productService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IProductService');
var seriesService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IProductSeriesService');
var accountDao = FuRoomClient.getRemoteProxy('/furoom/gpps.dao.ILenderAccountDao');
var baccountDao = FuRoomClient.getRemoteProxy('/furoom/gpps.dao.IBorrowerAccountDao');
var noticeService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.INoticeService');
var newsService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.INewsService');
var statisticService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IStatisticsService');
var refservice = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IActivityRefService');
var actService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IActivityService');
var adminService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IAdminService');
var helpservice = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IHelpService');

var tpservice = FuRoomClient.getRemoteProxy('/furoom/gpps.service.thirdpay.IThirdPaySupportService');
var paybackService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IPayBackService');
var accountService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IAccountService');
var paybackService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IPayBackService');
var letterService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.ILetterService');
var submitService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.ISubmitService');
var purchaseService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IPurchaseService');

var tokenService = FuRoomClient.getRemoteProxy('/furoom/gpps.token.service.ITokenService');
var inviteService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IInviteService');
var contractService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IContractService');

var toService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IWaitToDoStatisticsService');
var orderDao = FuRoomClient.getRemoteProxy('/furoom/gpps.dao.IGovermentOrderDao');
var messageService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.message.IMessageSupportService');
var productActionService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IProductActionService');
var subscribeService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.ISubscribeService');
//var subscribeDao = FuRoomClient.getRemoteProxy('/furoom/gpps.dao.ISubscribeDao');
var bindService = FuRoomClient.getRemoteProxy('/furoom/weixin.service.IBindService');
var centralService = FuRoomClient.getRemoteProxy('/furoom/weixin.service.ICentralService');
