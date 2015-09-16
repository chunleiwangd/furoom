package gpps.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gpps.dao.IBorrowerDao;
import gpps.dao.ICashStreamDao;
import gpps.dao.ILenderDao;
import gpps.model.Borrower;
import gpps.model.CashStream;
import gpps.model.GovermentOrder;
import gpps.model.Lender;
import gpps.model.Product;
import gpps.service.IAccountService;
import gpps.service.IProductService;
import gpps.service.ITempDebtService;
import gpps.service.thirdpay.ITransferApplyService;
import gpps.service.thirdpay.LoanFromTP;
import gpps.service.thirdpay.Transfer.LoanJson;

@Service
public class TempDebtServiceImpl implements ITempDebtService {

	@Autowired
	IProductService productService;
	@Autowired
	IAccountService accountService;
	@Autowired
	IBorrowerDao borrowerDao;
	@Autowired
	ILenderDao lenderDao;
	@Autowired
	ITransferApplyService transferService;
	@Autowired
	ICashStreamDao cashstreamDao;
	@Override
	public void returnMoney(Integer productId, Integer userId) throws Exception {
		Product product = productService.find(productId);
		if(product==null || product.getState()!=Product.STATE_REPAYING){
			throw new Exception("产品状态不正确！");
		}
		GovermentOrder order = product.getGovermentOrder();
		if(order==null){
			throw new Exception("未找到对应的订单");
		}
		
		Lender lender = lenderDao.find(userId);
		if(lender==null){
			throw new Exception("未找到对应的出借方");
		}
		
		Borrower borrower = borrowerDao.find(order.getBorrowerId());
		if(borrower==null){
			throw new Exception("产品对应的借款方不存在");
		}
		
		synchronized(this){
			
			int count = cashstreamDao.countByActionAndStateAndDescription(CashStream.ACTION_TEMPDEBT, CashStream.STATE_SUCCESS, String.valueOf(product.getId()));
			
			if(count>0){
				return;
			}
			
			Integer csId = accountService.returnMoneyForTempDebt(lender.getAccountId(), borrower.getAccountId(), product.getRealAmount(), String.valueOf(product.getId()));				
			List<LoanJson> loanJsons=new ArrayList<LoanJson>();
		
			String toMoneyMoreMore = lender.getThirdPartyAccount();
			
			LoanJson loadJson=new LoanJson();
			loadJson.setLoanOutMoneymoremore(borrower.getThirdPartyAccount());
			loadJson.setLoanInMoneymoremore(toMoneyMoreMore);
			loadJson.setOrderNo(String.valueOf(csId));
			loadJson.setBatchNo(String.valueOf(product.getId()));
			loadJson.setAmount(product.getRealAmount().toString());
			loanJsons.add(loadJson);
		
			//无需审核直接转账
			List<LoanFromTP> loans = transferService.justTransferApplyNoNeedAudit(loanJsons);
		
			LoanFromTP loan = loans.get(0);
		
			cashstreamDao.updateLoanNo(csId, loan.getLoanNo(), null);
			accountService.changeCashStreamState(csId, CashStream.STATE_SUCCESS);
		}
	}

}
