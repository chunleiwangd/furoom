//var letterDao = FuRoomClient.getRemoteProxy("/furoom/gpps.dao.ILetterDao");
var lettercount = 0;

var res = myaccountService.getCurrentUser();

var usertype = res.get('usertype');
var user = res.get('value');
if(usertype=='lender' || usertype=='borrower'){
	lettercount = res.get('letter');
}
