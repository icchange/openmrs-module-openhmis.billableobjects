package org.openmrs.module.webservices.rest.resource;

import org.apache.commons.lang.NotImplementedException;
import org.openmrs.module.openhmis.billableobjects.api.IBillAssociatorDataService;
import org.openmrs.module.openhmis.billableobjects.api.model.BaseBillAssociator;
import org.openmrs.module.openhmis.billableobjects.api.model.IBillAssociator;
import org.openmrs.module.openhmis.commons.api.entity.IMetadataDataService;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;

@Resource(name = RestConstants.VERSION_2 + "/billableobjects/associators", supportedClass = IBillAssociator.class, supportedOpenmrsVersions = {"1.8.*", "1.9.*"})
public class BillAssociatorResource extends BaseRestMetadataResource<BaseBillAssociator> {
	private static final String NEED_SUBCLASS_HANDLER = "This operation should be handled by a subclass handler.";

	@Override
	public Class<? extends IMetadataDataService<BaseBillAssociator>> getServiceClass() {
		return IBillAssociatorDataService.class;
	}

	@Override
	public BaseBillAssociator newDelegate() {
		throw new NotImplementedException(NEED_SUBCLASS_HANDLER);
	}
}
