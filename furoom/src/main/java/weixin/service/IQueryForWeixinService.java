package weixin.service;

public interface IQueryForWeixinService {
	public String getMySubmit(String userid) throws Exception;
	public String getMyPayBack(String userid) throws Exception;
	public String getMyPayBackTo(String userid) throws Exception;
	public String getProductToBuy() throws Exception;
}
