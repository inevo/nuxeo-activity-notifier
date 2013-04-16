package org.nuxeo.ecm.activity.notifier.api;

import java.util.List;
import java.util.Set;

import org.nuxeo.ecm.core.api.ClientException;

/**
 * 
 * @author Tiago Cardoso @ inEvo.pt
 *
 */
public interface ActivityNotifierManager {



    List<ActivityNotification> getNotifications();


	
}
