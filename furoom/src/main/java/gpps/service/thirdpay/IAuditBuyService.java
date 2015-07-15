package gpps.service.thirdpay;

import java.security.SignatureException;
import java.util.List;
import java.util.Map;

public interface IAuditBuyService {
	/**
	 * 后台审核投标服务
	 * @param loanNos 所有流水号用英文逗号(,)连成一个字符串
	 * @param auditType 1.通过
						2.退回
						3.二次分配同意
						4.二次分配不同意
						5.提现通过
						6.提现退回
	 */
	public void auditBuy(List<String> loanNos,int auditType) throws Exception;
	
	/**
	 * 单纯的给第三方发送审核服务（通过、退回），不执行后续的操作auditBuyProcessor
	 * @param loanNos 所有流水号用英文逗号(,)连成一个字符串
	 * @param auditType 1.通过
						2.退回
						3.二次分配同意
						4.二次分配不同意
						5.提现通过
						6.提现退回
	 */
	public void justAuditBuy(List<String> loanNos, int auditType) throws Exception;
	
	
	
	/**
	 * 处理对于购买审核的返回信息，修改平台上相应的实体状体与账户额度，维护与第三方的一致性
	 * 并根据是否需要发送短信与站内信
	 * 
	 * @param retJson 第三方返回的JSON格式的结果参数
	 * 
	 * 
	 * */
	public void auditBuyProcessor(Map<String, String> returnParams) throws AlreadyDoneException, ResultCodeException, SignatureException, Exception;
	
	/**
	 * 审核通过的购买转账流水的后续处理，专门供系统管理员使用的
	 * 
	 * @param loanNos
	 * 
	 * */
	public void buyAuditSuccessHandle(List<String> loanNos) throws Exception;
}
