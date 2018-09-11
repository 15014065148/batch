package com.eveb.saasops.batch.game.nt.processor;

import com.eveb.saasops.batch.game.nt.domain.RevenueReport;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;

public class NTRevenueReportProcessor extends ValidatingItemProcessor<RevenueReport> {

    @Override
    public RevenueReport process(RevenueReport item) throws ValidationException {
        super.process(item); //需要执行super.process(item)才会调用自定义校验器
        return item;
    }

}
