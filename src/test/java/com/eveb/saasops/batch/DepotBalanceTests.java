package com.eveb.saasops.batch;

import com.eveb.saasops.batch.bet.scheduled.*;
import com.eveb.saasops.batch.bet.service.ValidBetService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class  DepotBalanceTests {


	@Autowired
	private ValidBetService validBetService;
	@Autowired
	private ValidBetJobHandler validBetJobHandler;
	@Autowired
	NoPayFundDepositHandler noPayFundDepositHandler;

	@Test
	public void NoPayFundDepositHandler() throws Exception {
		validBetService.castMbrValidbet("ybh");
	}

	@Test
	public void balanceJobHandler() {
//		try {
//			validBetJobHandler.execute(null);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
	}

	@Test
	public void queryBalanceJobHandler() {
//		try {
//			queryBalanceJobHandler.execute(null);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
	}
}
