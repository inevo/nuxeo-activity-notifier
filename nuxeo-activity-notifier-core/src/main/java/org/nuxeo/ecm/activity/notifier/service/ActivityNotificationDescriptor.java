package org.nuxeo.ecm.activity.notifier.service;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XNodeList;
import org.nuxeo.common.xmap.annotation.XObject;
import org.nuxeo.ecm.activity.notifier.api.ActivityNotification;
import org.nuxeo.ecm.social.relationship.RelationshipKind;
import org.nuxeo.ecm.social.relationship.service.RelationshipKindDescriptor;

@XObject("activityNotification")
public class ActivityNotificationDescriptor implements ActivityNotification {

	private static final long serialVersionUID = -5974825427889204458L;

	@XNode("@name")
	protected String name;

	@XNode("@label")
	protected String label; // used for i10n

	@XNode("@verb")
	protected String verb;

	@XNode("@targetId")
	protected String targetId;

	@XNode("@enabled")
	protected boolean enabled = true;

	@XNodeList(value = "kind", type = ArrayList.class, componentType = RelationshipKindDescriptor.class)
	protected List<RelationshipKindDescriptor> relationshipKinds;

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getVerb() {
		return verb;
	}

	@Override
	public String getTargetId() {
		return verb;
	}

	@Override
	public boolean getEnabled() {
		return enabled;
	}

	@Override
	public List<RelationshipKind> getRelationshipKinds() {
		return null;
	}

	public List<RelationshipKindDescriptor> getRelationshipKindDescriptors() {
		return relationshipKinds;
	}
}