package com.eveb.saasops.batch.bet.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
//会员银行卡
@Table(name = "mbr_bankcard")
public class MbrBankcard implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//自增长Id
	private Integer id;

	//会员Id
	private Integer accountId;

	//银行名称
	private String bankName;

	//银行卡号
	private String cardNo;

	//省
	private String province;

	//市
	private String city;

	//地址
	private String address;

	//开户姓名
	private String realName;

	//1开启, 0禁用
	private Byte available;

	private String createTime;

	//1删除，0未删除
	private Byte isDel;

	private String schemaPrex;

}