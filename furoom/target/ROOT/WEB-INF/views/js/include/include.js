document.write("<META HTTP-EQUIV='nocache' CONTENT='no-cache'>");


document.write("<script type='text/javascript' src='/resources/FuRoomClient.js'></script>");

document.write("<script type='text/javascript' src='/furoom/gpps.service.ILenderService?json'></script>");
document.write("<script type='text/javascript' src='/furoom/gpps.service.IBorrowerService?json'></script>");
document.write("<script type='text/javascript' src='/furoom/gpps.service.IProductSeriesService?json'></script>");
document.write("<script type='text/javascript' src='/furoom/gpps.service.IGovermentOrderService?json'></script>");
document.write("<script type='text/javascript' src='/furoom/gpps.service.IProductService?json'></script>");
document.write("<script type='text/javascript' src='/furoom/gpps.service.INoticeService?json'></script>");
document.write("<script type='text/javascript' src='/furoom/gpps.service.INewsService?json'></script>");
document.write("<script type='text/javascript' src='/furoom/gpps.service.IStatisticsService?json'></script>");
document.write("<script type='text/javascript' src='/furoom/gpps.service.IMyAccountService?json'></script>");
document.write("<script type='text/javascript' src='/furoom/gpps.service.IActivityRefService?json'></script>");
document.write("<script type='text/javascript' src='/furoom/gpps.service.IActivityService?json'></script>");
document.write("<script type='text/javascript' src='/furoom/gpps.service.IAdminService?json'></script>");
document.write("<script type='text/javascript' src='/furoom/gpps.service.IHelpService?json'></script>");

document.write("<script type='text/javascript' src='/furoom/gpps.service.IAccountService?json'></script>");
document.write("<script type='text/javascript' src='/furoom/gpps.service.ISubmitService?json'></script>");
document.write("<script type='text/javascript' src='/furoom/gpps.service.IPayBackService?json'></script>");
document.write("<script type='text/javascript' src='/furoom/gpps.service.ILetterService?json'></script>");
document.write("<script type='text/javascript' src='/furoom/gpps.service.thirdpay.IThirdPaySupportService?json'></script>");
document.write("<script type='text/javascript' src='/furoom/gpps.service.IPurchaseService?json'></script>");
document.write("<script type='text/javascript' src='/furoom/gpps.dao.ILenderAccountDao?json'></script>");
document.write("<script type='text/javascript' src='/furoom/gpps.dao.IBorrowerAccountDao?json'></script>");

document.write("<script type='text/javascript' src='/furoom/gpps.token.service.ITokenService?json'></script>");

document.write("<script type='text/javascript' src='/furoom/gpps.service.IContractService?json'></script>");
document.write("<script type='text/javascript' src='/furoom/gpps.service.IInviteService?json'></script>");

document.write("<script type='text/javascript' src='/furoom/gpps.dao.IGovermentOrderDao?json'></script>");
document.write("<script type='text/javascript' src='/furoom/gpps.service.IWaitToDoStatisticsService?json'></script>");
document.write("<script type='text/javascript' src='/furoom/gpps.service.message.IMessageSupportService?json'></script>");

document.write("<script type='text/javascript' src='/furoom/gpps.service.IProductActionService?json'></script>");
document.write("<script type='text/javascript' src='/furoom/gpps.service.ISubscribeService?json'></script>");
//document.write("<script type='text/javascript' src='/furoom/gpps.dao.ISubscribeDao?json'></script>");
document.write("<script type='text/javascript' src='/furoom/weixin.service.IBindService?json'></script>");
document.write("<script type='text/javascript' src='/furoom/weixin.service.ICentralService?json'></script>");


    				
document.write("<script>");
document.write("var service = FuRoomClient.getRemoteProxy('/furoom/gpps.service.ILenderService');");
document.write("var bservice = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IBorrowerService');");
document.write("var myaccountService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IMyAccountService');");
document.write("var orderService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IGovermentOrderService');");
document.write("var productService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IProductService');");
document.write("var seriesService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IProductSeriesService');");
document.write("var accountDao = FuRoomClient.getRemoteProxy('/furoom/gpps.dao.ILenderAccountDao');");
document.write("var baccountDao = FuRoomClient.getRemoteProxy('/furoom/gpps.dao.IBorrowerAccountDao');");
document.write("var noticeService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.INoticeService');");
document.write("var newsService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.INewsService');");
document.write("var statisticService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IStatisticsService');");
document.write("var refservice = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IActivityRefService');");
document.write("var actService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IActivityService');");
document.write("var adminService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IAdminService');");
document.write("var helpservice = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IHelpService');");

document.write("var tpservice = FuRoomClient.getRemoteProxy('/furoom/gpps.service.thirdpay.IThirdPaySupportService');");
document.write("var paybackService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IPayBackService');");
document.write("var accountService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IAccountService');");
document.write("var paybackService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IPayBackService');");
document.write("var letterService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.ILetterService');");
document.write("var submitService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.ISubmitService');");
document.write("var purchaseService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IPurchaseService');");

document.write("var tokenService = FuRoomClient.getRemoteProxy('/furoom/gpps.token.service.ITokenService');");
document.write("var inviteService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IInviteService');");
document.write("var contractService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IContractService');");

document.write("var toService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IWaitToDoStatisticsService');");
document.write("var orderDao = FuRoomClient.getRemoteProxy('/furoom/gpps.dao.IGovermentOrderDao');");
document.write("var messageService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.message.IMessageSupportService');");
document.write("var productActionService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IProductActionService');");
document.write("var subscribeService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.ISubscribeService');");
//document.write("var subscribeDao = FuRoomClient.getRemoteProxy('/furoom/gpps.dao.ISubscribeDao');");
document.write("var bindService = FuRoomClient.getRemoteProxy('/furoom/weixin.service.IBindService');");
document.write("var centralService = FuRoomClient.getRemoteProxy('/furoom/weixin.service.ICentralService');");


document.write("</script>");