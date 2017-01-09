package com.dwqb.tenant.schedule.strategy;

public interface IStrategyTask {

   void initialTaskParameter(String strategyName, String taskParameter) throws Exception;

   void stop(String strategyName) throws Exception;
}
