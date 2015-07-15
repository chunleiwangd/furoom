package gpps.model;

public class Binding {
	public static final long EXPIRED_PERIOD = 60L*24*3600*1000;
	
	public static final int TYPE_OPENID = 0; //绑定类型为openId,主要用于微信及app绑定，直接将用户名跟应用所得的openID进行绑定
	public static final int TYPE_TOKEN = 1;  //绑定类型为token,主要用于支持H5的网页本地保存的token
	public static final int STATE_VALID = 0;	//本条绑定状态为有效
	public static final int STATE_INVALID = 1;	//本条绑定状态为失效
	Integer id;
	int btype=TYPE_TOKEN; //默认为token
	String tvalue;
	long createtime;
	long expiredtime;
	Integer userid;
	int state=STATE_VALID;  //默认为有效
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getBtype() {
		return btype;
	}
	public void setBtype(int btype) {
		this.btype = btype;
	}
	public String getTvalue() {
		return tvalue;
	}
	public void setTvalue(String tvalue) {
		this.tvalue = tvalue;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
	public long getExpiredtime() {
		return expiredtime;
	}
	public void setExpiredtime(long expiredtime) {
		this.expiredtime = expiredtime;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
}
