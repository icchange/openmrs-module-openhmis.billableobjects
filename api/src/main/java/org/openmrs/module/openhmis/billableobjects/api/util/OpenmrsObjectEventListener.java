package org.openmrs.module.openhmis.billableobjects.api.util;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

import org.apache.log4j.Logger;
import org.openmrs.OpenmrsObject;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.Daemon;
import org.openmrs.event.EventListener;
import org.openmrs.module.DaemonToken;
import org.openmrs.module.openhmis.billableobjects.api.IBillableObjectDataService;
import org.openmrs.module.openhmis.billableobjects.api.IBillableObjectsService;
import org.openmrs.module.openhmis.billableobjects.api.type.BaseBillableObject;
import org.openmrs.module.openhmis.billableobjects.api.type.IBillableObject;

public class OpenmrsObjectEventListener implements EventListener, Runnable {
	private static final Logger logger = Logger.getLogger(OpenmrsObjectEventListener.class);
	private static IBillableObjectsService billableObjectsService;
	private DaemonToken daemonToken;
	private Message message;

	public OpenmrsObjectEventListener(DaemonToken token) {
		if (token != null)
			daemonToken = token;
		else
			throw new APIException("The DaemonToken cannot be null.");
	}
	
	@Override
	public void onMessage(Message message) {
		billableObjectsService = Context.getService(IBillableObjectsService.class);
		this.message = message;
		Daemon.runInDaemonThread(this, daemonToken);
	}
			
	@Override
	public void run() {
		try {
			MapMessage mapMessage = (MapMessage) message;
			String className = mapMessage.getString("classname");
			String associatedUuid = mapMessage.getString("uuid");
			
			IBillableObject billableObject;
			Class<? extends IBillableObject> cls = billableObjectsService
					.getBillableObjectClassForClassName(className);
			if (cls != null)
				billableObject = cls.newInstance();
			else
				throw new APIException("No handler found for class " + className + ".");
			OpenmrsObject associatedObject = billableObject.getObjectByUuid(associatedUuid);
			billableObject.setObject(associatedObject);			
			Context.getService(IBillableObjectDataService.class).save((BaseBillableObject<?>) billableObject);
		}
		catch (InstantiationException e) {
			logger.error("Error instantiating IBillableObject class: " + e.getMessage());
		}
		catch (JMSException e) {
			logger.error("Error reading message: " + e.getMessage());
		} catch (Exception e) {
			logger.error("Error handling message: " + e.getMessage());
			e.printStackTrace();
		}			
	}
}
