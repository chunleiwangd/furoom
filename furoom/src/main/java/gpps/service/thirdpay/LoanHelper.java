package gpps.service.thirdpay;

import gpps.service.exception.CheckException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class LoanHelper {
	public static List<LoanFromTP> parseJSON(String json, String action) throws CheckException{
		List<LoanFromTP> loans = new ArrayList<LoanFromTP>();
		Gson gson = new Gson();
		List returnParams=gson.fromJson(json, List.class);
		if(returnParams==null || returnParams.isEmpty()){
			throw new CheckException("找不到对应的转账信息");
		}
		for(Object obj : returnParams){
			Map<String, Object> temp = (Map<String, Object>)obj;
			LoanFromTP loan = null;
			if(action==null)
			{
				//ACTION为空代表要查询的是转账信息
				loan = new TransferFromTP(temp);
			}else if("1".equals(action)){
				//ACTION为1代表要查询的是充值信息
				loan = new RechargeFromTP(temp);
			}else if("2".equals(action)){
				//ACTION为2代表要查询的是提现信息
				loan = new WithDrawFromTP(temp);
			}
			loans.add(loan);
		}
		return loans;
	}
	
	public static void main(String args[]) throws Exception{
		List<LoanFromTP> tps = parseJSON(null, null);
		System.out.println(tps);
	}
}
