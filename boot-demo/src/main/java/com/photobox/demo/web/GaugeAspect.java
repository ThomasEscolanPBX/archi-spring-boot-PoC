package com.photobox.demo.web;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class GaugeAspect {

	@Autowired
	private GaugeService gauges;
	
	@Around("execution(public * com.photobox.demo.web.*.*(..))")
	public Object updateGauge(ProceedingJoinPoint pjp) throws Throwable {
		StopWatch sw = new StopWatch();
		sw.start(pjp.toString());
		Object result = pjp.proceed();
		sw.stop();
		gauges.submit(sw.getLastTaskName(), sw.getLastTaskTimeMillis());
		return result;
	}
}
