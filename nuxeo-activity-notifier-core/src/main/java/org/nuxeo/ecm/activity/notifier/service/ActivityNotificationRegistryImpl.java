package org.nuxeo.ecm.activity.notifier.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.activity.notifier.ActivityNotificationImpl;
import org.nuxeo.ecm.activity.notifier.api.ActivityNotification;
import org.nuxeo.ecm.activity.notifier.api.ActivityNotificationRegistry;
import org.nuxeo.ecm.social.relationship.RelationshipKind;
import org.nuxeo.ecm.social.relationship.service.RelationshipKindDescriptor;

public class ActivityNotificationRegistryImpl implements
		ActivityNotificationRegistry {

	private static final long serialVersionUID = 1L;
	private final Map<String, List<ActivityNotification>> notificationRegistry = new HashMap<String, List<ActivityNotification>>();
	private final List<ActivityNotification> notificationList = new ArrayList<ActivityNotification>();

	@Override
	public void clear() {
		notificationRegistry.clear();
	}

	@Override
	public void registerNotification(ActivityNotification notif,
			List<RelationshipKindDescriptor> kindsDescriptor) {
		List<RelationshipKind> kinds = new ArrayList<RelationshipKind>();
		for (RelationshipKindDescriptor kindDescriptor : kindsDescriptor) {
			kinds.add(RelationshipKind.newInstance(kindDescriptor.getGroup(),
					kindDescriptor.getName()));
		}
		ActivityNotificationImpl notification = new ActivityNotificationImpl(
				notif.getName(), notif.getLabel(), notif.getVerb(),
				notif.getTargetId(), kinds);
		if (notif.getEnabled()) {
			notificationList.add(notification);
		} else {
			unregisterNotification(notif);
		}
	}

	@Override
	public void unregisterNotification(ActivityNotification notif) {
		ActivityNotificationImpl notification = new ActivityNotificationImpl(
				notif.getName(), notif.getLabel(), notif.getVerb(),
				notif.getTargetId(), null);
		notificationList.remove(notification);
	}

	@Override
	public List<ActivityNotification> getNotifications() {
		// TODO Auto-generated method stub
		return notificationList;
	}

}
