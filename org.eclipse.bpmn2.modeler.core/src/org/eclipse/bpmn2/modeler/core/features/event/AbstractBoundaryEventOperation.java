/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 *
 * @author Ivar Meikas
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core.features.event;

import java.util.Collection;
import java.util.List;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.core.runtime.Assert;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public abstract class AbstractBoundaryEventOperation {

	public void execute(Shape shape) {
		execute(shape, shape.getContainer());
	}
	
	public void execute(Shape shape, Shape eventContainer) {
		
		Assert.isNotNull(eventContainer);
		
		Collection<PictogramElement> elements = Graphiti.getPeService().getAllContainedPictogramElements(eventContainer);

		Activity activity = BusinessObjectUtil.getFirstElementOfType(shape, Activity.class);
		List<BoundaryEvent> boundaryEvents = activity.getBoundaryEventRefs();
		
		for (PictogramElement e : elements) {
			BoundaryEvent boundaryEvent = BusinessObjectUtil.getFirstElementOfType(e, BoundaryEvent.class);
			if (boundaryEvents.contains(boundaryEvent)) {
				ContainerShape container = (ContainerShape) e;
				applyTo(container);
			}
		}
	}

	protected abstract void applyTo(ContainerShape container);
}