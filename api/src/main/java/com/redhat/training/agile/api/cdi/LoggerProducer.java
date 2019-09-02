package com.redhat.training.agile.api.cdi;

import java.util.logging.Logger;

import javax.ejb.Singleton;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

@Singleton
public class LoggerProducer {

    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint) {
    	return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }
}