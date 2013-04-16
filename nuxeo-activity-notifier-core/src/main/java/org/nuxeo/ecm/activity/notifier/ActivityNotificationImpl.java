package org.nuxeo.ecm.activity.notifier;

import java.util.List;

import org.nuxeo.ecm.activity.notifier.api.ActivityNotification;
import org.nuxeo.ecm.platform.ec.notification.NotificationImpl;
import org.nuxeo.ecm.social.relationship.RelationshipKind;
import org.nuxeo.ecm.social.relationship.service.RelationshipKindDescriptor;

import com.sun.org.apache.xpath.internal.operations.Equals;

public class ActivityNotificationImpl implements ActivityNotification {

	private static final long serialVersionUID = 655069887548493482L;

	private String name;

	private String label;

	private String verb;

	private String targetId;

	private boolean enabled;
	
	private List<RelationshipKind> relationshipKinds;
	
	public ActivityNotificationImpl(String name, String label, String verb,
			String targetId, List<RelationshipKind> relationshipKinds) {
		this.name = name;
		this.verb = verb;
		this.label = label;
		this.targetId = targetId;
		this.relationshipKinds = relationshipKinds;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return label;
	}

	@Override
	public String getVerb() {
		// TODO Auto-generated method stub
		return verb;
	}

	@Override
	public String getTargetId() {
		// TODO Auto-generated method stub
		return targetId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof NotificationImpl) {
			NotificationImpl other = (NotificationImpl) obj;
			return name.equals(other.getName());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public List<RelationshipKind> getRelationshipKinds() {
		// TODO Auto-generated method stub
		return relationshipKinds;
	}



}
