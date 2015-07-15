package gpps.service.impl;

import gpps.dao.IInviteDao;
import gpps.dao.ILenderAccountDao;
import gpps.dao.ILenderDao;
import gpps.model.Invite;
import gpps.model.Lender;
import gpps.model.LenderAccount;
import gpps.service.IInviteService;
import gpps.service.ILenderService;
import gpps.service.exception.InviteException;
import gpps.service.message.ILetterSendService;
import gpps.service.message.IMessageService;
import gpps.service.message.IMessageSupportService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InviteServiceImpl implements IInviteService {
@Autowired
IInviteDao inviteDao;
@Autowired
ILenderDao lenderDao;
@Autowired
ILenderAccountDao lenderAccountDao;
@Autowired
IMessageSupportService messageService;
@Autowired
ILetterSendService letterService;
@Autowired
ILenderService lenderService;

	@Override
	public List<String> allocateByLender(int number) throws InviteException{
		Lender lender = lenderService.getCurrentUser();
		if(lender==null){
			throw new InviteException("没有权限申请，请先登录！");
		}
		List<Invite> invites = inviteDao.queryByAttriToAndBatchCodeAndState(lender.getId(), currentBatchCode, Invite.STATE_INIT);
		if(invites.size()>=MAX_REMAIN_UNREGISTERED_NUMBER){
			throw new InviteException("本期您的剩余未注册邀请码数量已经超过系统允许的"+MAX_REMAIN_UNREGISTERED_NUMBER+"个");
		}
		
		int applyNum = MAX_REMAIN_UNREGISTERED_NUMBER - invites.size();
		if(applyNum>number){
			applyNum = number;
		}
		List<String> res = allocate(lender.getId(), applyNum);
		return res;
	}

	@Override
	public List<String> allocate(Integer lenderId, int number)
			throws InviteException {
		if(number<=0 || number>MAX_ALLOC_NUMBER){
			throw new InviteException("分配的数量有问题，必须是大于0小于等于"+MAX_ALLOC_NUMBER+"的值！");
		}
		Lender lender = lenderDao.find(lenderId);
		if(lender==null){
			throw new InviteException("用户不存在！");
		}
		
		int maxValue = 0;
		List<String> res = new ArrayList<String>();
		
		Integer id = inviteDao.getMaxId();
		
		if(id!=null){
		Invite invite = inviteDao.find(id);
		 maxValue = Integer.parseInt(invite.getCode());
		}
		String name = (lender.getName()==null || lender.getName().equals(""))?lender.getLoginId():lender.getName();
		String message = "【春蕾政采贷】尊敬的"+name+"，您将获得"+number+"个邀请码：";
		for(int i=0; i<number; i++){
			Random ran = new Random();
			maxValue = maxValue+ran.nextInt(100)+1;
			
			String ncode = maxValue+"";
			Invite inv = new Invite();
			inv.setAttributeTo(lenderId);
			inv.setCode(ncode);
			inv.setState(Invite.STATE_INIT);
			inv.setBatchCode(currentBatchCode);
			res.add(ncode);
			inviteDao.create(inv);
			
			message += (ncode+", ");
		}
		
		message += "邀请码详细规则请参见"+IMessageService.WEBADDR;
		
		
		letterService.sendMessage(ILetterSendService.USERTYPE_LENDER, lenderId, "邀请码发放", message);
		
		return res;
	}

	@Override
	public List<Invite> getUnRegistered(Integer lenderId)
			throws InviteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Invite> getRegistered(Integer lenderId) throws InviteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void check(String code) throws InviteException {
		Invite invite = inviteDao.findByCode(code);
		if(invite==null){
			throw new InviteException("无效的邀请码！");
		}
		if(invite.getState()==Invite.STATE_REGISTERED){
			throw new InviteException("本邀请码已经被成功注册，无法再次注册！");
		}else if(invite.getState()==Invite.STATE_HANDLEING){
			throw new InviteException("本邀请码已被锁定，正在注册中！");
		}
		
		inviteDao.update(code, null, Invite.STATE_HANDLEING);
	}

	@Override
	public void register(String code, Integer lenderId) throws InviteException {
		Invite invite = inviteDao.findByCode(code);
		if(invite==null){
			throw new InviteException("无效的邀请码！");
		}
		if(invite.getState()==Invite.STATE_REGISTERED){
			throw new InviteException("本邀请码已经被成功注册，无法再次注册！");
		}
		inviteDao.update(code, lenderId, Invite.STATE_REGISTERED);
	}

	@Override
	public void release(String code) throws InviteException {
		Invite invite = inviteDao.findByCode(code);
		if(invite==null){
			throw new InviteException("无效的邀请码！");
		}
		if(invite.getState()==Invite.STATE_REGISTERED){
			throw new InviteException("本邀请码已经被成功注册，无法释放！");
		}
		inviteDao.update(code, null, Invite.STATE_INIT);
	}
	
	@Override
	public List<Invite> queryByAttriToAndBatchCode(Integer lenderId, Integer batchCode){
		List<Invite> res = inviteDao.queryByAttriToAndBatchCode(lenderId, batchCode);
		if(res==null){
			res= new ArrayList<Invite>();
		}
		for(Invite re : res){
			Integer rid = re.getRegisterBy();
			if(rid!=null)
			{
				LenderAccount la = lenderAccountDao.find(lenderDao.find(rid).getAccountId());
				if(la.getUsed().add(la.getFreeze()).add(la.getTotalincome()).compareTo(BigDecimal.ZERO)>0)
				{
					re.setState(Invite.STATE_BUY_DONE);
				}
			}
		}
		return res;
	}

}
