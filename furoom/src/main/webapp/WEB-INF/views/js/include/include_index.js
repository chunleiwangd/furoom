function include(path){ 
    var a=$('<script></script>')
    a.attr("type","text/javascript"); 
    a.attr('src',path); 
   $('head').append(a);
}
//include('/resources/FuRoomClient.js');
include('/furoom/gpps.service.IMyAccountService?json');
var myaccountService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IMyAccountService');