function include(path){ 
    var a=$('<script></script>')
    a.attr("type","text/javascript"); 
    a.attr('src',path); 
   $('head').append(a);
}
include('/resources/FuRoomClient.js');
include('/furoom/gpps.service.IMyAccountService?json');
include('/furoom/gpps.service.IProductActionService?json');
include('/furoom/gpps.service.IBorrowerService?json');
include('/furoom/gpps.dao.ILenderAccountDao?json');
include('/furoom/gpps.service.ISubscribeService?json');
include('/furoom/gpps.service.ISubmitService?json');
include('/furoom/gpps.service.thirdpay.IThirdPaySupportService?json');
include('/furoom/gpps.service.IGovermentOrderService?json');

var myaccountService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IMyAccountService');
var productActionService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IProductActionService');
var accountDao = FuRoomClient.getRemoteProxy('/furoom/gpps.dao.ILenderAccountDao');
var bservice = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IBorrowerService');
var subscribeService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.ISubscribeService');
var submitService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.ISubmitService');
var tpservice = FuRoomClient.getRemoteProxy('/furoom/gpps.service.thirdpay.IThirdPaySupportService');
var orderService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IGovermentOrderService');