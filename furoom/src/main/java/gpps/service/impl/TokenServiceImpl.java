package gpps.service.impl;

import gpps.dao.IBindingDao;
import gpps.dao.ILenderDao;
import gpps.model.Binding;
import gpps.model.Lender;
import gpps.service.ILoginService;
import gpps.token.service.ITokenService;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
@Service
public class TokenServiceImpl implements ITokenService {
	@Autowired
	IBindingDao bindingDao;
	@Autowired
	ILenderDao lenderDao;
	@Override
	public boolean isValid(String token) {
		List<Binding> bindings = bindingDao.findByTypeAndValueAndStateAndUserId(Binding.TYPE_TOKEN, token, -1, null);
		if(bindings==null || bindings.isEmpty()){
			return false;
		}else{
			Binding binding = bindings.get(0);
			if(System.currentTimeMillis() >= binding.getExpiredtime()){
				return false;
			}else{
				return true;
			}
		}
	}

	@Override
	public void login(String token, String loginId) throws Exception {
		if(!isValid(token)){
			throw new Exception("授权码已经失效！");
		}
		Lender lender = lenderDao.findByLoginId(loginId);
		if(lender==null){
			throw new Exception("用户登录名错误！");
		}
		List<Binding> bindings = bindingDao.findByTypeAndValueAndStateAndUserId(Binding.TYPE_TOKEN, token, -1, lender.getId());
		if(bindings==null || bindings.isEmpty()){
			throw new Exception("登录失败，无权限！");
		}
		
		 HttpSession session=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		 session.setAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER, lender);
	}

	@Override
	public String createToken(Integer lenderId){
		Binding binding = new Binding();
		binding.setBtype(Binding.TYPE_TOKEN);
		binding.setCreatetime(System.currentTimeMillis());
		binding.setExpiredtime(binding.getCreatetime()+Binding.EXPIRED_PERIOD);
		binding.setState(Binding.STATE_VALID);
		
		 UUID uuid = UUID.randomUUID();
		
		binding.setTvalue(uuid.toString());
		binding.setUserid(lenderId);
		bindingDao.create(binding);
		return binding.getTvalue();
	}

}
