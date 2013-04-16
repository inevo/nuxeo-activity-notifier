package org.nuxeo.ecm.activity.notifier.api;

import java.io.Serializable;
import java.util.List;

import org.nuxeo.ecm.social.relationship.RelationshipKind;
import org.nuxeo.ecm.social.relationship.service.RelationshipKindDescriptor;

public interface ActivityNotification extends Serializable {

	/**
	 * @return the name.
	 */
	String getName();

	/**
	 * @return the subject.
	 */
	String getLabel();

	/**
	 * @return the subject template.
	 */
	String getVerb();

	String getTargetId();

	boolean getEnabled();

	List<RelationshipKind> getRelationshipKinds();


}
