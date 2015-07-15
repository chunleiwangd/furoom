var lettercount = 0;
var usertype=null;
var user=null;
var openId = getQueryString("openId");
var code = getQueryString("code");
var getUser = function(url)
{
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
			
			if(url.equals("weixin_bind.html")){
				window.location.href="weixin_myaccount.html?openId="+openId;
			}
			
		}else{
			window.location.href="weixin_bind.html?openId="+openId;
		}
	}catch(e){
		window.location.href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxdd002cd13f660e62&redirect_uri=http://www.zhengcaidai.com/weixin_bind.html&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
	}
	}else{
	$('#content').append('code='+code);
	if(code!=null && code!=''){
		try{
			var openId = centralService.getOpenId(code);
			alert(openId);
			alert(url+"?version=1&openId="+openId);
			window.location.href=url+"?openId="+openId;
		}catch(e){
			window.location.href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxdd002cd13f660e62&redirect_uri=http://www.zhengcaidai.com/weixin_bind.html&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
		}
	}
	}
}