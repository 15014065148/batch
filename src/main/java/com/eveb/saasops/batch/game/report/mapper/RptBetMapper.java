package com.eveb.saasops.batch.game.report.mapper;

import com.eveb.saasops.batch.game.report.domain.GameCode;
import com.eveb.saasops.batch.game.report.domain.RptBetDayModel;
import com.eveb.saasops.batch.game.report.domain.RptBetModel;
import com.eveb.saasops.batch.game.report.domain.TGmApi;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface RptBetMapper {

    int insertBBINDayRptBet(@Param("schemaPrex") String schemaPrex,@Param("rpt") RptBetDayModel rpt);

    void insertBBINDayRptBetList(@Param("schemaPrex") String schemaPrex,@Param("bets") List<RptBetDayModel> bets);

    @Select("select * from rpt_bet_rcd where id = #{id} limit 1")
    RptBetModel getRptBetRcdByID(@Param("id") Long id);

    @Select("select * from t_gm_code where codegroup = #{codegroup}")
    List<GameCode> getGameCodeByCodegroup(@Param("codegroup") Integer codegroup);

    @Select("select * from t_gm_code where codegroup = #{codegroup} and codeid= #{codeid}")
    GameCode getGameCodeByCodegroupAndCodeID(@Param("id") Integer codegroup, @Param("gametype") String codeid);

    List<GameCode> getGameCodeList();

    @Select("select game.depotName as platform,'game_code' as codetype,game.gameCode as codeid ,game.gameName as codename  from t_gm_game game  where catId=#{catId} and depotName=#{platform}")
    List<GameCode> getGameListByPlatForm(@Param("platform") String platform,@Param("catId") String catId);

    @Select("select codegroup,codeid,codename from t_gm_code where codetype=#{codetype}")
    Map getGameCodeMap(@Param("codetype") String codetype);

    int delRptBetDayByDate(@Param("schemaPrex") String schemaPrex,@Param("startday")String startday);

    List<TGmApi> getGameApiByApiName(@Param("apiName")String apiName,@Param("agyAcc")List<String> agyAcc);

    List<String> getSiteForeByApiId(@Param("apiId")Integer apiId);

}
