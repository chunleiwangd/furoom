package test.test1;

import java.util.Date;
import java.util.List;

import gpps.model.PayBack;
import gpps.tools.PayBackCalculateUtils;

public class TestPayBackCalculate {
	public static void main(String args[]) throws Exception{
		Date date = new Date("2015/8/30");
		Date dateto = new Date("2016/2/29");
		List<PayBack> pbs = PayBackCalculateUtils.calclatePayBacksForXXHB(500000, 0.19, date.getTime(), dateto.getTime());
		for(PayBack pb : pbs){
			System.out.println((new Date(pb.getDeadline())).toLocaleString()+"---"+pb.getChiefAmount().floatValue()+"---"+pb.getInterest().floatValue());
		}
	}
}
