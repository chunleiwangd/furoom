var lettercount = 0;
var usertype=null;
var user=null;
var openId = getQueryString("openId");

if(openId!=null && openId!=''){
	try{
		var res = bindService.getCurrentUser(openId);
		if(res!=null)
		{
			usertype = res.get('usertype');
			user = res.get('value');
			if(usertype=='lender'){
				lettercount = res.get('letter');
			}
		}else{
			window.location.href="weixin_bind.html?openId="+openId;
		}
	}catch(e){
		alert(e.message);
	}
}