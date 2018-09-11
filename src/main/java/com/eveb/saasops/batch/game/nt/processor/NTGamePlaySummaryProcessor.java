package com.eveb.saasops.batch.game.nt.processor;

import com.eveb.saasops.batch.game.nt.domain.GamePlaySummary;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;

public class NTGamePlaySummaryProcessor extends ValidatingItemProcessor<GamePlaySummary> {

    @Override
    public GamePlaySummary process(GamePlaySummary item) throws ValidationException {
        super.process(item); //需要执行super.process(item)才会调用自定义校验器
        return item;
    }
}
