package com.eveb.saasops.batch.bet.service;

import com.eveb.saasops.batch.bet.entity.MbrDepotWallet;
import com.eveb.saasops.batch.bet.mapper.DepotBalanceMapper;
import com.eveb.saasops.batch.common.SchemaCode;
import com.eveb.saasops.batch.feign.feign.entity.R;
import com.eveb.saasops.batch.sys.service.SysService;
import com.eveb.saasops.batch.sys.util.AesUtil;
import com.eveb.saasops.batch.sys.util.Collections3;
import com.eveb.saasops.batch.sys.util.SiteCodeAesUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static com.eveb.saasops.batch.sys.util.DateUtil.FORMAT_18_DATE_TIME;
import static com.eveb.saasops.batch.sys.util.DateUtil.getCurrentDate;

@Slf4j
@Service
@Transactional
public class UpdateDepotBalance {

    @Autowired
    private DepotBalanceMapper depotBalanceMapper;
    @Autowired
    private IDepotBalanceService depotBalanceService;
    @Autowired
    private SysService sysService;

    public void setDepotBalance(String siteCode) {
        String schemaPrex = new SchemaCode(sysService.getSchemaName(siteCode)).getSchemaCode();
        List<MbrDepotWallet> depotWallets = depotBalanceMapper.findMbrDepotWalletList(schemaPrex);
        if (Collections3.isNotEmpty(depotWallets)) {
            depotWallets.stream().forEach(ds -> {
                updateDepotBalance(ds, siteCode);
            });
        }
    }

    private void updateDepotBalance(MbrDepotWallet depotWallet, String siteCode) {
        MbrDepotWallet mbrDepotWallet = new MbrDepotWallet();
        mbrDepotWallet.setId(depotWallet.getId());
        mbrDepotWallet.setBalance(getDepotBalance(siteCode, depotWallet));
        mbrDepotWallet.setTime(getCurrentDate(FORMAT_18_DATE_TIME));
        String schemaPrex = new SchemaCode(sysService.getSchemaName(siteCode)).getSchemaCode();
        mbrDepotWallet.setSchemaPrex(schemaPrex);
        depotBalanceMapper.updateMbrDepotWalletList(mbrDepotWallet);
    }

    public BigDecimal getDepotBalance(String siteCode, MbrDepotWallet depotWallet) {
        R r = depotBalanceService.depotBalance(SiteCodeAesUtil.encrypt(siteCode),
                depotWallet.getDepotId(), depotWallet.getAccountId());
        log.info("站点【" + siteCode + "】会员【" + depotWallet.getLoginName() + "】,获取余额【" + new Gson().toJson(r) + "】");
        if (Integer.parseInt(r.get("code").toString()) == 0 && Objects.nonNull(r.get("data"))) {
            JsonElement balance = new JsonParser().parse(r.get("data").toString()).getAsJsonObject().get("balance");
            return Objects.nonNull(balance) ? new BigDecimal(balance.toString()) : BigDecimal.ZERO;
        }
        return BigDecimal.ZERO;
    }
}