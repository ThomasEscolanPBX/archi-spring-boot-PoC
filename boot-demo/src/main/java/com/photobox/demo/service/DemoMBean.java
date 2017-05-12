package com.photobox.demo.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(objectName = "DemoDomain:name=CounterMBean")
public class DemoMBean {

	private final AtomicInteger increment = new AtomicInteger();
	private final AtomicInteger value = new AtomicInteger();

	@ManagedAttribute
	public int getIncrement() {
		return this.increment.get();
	}
	@ManagedAttribute
	public void setIncrement(int increment) {
		this.increment.set(increment);
	}

	@ManagedAttribute
	public int getCurrentValue() {
		if (increment.get() > 0) {
			return this.value.addAndGet(increment.get());
		} else {
			return this.value.getAndIncrement();
		}
	}

	@ManagedOperation
	public int resetValue() {
		this.value.set(0);
		return this.value.getAndUpdate(n -> 0);	// n returned
	}
}
