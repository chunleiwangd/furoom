function include(path){ 
    var a=$('<script></script>')
    a.attr("type","text/javascript"); 
    a.attr('src',path); 
   $('head').append(a);
}
include('/resources/FuRoomClient.js');
include('/furoom/gpps.service.IMyAccountService?json');
include('/furoom/gpps.service.IGovermentOrderService?json');

var myaccountService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IMyAccountService');
var orderService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IGovermentOrderService');