// inEvo.pt 2013

package org.nuxeo.ecm.activity.notifier.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.activity.Activity;
import org.nuxeo.ecm.activity.ActivityEventContext;
import org.nuxeo.ecm.activity.notifier.api.ActivityNotification;
import org.nuxeo.ecm.activity.notifier.service.NotifierServiceHelper;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.social.relationship.RelationshipKind;

/**
 * notifier event listener. acepts events and triggers notifications
 * 
 * @author tiago
 * 
 */
public class NotifierEventListener implements EventListener {

	private static final Log log = LogFactory
			.getLog(NotifierEventListener.class);

	@Override
	public void handleEvent(Event event) throws ClientException {

		if (NotifierServiceHelper.getActivityNotifierService() == null) {
			log.error("Unable to get NotificationService, exiting");
			return;
		}

		List<ActivityNotification> notifs = NotifierServiceHelper
				.getActivityNotifierService().getNotifications();
		if (notifs != null && !notifs.isEmpty()) {
			try {
				handleNotifications(event, notifs);
			} catch (Exception e) {
				log.error("Error during Notification processing for event "
						+ event.getName(), e);
			}
		}

	}

	protected void handleNotifications(Event event,
			List<ActivityNotification> notifs) throws Exception {

		EventContext ctx = event.getContext();
		ActivityEventContext activityCtx = null;
		if (ctx instanceof ActivityEventContext) {
			activityCtx = (ActivityEventContext) ctx;
		} else {
			log.warn("Can not handle notification on a event that is not bound to a DocumentEventContext");
			return;
		}

		Map<ActivityNotification, List<String>> targetUsers = getInterstedUsers(
				activityCtx.getActivity(), notifs);
		// String user, String originEvent, String name, String target, String
		// object, String label
		for (ActivityNotification notif : targetUsers.keySet()) {
			List<String> users = targetUsers.get(notif);
			for (String user : users) {
				NotifierServiceHelper.getNotifierService()
						.addNotification(
								user,
								event.getName(),
								notif.getName(),
								"activity:"
										+ activityCtx.getActivity().getId()
												.toString(),
								activityCtx.getActivity().getTarget(),
								notif.getLabel());
			}

		}

	}

	private Map<ActivityNotification, List<String>> getInterstedUsers(
			Activity activity, List<ActivityNotification> notifs)
			throws Exception {
		Map<ActivityNotification, List<String>> interested = new HashMap<ActivityNotification, List<String>>();
		for (ActivityNotification notification : notifs) {
			List<String> notificationInterested = new ArrayList<String>();
			if (activity.getVerb().equals(notification.getVerb())) {
				for (RelationshipKind kind : notification
						.getRelationshipKinds()) {
					notificationInterested.addAll(NotifierServiceHelper
							.getRelationshipService().getTargetsOfKind(
									activity.getTarget(), kind));
				}
			}
			interested.put(notification, notificationInterested);
		}
		return interested;
	}

	/*
	 * private boolean isDeleteEvent(String eventId) { List<String>
	 * deletionEvents = new ArrayList<String>();
	 * deletionEvents.add("activityRemoved");
	 * deletionEvents.add("activityReplyRemoved"); return
	 * deletionEvents.contains(eventId); }
	 * 
	 * private boolean isUser(String subscriptor) { return subscriptor != null
	 * && subscriptor.startsWith("user:"); }
	 */

}
