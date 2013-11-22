package org.openmrs.module.openhmis.billableobjects.api.types;

import org.openmrs.OpenmrsObject;
import org.openmrs.module.openhmis.billableobjects.api.IBillableObject;
import org.openmrs.module.openhmis.cashier.api.model.Bill;

public class BaseBillableObject<T extends OpenmrsObject> implements IBillableObject<T> {
	
	private Integer billable_object_id;
	private String billable_object_uuid;
	private T object;
	private Bill bill;
	
	@Override
	public Integer getId() {
		return billable_object_id;
	}

	@Override
	public void setId(Integer id) {
		billable_object_id = id;
	}

	@Override
	public T getObject() {
		return object;
	}

	@Override
	public void setObject(T object) {
		this.object = object;
	}

	@Override
	public Bill getBill() {
		return bill;
	}

	@Override
	public void setBill(Bill bill) {
		this.bill = bill;
	}

	@Override
	public String getUuid() {
		return billable_object_uuid;
	}

	@Override
	public void setUuid(String uuid) {
		billable_object_uuid = uuid;
	}

}