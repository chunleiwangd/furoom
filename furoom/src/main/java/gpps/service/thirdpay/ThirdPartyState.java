package gpps.service.thirdpay;

public class ThirdPartyState {
	/**
	 * 操作类型：手动
	 * 
	 * */
	public static final String THIRD_ACTION_MANUAL = "1";
	/**
	 * 操作类型：自动
	 * 
	 * */
	public static final String THIRD_ACTION_AUTO = "2";
	
	
	/**
	 * 转账类型：投标
	 * 
	 * */
	public static final String THIRD_TRANSFERACTION_BUY = "1";
	/**
	 * 转账类型：还款
	 * 
	 * */
	public static final String THIRD_TRANSFERACTION_REPAY = "2";
	
	
	/**
	 * 转账方式：桥连
	 * 
	 * */
	public static final String THIRD_TRANSFERTYPE_BRIDEG = "1";
	/**
	 * 转账方式：直连
	 * 
	 * */
	public static final String THIRD_TRANSFERTYPE_DIRECT = "2";
	
	/**
	 * 转账状态：未转账
	 * 
	 * */
	public static final String THIRD_TRANSFERSTATE_NOT = "0";
	/**
	 * 转账状态：已转账
	 * 
	 * */
	public static final String THIRD_TRANSFERSTATE_DONE = "1";
	
	/**
	 * 操作状态:未操作
	 * 
	 * */
	public static final String THIRD_ACTSTATE_NOTOPERATE = "0";
	/**
	 * 操作状态:已通过
	 * 
	 * */
	public static final String THIRD_ACTSTATE_PASS = "1";
	/**
	 * 操作状态:已退回
	 * 
	 * */
	public static final String THIRD_ACTSTATE_RETURN = "2";
	/**
	 * 操作状态:自动通过
	 * 
	 * */
	public static final String THIRD_ACTSTATE_AUTOPASS = "3";
	
	
	/**
	 * 审核类型：通过
	 * 
	 * */
	public static final int THIRD_AUDITTYPE_PASS = 1;
	/**
	 * 审核类型：拒绝
	 * 
	 * */
	public static final int THIRD_AUDITTYPE_RETURN = 2;
	
	
	
	
	
	
	public static final int LOAN_STATE_TRANSFER_APPLY = 1;
	public static final int LOAN_STATE_TRANSFER_AUDIT_PASS = 2;
	public static final int LOAN_STATE_TRANSFER_AUDIT_RETURN = 3;
	public static final int LOAN_STATE_RECHARGE = 4;
	
	
	/**
	 * 判断资金转账是否仅仅申请，尚未审核
	 * 
	 * */
	public static boolean isTransferApply(TransferFromTP loan){
		//转账状态为“1”（已转账），并且操作状态为“0”（未操作）
		return THIRD_TRANSFERSTATE_DONE.equals(loan.getTransferState()) && THIRD_ACTSTATE_NOTOPERATE.equals(loan.getActState());
	}
	
}
