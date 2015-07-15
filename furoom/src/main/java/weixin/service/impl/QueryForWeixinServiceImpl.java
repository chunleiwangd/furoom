package weixin.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gpps.dao.IBindingDao;
import gpps.dao.ICardBindingDao;
import gpps.dao.ICashStreamDao;
import gpps.dao.IGovermentOrderDao;
import gpps.dao.ILenderAccountDao;
import gpps.dao.ILenderDao;
import gpps.dao.IPayBackDao;
import gpps.dao.IProductDao;
import gpps.dao.IProductSeriesDao;
import gpps.dao.ISubmitDao;
import gpps.inner.service.IInnerThirdPaySupportService;
import gpps.model.Binding;
import gpps.model.GovermentOrder;
import gpps.model.Lender;
import gpps.model.PayBack;
import gpps.model.Product;
import gpps.model.ProductSeries;
import gpps.model.Submit;
import gpps.service.IAccountService;
import gpps.service.ILenderService;
import gpps.service.ILoginService;
import gpps.service.PayBackDetail;
import weixin.service.IQueryForWeixinService;

@Service
public class QueryForWeixinServiceImpl implements IQueryForWeixinService {
	@Autowired
	ILenderService lenderService;
	
	@Autowired
	ILenderDao lenderDao;
	
	@Autowired
	ILenderAccountDao lenderAccountDao;
	
	@Autowired
	IBindingDao bindingDao;
	
	@Autowired
	ISubmitDao submitDao;
	
	@Autowired
	IProductDao productDao;
	
	@Autowired
	IGovermentOrderDao orderDao;
	@Autowired
	IProductSeriesDao productSeriesDao;
	@Autowired
	IPayBackDao paybackDao;
	@Autowired
	ICashStreamDao cashStreamDao;
	@Autowired
	IAccountService accountService;
	@Autowired
	ICardBindingDao cardBindingDao;
	@Autowired
	IInnerThirdPaySupportService supportService;
	@Override
	public String getMySubmit(String userid) throws Exception {
		List<Binding> bindings = bindingDao.findByTypeAndValueAndStateAndUserId(Binding.TYPE_OPENID, userid, Binding.STATE_VALID, null);
		
		if(bindings==null || bindings.isEmpty()){
			throw new Exception("尚未绑定用户，请先绑定再查询！");
		}
		Lender lender = lenderDao.find(bindings.get(0).getUserid());
		
		List<Integer> states = new ArrayList<Integer>();
		states.add(Product.STATE_REPAYING);
		int count = submitDao.countByLenderAndStateAndProductStatesAndPurchaseFlag(lender.getId(), Submit.STATE_COMPLETEPAY, states, Submit.PURCHASE_FLAG_UNPURCHASE);
		List<Submit> submits = submitDao.findAllByLenderAndStateAndProductStatesAndPurchaseFlagWithPaged(lender.getId(), Submit.STATE_COMPLETEPAY, states, Submit.PURCHASE_FLAG_UNPURCHASE, 0, count);
		BigDecimal sum = BigDecimal.ZERO;
		if(submits!=null)
		for(Submit submit : submits){
			sum = sum.add(submit.getAmount());
		}
		
		int count_apply = submitDao.countByLenderAndStateAndProductStatesAndPurchaseFlag(lender.getId(), Submit.STATE_WAITFORPURCHASEBACK, states, Submit.PURCHASE_FLAG_UNPURCHASE);
		List<Submit> submits_apply = submitDao.findAllByLenderAndStateAndProductStatesAndPurchaseFlagWithPaged(lender.getId(), Submit.STATE_WAITFORPURCHASEBACK, states, Submit.PURCHASE_FLAG_UNPURCHASE, 0, count_apply);
		BigDecimal sum_apply = BigDecimal.ZERO;
		if(submits_apply!=null)
		for(Submit submit : submits_apply){
			sum_apply = sum_apply.add(submit.getAmount());
		}
		
		List<Integer> states_financing = new ArrayList<Integer>();
		states_financing.add(Product.STATE_FINANCING);
		int count_financing = submitDao.countByLenderAndStateAndProductStatesAndPurchaseFlag(lender.getId(), Submit.STATE_COMPLETEPAY, states_financing, Submit.PURCHASE_FLAG_UNPURCHASE);
		List<Submit> submits_financing = submitDao.findAllByLenderAndStateAndProductStatesAndPurchaseFlagWithPaged(lender.getId(), Submit.STATE_COMPLETEPAY, states_financing, Submit.PURCHASE_FLAG_UNPURCHASE, 0, count_financing);
		BigDecimal sum_financing = BigDecimal.ZERO;
		if(submits_financing!=null)
		for(Submit submit : submits_financing){
			sum_financing = sum_financing.add(submit.getAmount());
		}
		
		
		List<Integer> states_done = new ArrayList<Integer>();
		states_done.add(Product.STATE_CLOSE);
		states_done.add(Product.STATE_APPLYTOCLOSE);
		int count_done = submitDao.countByLenderAndStateAndProductStatesAndPurchaseFlag(lender.getId(), Submit.STATE_COMPLETEPAY, states_done, Submit.PURCHASE_FLAG_UNPURCHASE);
		List<Submit> submits_done = submitDao.findAllByLenderAndStateAndProductStatesAndPurchaseFlagWithPaged(lender.getId(), Submit.STATE_COMPLETEPAY, states_done, Submit.PURCHASE_FLAG_UNPURCHASE, 0, count_done);
		BigDecimal sum_done = BigDecimal.ZERO;
		if(submits_done!=null)
		for(Submit submit : submits_done){
			sum_done = sum_done.add(submit.getAmount());
		}
		
		
		StringBuilder sb = new StringBuilder();
		sb.append("尊敬的用户："+lender.getName());
		
		sb.append(",您的投资概况如下：\n");
		
		sb.append("还款中的投资：").append(count).append("笔，总额").append(sum.floatValue()).append("元\n");
		sb.append("融资中已购买：").append(count_financing).append("笔，总额").append(sum_financing.floatValue()).append("元\n");
		sb.append("申请回购投资：").append(count_apply).append("笔，总额").append(sum_apply.floatValue()).append("元\n");
		sb.append("还款完毕的投资：").append(count_done).append("笔，总额").append(sum_done.floatValue()).append("元\n");
		sb.append("\n");
		sb.append("查看详情请<a href='"+supportService.getReturnUrl()+"/weixin_myaccountdetail.html?fid=submit&userid="+userid+"'>点击此处</a>");
		return sb.toString();
	}

	@Override
	public String getMyPayBack(String userid) throws Exception {

		List<Binding> bindings = bindingDao.findByTypeAndValueAndStateAndUserId(Binding.TYPE_OPENID, userid, Binding.STATE_VALID, null);
		
		if(bindings==null || bindings.isEmpty()){
			throw new Exception("尚未绑定用户，请先绑定再查询！");
		}
		Lender lender = lenderDao.find(bindings.get(0).getUserid());
		
		
		StringBuilder sb = new StringBuilder();
		sb.append("尊敬的用户："+lender.getName());
		sb.append(",您的回款概况如下：\n");
		
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		Calendar cal=Calendar.getInstance();
		long endtime=cal.getTimeInMillis();
		cal.add(Calendar.YEAR, -1);
		PayBackDetail detail=cashStreamDao.sumLenderRepayed(lender.getAccountId(), cal.getTimeInMillis(), endtime);
		if(detail==null){
			detail = new PayBackDetail();
		}
		sb.append("今年已回款情况 \n");
		sb.append("本金：").append(detail.getChiefAmount().floatValue())
			.append("，利息：").append(detail.getInterest().floatValue())
			.append("，合计：").append(detail.getChiefAmount().add(detail.getInterest()).floatValue()).append("\n");
		sb.append("-----------\n");
		cal.add(Calendar.MONTH, 6);
		detail=cashStreamDao.sumLenderRepayed(lender.getAccountId(), cal.getTimeInMillis(), endtime);
		if(detail==null){
			detail = new PayBackDetail();
		}
		sb.append("近半年已回款情况\n");
		sb.append("本金：").append(detail.getChiefAmount().floatValue())
			.append("，利息：").append(detail.getInterest().floatValue())
			.append("，合计：").append(detail.getChiefAmount().add(detail.getInterest()).floatValue()).append("\n");
		sb.append("----------\n");
		cal.add(Calendar.MONTH, 3);
		detail=cashStreamDao.sumLenderRepayed(lender.getAccountId(), cal.getTimeInMillis(), endtime);
		if(detail==null){
			detail = new PayBackDetail();
		}
		sb.append("近三个月已回款情况\n");
		sb.append("本金：").append(detail.getChiefAmount().floatValue())
			.append("，利息：").append(detail.getInterest().floatValue())
			.append("，合计：").append(detail.getChiefAmount().add(detail.getInterest()).floatValue()).append("\n");
		sb.append("----------\n");
		cal.add(Calendar.MONTH, 1);
		detail=cashStreamDao.sumLenderRepayed(lender.getAccountId(), cal.getTimeInMillis(), endtime);
		if(detail==null){
			detail = new PayBackDetail();
		}
		sb.append("近两个月已回款情况\n");
		sb.append("本金：").append(detail.getChiefAmount().floatValue())
			.append("，利息：").append(detail.getInterest().floatValue())
			.append("，合计：").append(detail.getChiefAmount().add(detail.getInterest()).floatValue()).append("\n");
		sb.append("----------\n");
		cal.add(Calendar.MONTH, 1);
		detail=cashStreamDao.sumLenderRepayed(lender.getAccountId(), cal.getTimeInMillis(), endtime);
		sb.append("近一个月已回款情况\n");
		sb.append("本金：").append(detail.getChiefAmount().floatValue())
			.append("，利息：").append(detail.getInterest().floatValue())
			.append("，合计：").append(detail.getChiefAmount().add(detail.getInterest()).floatValue()).append("\n");
		sb.append("----------\n");
		sb.append("\n");
		sb.append("查看详情请<a href='"+supportService.getReturnUrl()+"/weixin_myaccountdetail.html?fid=payback&sid=payback-have&userid="+userid+"'>点击此处</a>");
		return sb.toString();
	}
	@Override
	public String getMyPayBackTo(String userid) throws Exception{
		Lender lender = lenderService.getCurrentUser();
		if(lender==null)
		{
			List<Binding> bindings = bindingDao.findByTypeAndValueAndStateAndUserId(Binding.TYPE_OPENID, userid, Binding.STATE_VALID, null);
		
			if(bindings==null || bindings.isEmpty()){
				throw new Exception("尚未绑定用户，请先绑定再查询！");
			}
			lender = lenderDao.find(bindings.get(0).getUserid());
		}
		if(lender.getCardBindingId()!=null)
			lender.setCardBinding(cardBindingDao.find(lender.getCardBindingId()));	
		lenderService.getCurrentSession().setAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER, lender);
		
		Map<String, PayBackDetail> res = accountService.getLenderWillBeRepayedDetail();
		
		PayBackDetail detail = res.get(PayBackDetail.ONEMONTH);
		StringBuilder sb = new StringBuilder();
		sb.append("尊敬的用户："+lender.getName());
		sb.append(",您的待回款概况如下：\n");
		
		sb.append("本月内待回款情况\n");
		sb.append("本金：").append(detail.getChiefAmount().floatValue())
			.append("，利息：").append(detail.getInterest().floatValue())
			.append("，合计：").append(detail.getChiefAmount().add(detail.getInterest()).floatValue()).append("\n");
		sb.append("----------\n");
		
		detail = res.get(PayBackDetail.TWOMONTH);
		sb.append("两个月内待回款情况\n");
		sb.append("本金：").append(detail.getChiefAmount().floatValue())
		.append("，利息：").append(detail.getInterest().floatValue())
		.append("，合计：").append(detail.getChiefAmount().add(detail.getInterest()).floatValue()).append("\n");
		sb.append("----------\n");
		
		detail = res.get(PayBackDetail.THREEMONTH);
		sb.append("三个月内待回款情况\n");
		sb.append("本金：").append(detail.getChiefAmount().floatValue())
		.append("，利息：").append(detail.getInterest().floatValue())
		.append("，合计：").append(detail.getChiefAmount().add(detail.getInterest()).floatValue()).append("\n");
		sb.append("----------\n");
		
		detail = res.get(PayBackDetail.HALFYEAR);
		sb.append("半年内待回款情况 \n");
		sb.append("本金：").append(detail.getChiefAmount().floatValue())
		.append("，利息：").append(detail.getInterest().floatValue())
		.append("，合计：").append(detail.getChiefAmount().add(detail.getInterest()).floatValue()).append("\n");
		sb.append("----------\n");
		
		detail = res.get(PayBackDetail.ONEYEAR);
		sb.append("一年内待回款情况\n");
		sb.append("本金：").append(detail.getChiefAmount().floatValue())
		.append("，利息：").append(detail.getInterest().floatValue())
		.append("，合计：").append(detail.getChiefAmount().add(detail.getInterest()).floatValue()).append("\n");
		sb.append("----------\n");
		
		sb.append("\n");
		sb.append("查看详情请<a href='"+supportService.getReturnUrl()+"/weixin_myaccountdetail.html?fid=payback&sid=payback-to&userid="+userid+"'>点击此处</a>");
		return sb.toString();
	}

	@Override
	public String getProductToBuy() throws Exception {
		return "今天是个好天气";
	}
	
	private List<Submit> findSubmits(List<Submit> submits,Integer productId)
	{
		if(submits==null||submits.size()==0)
			return null;
		List<Submit> list=new ArrayList<Submit>();
		for(Submit submit:submits)
		{
			if((int)productId==submit.getProductId())
				list.add(submit);
		}
		return list;
	}

}
