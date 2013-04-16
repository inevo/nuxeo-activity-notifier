package org.nuxeo.ecm.activity.notifier.service;

import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.activity.notifier.api.ActivityNotification;
import org.nuxeo.ecm.activity.notifier.api.ActivityNotifierManager;
import org.nuxeo.ecm.activity.notifier.api.ActivityNotificationRegistry;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.platform.ec.notification.service.GeneralSettingsDescriptor;
import org.nuxeo.ecm.social.relationship.RelationshipConstants;
import org.nuxeo.ecm.social.relationship.RelationshipKind;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.ComponentName;
import org.nuxeo.runtime.model.DefaultComponent;
import org.nuxeo.runtime.model.Extension;

public class ActivityNotifierService extends DefaultComponent implements ActivityNotifierManager {
	private static final Log log = LogFactory.getLog(ActivityNotifierService.class);
	
	public static final ComponentName NAME = new ComponentName(
            "org.nuxeo.ecm.activity.notifier.service.ActivityNotifierService");
	
    protected static final String NOTIFICATIONS_EP = "notifications";
    protected static final String SETTINGS_EP = "settings";
    
    protected SettingsDescriptor settings;
    protected ActivityNotificationRegistry notificationRegistry;

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getAdapter(Class<T> adapter) {
        if (adapter.isAssignableFrom(ActivityNotifierManager.class)) {
            return (T) this;
        }
        return null;
    }
    
    @Override
    public void activate(ComponentContext context) throws Exception {
        notificationRegistry = new ActivityNotificationRegistryImpl();

        // init default settings
        settings = new SettingsDescriptor();
        //TODO: what are the needed settings ?
       // settings.documentRelation = RelationshipKind.newInstance("document", "followes").toString();
       // settings.userRelation = RelationshipKind.newInstance("circle", "followes").toString();

    }

    @Override
    public void deactivate(ComponentContext context) throws Exception {
        notificationRegistry.clear();
        notificationRegistry = null;
    }
    
    @Override
    public void registerExtension(Extension extension) throws Exception {
        log.info("Registering notification extension");
        String xp = extension.getExtensionPoint();
        if (NOTIFICATIONS_EP.equals(xp)) {
            Object[] contribs = extension.getContributions();
            for (Object contrib : contribs) {
                try {
                    ActivityNotificationDescriptor notifDesc = (ActivityNotificationDescriptor) contrib;
                    notificationRegistry.registerNotification(notifDesc, notifDesc.getRelationshipKindDescriptors());
                } catch (Exception e) {
                    log.error(e);
                }
            }
        } else if (SETTINGS_EP.equals(xp)) {
            Object[] contribs = extension.getContributions();
            for (Object contrib : contribs) {
                try {
                	registerSettings((SettingsDescriptor) contrib);
                } catch (Exception e) {
                    log.error(e);
                }
            }
        }
    }

    protected void registerSettings(SettingsDescriptor desc) {
    	settings = desc;
        // TODO: insert service settings here
        //generalSettings.eMailSubjectPrefix = Framework.expandVars(generalSettings.eMailSubjectPrefix);
    }

    
    @Override
    public void unregisterExtension(Extension extension) throws Exception {
        String xp = extension.getExtensionPoint();
        if (NOTIFICATIONS_EP.equals(xp)) {
            Object[] contribs = extension.getContributions();
            for (Object contrib : contribs) {
                try {
                    ActivityNotificationDescriptor notifDesc = (ActivityNotificationDescriptor) contrib;
                    notificationRegistry.unregisterNotification(notifDesc);
                } catch (Exception e) {
                    log.error(e);
                }
            }
        } 
    }
    
    public ActivityNotificationRegistry getNotificationRegistry() {
        return notificationRegistry;
    }
    
    //TODO : get subscribers for an activity notification

	@Override
	public List<ActivityNotification> getNotifications() {
		// TODO Auto-generated method stub
		return notificationRegistry.getNotifications();
	}


}
