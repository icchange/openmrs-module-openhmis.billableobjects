define(
    [
        openhmis.url.backboneBase + 'js/openhmis',
        openhmis.url.backboneBase + 'js/lib/i18n',
        openhmis.url.backboneBase + 'js/model/generic',
        openhmis.url.inventoryBase + 'js/model/item',
        openhmis.url.billableobjBase + 'js/model/encounterType'
    ],
    function(openhmis, __) {
    	
        openhmis.BillingHandlerType = openhmis.GenericModel.extend({
        	urlRoot: openhmis.url.page + openhmis.url.billableobjBase + "handlertypes.json",
        	
        	parse: function(resp) {
        		return { uuid: resp, name: resp };
        	},
        	
        	toString: function() {
        		return this.get("name");
        	}
        	
        });

        openhmis.BillingHandler = openhmis.GenericModel.extend({
            meta: {
                name: __("Billing Handler"),
                openmrsType: 'metadata',
                restUrl: 'v2/billableobjects/handlers'
            },
            url: function() {
            	// A bit of a hack to make sure we go to the model for the url
            	var tmpCollection = this.collection;
            	this.collection = undefined;
            	var url = openhmis.GenericModel.prototype.url.call(this);
            	this.collection = tmpCollection;
            	return url;
            },
            schema: {
                type: {
                	type: 'BillingHandlerTypeSelect',
                	options: new openhmis.GenericCollection([], { model: openhmis.BillingHandlerType }),
                	objRef: true
                }
            },
            
            toJSON: function() {
            	var attrs = openhmis.GenericModel.prototype.toJSON.call(this);
            	delete attrs.type;
            	return attrs;
            }
        });
        
        openhmis.EncounterHandler = openhmis.BillingHandler.extend({
            meta: {
                name: __("Encounter Handler"),
                openmrsType: 'metadata',
                restUrl: 'v2/billableobjects/encounterhandlers'
            },
            
            schema: _.extend({}, openhmis.BillingHandler.prototype.schema, {
            	name: 'Text',
            	description: 'Text',
            	encounterType: {
            		type: 'GenericModelSelect',
            		modelType: openhmis.EncounterType,
            		options: new openhmis.GenericCollection(null, { model: openhmis.EncounterType }),
            		objRef: true
            	},
            	associatedItems: {
            		type: 'List',
            		itemType: 'NestedExistingModel',
            		options: new openhmis.GenericCollection(null, { model: openhmis.Item }),
            		title: "Choose an item",
            		editor: 'ItemListSelect',
            		editorOptions: {
            			listFields: ["name", "defaultPrice"]
            		},
            		model: openhmis.Item,
            		objRef: true
            	}
            }),
            
            initialize: function(attrs, options) {
            	this.set("type", "EncounterHandler", { silent: true });
            },

            validate: function(attrs, options) {
    			if (!attrs.name) return { name: __("A name is required") };
                return null;
            },
            
        	toString: function() {
        		return this.get("name");
        	}
        });
        
        openhmis.DrugOrderHandler = openhmis.BillingHandler.extend({
            meta: {
                name: __("Drug Order Handler"),
                openmrsType: 'metadata',
                restUrl: 'v2/billableobjects/drugorderhandlers'
            },

            schema: _.extend({}, openhmis.BillingHandler.prototype.schema, {
            	name: 'Text',
            	description: 'Text'
            }),
            
            initialize: function(attrs, options) {
            	this.set("type", "DrugOrderHandler", { silent: true });
            },
            
        	toString: function() {
        		return this.get("name");
        	}
        });
        
        return openhmis;
    }
);