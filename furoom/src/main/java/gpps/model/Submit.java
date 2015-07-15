package gpps.model;

import java.math.BigDecimal;

public class Submit {
	//抢占的额度有效时间为10分钟，10分钟内未支付成功，则将额度回退
	public static final long PAYEXPIREDTIME=10L*60*1000;
	
	//用户购买债权有效支付时间为30分钟，30分钟内未支付成功，则将状态回滚
	public static final long PURCHASEEXPIREDTIME=30L*60*1000;
	
	
	//预约审核通过的额度有效时间为12小时，12小时内未支付成功，则将额度回退
	public static final long SUBSCRIBE_PAYEXPIREDTIME = 12L*3600*1000;
	
	
	private Integer id;
	/**
//	 * 1:申请竞标-> 2:待支付 （支付） 4:竞标中 (融资审核成功) 8:还款中 16：还款完毕
//	 * 
//	 * 32:流标(融资审核不成功) 64:退订（未支付） 128：异常（额度不足）申请不成功
//	 * 
//	 * 已购买;流标;
//	 * 其他状态从产品中获取
//	 * 
	 * 1:待付款;2:购买成功;4:退订;8:流标;16预约审核通过待付款
	 */
	public static final int STATE_WAITFORPAY=1;//1
	public static final int STATE_COMPLETEPAY=1<<1;//2
	public static final int STATE_UNSUBSCRIBE=1<<2;//4
	public static final int STATE_FAILBIDDING=1<<3;//8
	public static final int STATE_SUBSCRIBE_WAITFORPAY=1<<4;//16
	
	public static final int STATE_WAITFORPURCHASEBACK=1<<5;//32,待回购(回购处理中)
	public static final int STATE_PURCHASEBACKDONE=1<<6;//64，已回购
	
	public static final int STATE_WAITFORPURCHASE=1<<7;//128，客户二手市场上购买标的时待支付
	public static final int STATE_PURCHASEDONE=1<<8; //256,二手购买成功
	
	
	public static final int PURCHASE_FLAG_UNPURCHASE = 0; //未被企业回购
	public static final int PURCHASE_FLAG_PURCHASEBACK = 1;  //已被企业回购
	
	
	public static final int HANDLE_FLAG_NONE = 0; //既不可以被买入又不可以被回购
	public static final int HANDLE_FLAG_PURCHASE = 1;  //可以被买入
	public static final int HANDLE_FLAG_PURCHASEBACK = 2; //可以被回购
	
	private int state=STATE_WAITFORPAY;
	private long createtime = System.currentTimeMillis();
	private long holdingstarttime = 0;        //每次用户在二手市场购买标的后，记录下用户持有该标的的起始时间

	private Integer lenderId;
	private Integer borrowerId;

	private Integer productId;
	private long lastmodifytime = System.currentTimeMillis();
	private BigDecimal amount = BigDecimal.ZERO;
	
	private int purchaseFlag=PURCHASE_FLAG_UNPURCHASE;
	
	public int getPurchaseFlag() {
		return purchaseFlag;
	}
	public void setPurchaseFlag(int purchaseFlag) {
		this.purchaseFlag = purchaseFlag;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
	public Integer getLenderId() {
		return lenderId;
	}
	public void setLenderId(Integer lenderId) {
		this.lenderId = lenderId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public long getLastmodifytime() {
		return lastmodifytime;
	}
	public void setLastmodifytime(long lastmodifytime) {
		this.lastmodifytime = lastmodifytime;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public Integer getBorrowerId() {
		return borrowerId;
	}
	public void setBorrowerId(Integer borrowerId) {
		this.borrowerId = borrowerId;
	}
	public long getHoldingstarttime() {
		return holdingstarttime;
	}
	public void setHoldingstarttime(long holdingstarttime) {
		this.holdingstarttime = holdingstarttime;
	}
	
	//辅助对象
	private Product product;
	private BigDecimal repayedAmount=new BigDecimal(0);//已还款
	private BigDecimal waitforRepayAmount=new BigDecimal(0);//待回款
	private long payExpiredTime;
	private String lenderName;
	
	private int handleFlag=HANDLE_FLAG_NONE; //是否可以执行回购
	
	public int getHandleFlag() {
		return handleFlag;
	}
	public void setHandleFlag(int handleFlag) {
		this.handleFlag = handleFlag;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public BigDecimal getRepayedAmount() {
		return repayedAmount;
	}
	public void setRepayedAmount(BigDecimal repayedAmount) {
		this.repayedAmount = repayedAmount;
	}
	public long getPayExpiredTime() {
		return payExpiredTime;
	}
	public void setPayExpiredTime(long payExpiredTime) {
		this.payExpiredTime = payExpiredTime;
	}
	public String getLenderName() {
		return lenderName;
	}
	public void setLenderName(String lenderName) {
		this.lenderName = lenderName;
	}
	public BigDecimal getWaitforRepayAmount() {
		return waitforRepayAmount;
	}
	public void setWaitforRepayAmount(BigDecimal waitforRepayAmount) {
		this.waitforRepayAmount = waitforRepayAmount;
	}
}
