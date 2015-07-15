package weixin.service.impl;

import gpps.dao.IBindingDao;
import gpps.dao.ICardBindingDao;
import gpps.dao.ILenderAccountDao;
import gpps.dao.ILenderDao;
import gpps.dao.ILetterDao;
import gpps.model.Admin;
import gpps.model.Binding;
import gpps.model.Borrower;
import gpps.model.Lender;
import gpps.model.LenderAccount;
import gpps.model.Letter;
import gpps.service.ILenderService;
import gpps.service.ILoginService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import weixin.service.IBindService;


@Service
public class BindServiceImpl implements IBindService {
	
	@Autowired
	ILenderService lenderService;
	
	@Autowired
	ILenderDao lenderDao;
	
	@Autowired
	ILenderAccountDao lenderAccountDao;
	
	@Autowired
	IBindingDao bindingDao;
	
	@Autowired
	ILetterDao letterDao;
	@Autowired
	ICardBindingDao cardBindingDao;
	
	private static Logger log = Logger.getLogger(BindServiceImpl.class);
	
	@Override
	public void bind(String username, String password, String userid, String validate) throws Exception {
		
		List<Binding> bindings = bindingDao.findByTypeAndValueAndStateAndUserId(Binding.TYPE_OPENID, userid, Binding.STATE_VALID, null);
		
		if(bindings!=null && !bindings.isEmpty()){
			throw new Exception("您已经绑定账户，重新绑定请先解绑！");
		}
		
		lenderService.validateAndLogin(username, password, validate);
		
		
		Lender lender = lenderDao.findByLoginId(username);
		
		List<Binding> invalidBindings = bindingDao.findByTypeAndValueAndStateAndUserId(Binding.TYPE_OPENID, userid, Binding.STATE_INVALID, lender.getId());
		
		Binding bind = null;
		if(invalidBindings!=null && !invalidBindings.isEmpty()){
			bind = invalidBindings.get(0);
			bindingDao.changeState(bind.getId(), Binding.STATE_VALID);
		}else{
			bind = new Binding();
			bind.setBtype(Binding.TYPE_OPENID);
			bind.setCreatetime(System.currentTimeMillis());
			bind.setExpiredtime(0);
			bind.setState(Binding.STATE_VALID);
			bind.setTvalue(userid);
			bind.setUserid(lender.getId());
			bindingDao.create(bind);
		}
		if(lender.getCardBindingId()!=null)
			lender.setCardBinding(cardBindingDao.find(lender.getCardBindingId()));	
		lenderService.getCurrentSession().setAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER, lender);
	}
	
	
	@Override
	public String getAccountMessage(String userid) throws Exception{
		List<Binding> bindings = bindingDao.findByTypeAndValueAndStateAndUserId(Binding.TYPE_OPENID, userid, Binding.STATE_VALID, null);
		
		if(bindings==null || bindings.isEmpty()){
			throw new Exception("尚未绑定用户，请先绑定再查询！");
		}
		Lender lender = lenderDao.find(bindings.get(0).getUserid());
		LenderAccount lenderAccount = lenderAccountDao.find(lender.getAccountId());
		StringBuilder sb = new StringBuilder();
		sb.append("尊敬的用户："+lender.getName()).append(",您的账户信息如下：\n");
		sb.append("总金额：").append(lenderAccount.getTotal().floatValue()).append("\n");
		sb.append("可用金额：").append(lenderAccount.getUsable().floatValue()).append("\n");
		sb.append("已投金额：").append(lenderAccount.getUsed().floatValue()).append("\n");
		sb.append("冻结金额：").append(lenderAccount.getFreeze().floatValue()).append("\n");
		sb.append("已获利息：").append(lenderAccount.getTotalincome().floatValue()).append("\n");
		sb.append("\n");
		sb.append("回复以下数字快速查询:").append("\n");
		sb.append("\n");
		sb.append("[1] 我的投资").append("\n");
		sb.append("[2] 我的已还款").append("\n");
		sb.append("[3] 我的待还款").append("\n");
		sb.append("[4] 关于我们").append("\n");
		return sb.toString();
	}
	
	@Override
	public boolean isBind(String userid){
		
		List<Binding> bindings = bindingDao.findByTypeAndValueAndStateAndUserId(Binding.TYPE_OPENID, userid, Binding.STATE_VALID, null);
		
		if(bindings==null || bindings.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public void unbind(String userid){
		
		lenderService.getCurrentSession().removeAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER);
		
		List<Binding> bindings = bindingDao.findByTypeAndValueAndStateAndUserId(Binding.TYPE_OPENID, userid, Binding.STATE_VALID, null);
		
		if(bindings==null || bindings.isEmpty()){
			return;
		}
		
		for(Binding binding : bindings)
		{
			bindingDao.changeState(binding.getId(), Binding.STATE_INVALID);
		}
	}
	
	
	@Override
	public Integer getCurrentUserInner(String userid) throws Exception{
		
		HttpSession session=lenderService.getCurrentSession();
		if(session==null)
			return null;
		Object user=session.getAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER);
		
		if(user!=null && user instanceof Lender){
			return ((Lender)user).getId();
			
		}else{
			List<Binding> bindings = bindingDao.findByTypeAndValueAndStateAndUserId(Binding.TYPE_OPENID, userid, Binding.STATE_VALID, null);
			
			if(bindings==null || bindings.isEmpty()){
				log.info("非法的参数【openId="+userid+"】，获取用户不正确");
				throw new Exception("非法的参数，获取用户不正确");
			}
			user = lenderDao.find(bindings.get(0).getUserid());
			if(user==null){
				log.info("非法的参数【lenderId="+bindings.get(0).getUserid()+"】，获取用户不正确");
				throw new Exception("非法的参数，获取用户不正确");
			}
			Lender lender = (Lender)user;
			if(lender.getCardBindingId()!=null)
				lender.setCardBinding(cardBindingDao.find(lender.getCardBindingId()));	
			session.setAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER, lender);
			return lender.getId();
		}
	}
	
	@Override
	public Map<String, Object> getCurrentUser(String userid) throws Exception{
		Map<String, Object> res = new HashMap<String, Object>();
		
		HttpSession session=lenderService.getCurrentSession();
		if(session==null)
			return null;
		Object user=session.getAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER);
		
		if(user!=null && user instanceof Lender){
			res.put("usertype", "lender");
			res.put("letter", letterDao.countByReceiver(Letter.MARKREAD_NO, Letter.RECEIVERTYPE_LENDER, ((Lender)user).getId()));
			
		}else{
			List<Binding> bindings = bindingDao.findByTypeAndValueAndStateAndUserId(Binding.TYPE_OPENID, userid, Binding.STATE_VALID, null);
			
			if(bindings==null || bindings.isEmpty()){
				log.info("用户尚未绑定【openId="+userid+"】，获取不成功！");
				return null;
			}
			user = lenderDao.find(bindings.get(0).getUserid());
			if(user==null){
				
				log.info("用户绑定不正确【lenderId="+bindings.get(0).getUserid()+"】，获取不成功！");
				return null;
			}
			Lender lender = (Lender)user;
			if(lender.getCardBindingId()!=null)
				lender.setCardBinding(cardBindingDao.find(lender.getCardBindingId()));	
			session.setAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER, lender);
			res.put("usertype", "lender");
			res.put("letter", letterDao.countByReceiver(Letter.MARKREAD_NO, Letter.RECEIVERTYPE_LENDER, lender.getId()));
		}
		res.put("value", user);
		return res;
	}

}
