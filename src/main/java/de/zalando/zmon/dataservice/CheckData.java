package de.zalando.zmon.dataservice;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

/**
 * Created by jmussler on 4/22/15.
 */
public class CheckData {
    public String time;
    public int check_id;
    public String entity_id;
    public int run_time;
    public JsonNode check_result;
    public boolean exception;
    Map<Integer, AlertData> alerts;
}
