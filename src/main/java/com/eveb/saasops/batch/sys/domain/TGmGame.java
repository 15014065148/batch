package com.eveb.saasops.batch.sys.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Setter
@Getter
@Table(name = "t_gm_game")
public class TGmGame implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer catId;

    private Integer subCatId;

    private Integer depotId;

    private String gameCode;

    private String gameName;

    private String gameTag;

    private String gameParam;

    private Byte available;

    private String logo;

    private Byte enablePc;

    private Byte enableMb;

    private Byte enableTest;

    private Byte enableHot;

    private Byte ebableNew;

    private Byte enablePool;

    private String memo;

    private String createUser;

    private String createTime;

    private String modifyUser;

    private String modifyTime;

    private Integer compensateNum;

    private String gameNameEn;

    private String gameId;

    private String url;

    private String pcUrlTag;

    private String htmlTag;

    private Integer recRating;

    private String poolCat;

    private String poolParam;

    private Byte topLink;

    private Integer sortId;

    private Integer clickNum;

    private Integer monthPer;

    private Integer lastdayPer;

    private String catName;

    private String depotName;
}