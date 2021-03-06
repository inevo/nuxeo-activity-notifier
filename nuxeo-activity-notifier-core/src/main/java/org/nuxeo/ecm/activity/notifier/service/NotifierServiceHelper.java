package org.nuxeo.ecm.activity.notifier.service;

import org.nuxeo.ecm.notifier.service.NotifierService;
import org.nuxeo.ecm.activity.notifier.api.ActivityNotifierManager;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.ecm.social.relationship.service.RelationshipService;
import org.nuxeo.runtime.api.Framework;

/**
 * 
 * @author Tiago Cardoso @ inEvo.pt
 * 
 */
public class NotifierServiceHelper {

	// Utility class.
	private NotifierServiceHelper() {
	}

	private static UserManager userManager;
	private static RelationshipService relationshipService;
	private static NotifierService notifierService;
	private static ActivityNotifierManager activityNotifierService;

	/**
	 * Locates the notification service using NXRuntime.
	 * 
	 * public static ActivityNotifierService getActivityNotifierService() {
	 * return (ActivityNotifierService)
	 * Framework.getLocalService(ActivityNotifierManager.class); }
	 */
	public static ActivityNotifierManager getActivityNotifierService()
			throws ClientException {
		if (activityNotifierService == null) {
			try {
				activityNotifierService = Framework
						.getLocalService(ActivityNotifierManager.class);
			} catch (Exception e) {
				throw new ClientException(e);
			}
		}
		return activityNotifierService;
	}

	public static UserManager getUsersService() throws ClientException {
		if (userManager == null) {
			try {
				userManager = Framework.getService(UserManager.class);
			} catch (Exception e) {
				throw new ClientException(e);
			}
		}
		return userManager;
	}

	public static RelationshipService getRelationshipService()
			throws ClientException {
		if (relationshipService == null) {
			try {
				relationshipService = Framework
						.getLocalService(RelationshipService.class);
			} catch (Exception e) {
				throw new ClientException(e);
			}
		}
		return relationshipService;
	}

	public static NotifierService getNotifierService() throws ClientException {
		if (notifierService == null) {
			try {
				notifierService = Framework
						.getLocalService(NotifierService.class);
			} catch (Exception e) {
				throw new ClientException(e);
			}
		}
		return notifierService;
	}

}
