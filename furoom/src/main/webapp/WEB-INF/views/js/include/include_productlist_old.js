document.write("<META HTTP-EQUIV='nocache' CONTENT='no-cache'>");
document.write("<script type='text/javascript' src='/resources/FuRoomClient.js'></script>");
document.write("<script type='text/javascript' src='/furoom/gpps.service.IMyAccountService?json'></script>");
document.write("<script type='text/javascript' src='/furoom/gpps.service.IGovermentOrderService?json'></script>");

document.write("<script>");
document.write("var myaccountService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IMyAccountService');");
document.write("var orderService = FuRoomClient.getRemoteProxy('/furoom/gpps.service.IGovermentOrderService');");
document.write("</script>");