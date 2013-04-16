package org.nuxeo.ecm.activity.notifier.api;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.nuxeo.ecm.social.relationship.service.RelationshipKindDescriptor;

/**
 * 
 * @author Tiago Cardoso @ inEvo.pt
 *
 */
public interface ActivityNotificationRegistry extends Serializable {

    void clear();

    void registerNotification(ActivityNotification notif, List<RelationshipKindDescriptor> kindsDescription);

    void unregisterNotification(ActivityNotification notif);

    List<ActivityNotification> getNotifications();


}
