package com.eveb.saasops.batch;

import com.eveb.saasops.batch.bet.service.ValidBetService;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BatchApplicationTests {

	@Autowired
	private ValidBetService validBetService;


	@Test
	public void getValidVet() {
		System.out.println(new Gson().toJson(validBetService.getValidVet("df",null,
				null,null, Lists.newArrayList("nhhmmm1"),
				null))+"*******************************");
	}

	@Test
	public void contextLoads() {
		validBetService.castMbrValidbet("df");
	}

}
