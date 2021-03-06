package org.camunda.bpm.modeler.ui.features.activity;

import org.camunda.bpm.modeler.core.features.DefaultBpmn2ResizeShapeFeature;
import org.camunda.bpm.modeler.core.features.event.AbstractBoundaryEventOperation;
import org.camunda.bpm.modeler.core.layout.util.BoundaryEventUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * The abstract resize feature for activities.
 * 
 * @author nico.rehwaldt
 */
public class ResizeActivityFeature extends DefaultBpmn2ResizeShapeFeature {
	
	public ResizeActivityFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void postResize(IResizeShapeContext context) {
		super.postResize(context);
		
		final Shape shape = context.getShape();
		
		new AbstractBoundaryEventOperation() {
			
			@Override
			protected void applyTo(ContainerShape boundaryShape) {
				BoundaryEventUtil.repositionBoundaryEvent(boundaryShape, shape, getFeatureProvider());
			}
		}.execute(shape);
	}
}