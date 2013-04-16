// inEvo.pt 2013

package org.nuxeo.ecm.activity.notifier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mvel2.PropertyAccessException;
import org.nuxeo.ecm.activity.notifier.api.ActivityNotification;
import org.nuxeo.ecm.activity.notifier.service.ActivityNotifierService;
import org.nuxeo.ecm.activity.notifier.service.NotifierServiceHelper;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DataModel;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.PostCommitFilteringEventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.ecm.platform.ec.notification.NotificationConstants;
import org.nuxeo.ecm.platform.ec.notification.NotificationEventListener;
import org.nuxeo.ecm.platform.ec.notification.NotificationImpl;
import org.nuxeo.ecm.platform.ec.notification.email.EmailHelper;
import org.nuxeo.ecm.platform.ec.notification.service.NotificationServiceHelper;
import org.nuxeo.ecm.platform.notification.api.Notification;
import org.nuxeo.ecm.platform.url.DocumentViewImpl;
import org.nuxeo.ecm.platform.url.api.DocumentView;
import org.nuxeo.ecm.platform.url.api.DocumentViewCodecManager;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.ecm.social.relationship.RelationshipKind;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.ecm.activity.Activity;
import org.nuxeo.ecm.activity.ActivityEventContext;

/**
 * notifier event listener. acepts events and triggers notifications
 * 
 * @author tiago
 * 
 */
public class NotifierEventListener implements PostCommitFilteringEventListener {

	private static final Log log = LogFactory
			.getLog(NotificationEventListener.class);

	private ActivityNotifierService activityNotifierService = NotifierServiceHelper
			.getActivityNotifierService();

	@Override
	public boolean acceptEvent(Event event) {
		if (activityNotifierService == null) {
			return false;
		}
		return true;
	}

	@Override
	public void handleEvent(EventBundle events) throws ClientException {

		if (activityNotifierService == null) {
			log.error("Unable to get NotificationService, exiting");
			return;
		}

		for (Event event : events) {
			List<ActivityNotification> notifs = activityNotifierService
					.getNotifications();
			if (notifs != null && !notifs.isEmpty()) {
				try {
					handleNotifications(event, notifs);
				} catch (Exception e) {
					log.error("Error during Notification processing for event "
							+ event.getName(), e);
				}
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

		for (ActivityNotification notif : targetUsers.keySet()) {
			List<String> users = targetUsers.get(notif);
			for (String user : users) {
				NotifierServiceHelper.getNotifierService().addNotification(
						user, event.getName(), notif.getName(),
						notif.getTargetId(), notif.getVerb());
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
