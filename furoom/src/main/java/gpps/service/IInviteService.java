package gpps.service;

import gpps.model.Invite;
import gpps.service.exception.InviteException;

import java.util.List;

public interface IInviteService {
	public static final int MAX_ALLOC_NUMBER = 10;
	public static final int MAX_REMAIN_UNREGISTERED_NUMBER = 10;
	
	
	public Integer currentBatchCode = null;
	
	/**
	 * 管理员使用，管理员给用户分配邀请码
	 * 
	 * */
	public List<String> allocate(Integer lenderId, int number) throws InviteException;
	public List<Invite> getUnRegistered(Integer lenderId) throws InviteException;
	public List<Invite> getRegistered(Integer lenderId) throws InviteException;
	public void check(String code) throws InviteException;
	public void register(String code, Integer lenderId) throws InviteException;
	public void release(String code) throws InviteException;
	public List<Invite> queryByAttriToAndBatchCode(Integer lenderId, Integer batchCode);
	
	/**
	 * 普通用户使用，用户自己来申请邀请码
	 * 
	 * */
	public List<String> allocateByLender(int number) throws InviteException;
}
