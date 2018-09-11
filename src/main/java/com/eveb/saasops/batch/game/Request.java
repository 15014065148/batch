package com.eveb.saasops.batch.game;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface Request<M> {

    List<M> getBetList(GameParameter<Map<String, Object>> parameter) throws Exception;
}
