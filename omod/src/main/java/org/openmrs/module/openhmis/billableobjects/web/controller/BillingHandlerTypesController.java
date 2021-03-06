package org.openmrs.module.openhmis.billableobjects.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.billableobjects.api.IBillingHandlerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/module/openhmis/billableobjects/handlertypes")
public class BillingHandlerTypesController {
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> typeNames() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("results", Context.getService(IBillingHandlerService.class).getHandlerTypeNames());
		return result;
	}
}
