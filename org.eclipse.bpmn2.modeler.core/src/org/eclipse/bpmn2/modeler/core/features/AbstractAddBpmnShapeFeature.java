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
package org.eclipse.bpmn2.modeler.core.features;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.rectangle;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.bpmn2.modeler.core.utils.ContextUtil;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ITargetContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

public abstract class AbstractAddBpmnShapeFeature<T extends BaseElement> extends AbstractAddBpmnElementFeature<T, ContainerShape> {

	public AbstractAddBpmnShapeFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	/**
	 * Creates the new shape for the given context with the specified bounds.
	 * 
	 * @param context
	 * @param bounds
	 * @return
	 */
	protected abstract ContainerShape createPictogramElement(IAddContext context, IRectangle bounds);

	/**
	 * Creates anchors for the newly created shape.
	 * 
	 * @param context
	 * @param newShape
	 */
	protected void createAnchors(IAddContext context, ContainerShape newShape) {

		// per default, create chopbox anchor and
		// four fix point anchors on all four sides of the shape (North-East-South-West)
		AnchorUtil.addChopboxAnchor(newShape);
		AnchorUtil.addFixedPointAnchors(newShape);
	}

	/**
	 * Creates the shape including its anchors with the given bounds.
	 * 
	 * @param context
	 * @param newShapeBounds
	 * 
	 * @return
	 */
	protected ContainerShape createShape(IAddContext context, IRectangle newShapeBounds) {
		ContainerShape newShape = createPictogramElement(context, newShapeBounds);
		
		postCreateHook(context, newShapeBounds, newShape);
		
		createAnchors(context, newShape);
		
		setProperties(context, newShape);
		
		return newShape;
	}
	
	/**
	 * Return bounds for the element to be added.
	 * 
	 * @param context
	 * 
	 * @return
	 */
	protected IRectangle getAddBounds(IAddContext context) {
		
		adjustLocationAndSize(context, getWidth(context), getHeight(context));
		
		return rectangle(
			context.getX(), 
			context.getY(), 
			context.getWidth(), 
			context.getHeight());
	}

	/**
	 * Get or create the {@link BPMNShape} for the given shape and baseElement in a given context. 
	 * 
	 * @param shape
	 * @param baseElement
	 * @param context
	 * 
	 * @return the bpmn shape
	 */
	protected BPMNShape createDi(Shape shape, BaseElement baseElement, IAddContext context) {
		boolean isImport = isImport(context);
		
		return createDIShape(shape, baseElement, findDIShape(baseElement), !isImport);
	}

	/**
	 * Creates the di shape for the given element
	 * 
	 * @param shape
	 * @param elem
	 * @param bpmnShape
	 * @param applyDefaults
	 * @return
	 */
	protected BPMNShape createDIShape(Shape shape, BaseElement elem, BPMNShape bpmnShape, boolean applyDefaults) {
		if (bpmnShape == null) {
			IRectangle bounds = LayoutUtil.getAbsoluteBounds(shape);
			bpmnShape = DIUtils.createDIShape(elem, bounds, getDiagram());
		}
		
		link(shape, new Object[] { elem, bpmnShape });
		
		if (applyDefaults) {
			Bpmn2Preferences.getInstance(bpmnShape.eResource()).applyBPMNDIDefaults(bpmnShape, null);
		}
		
		return bpmnShape;
	}

	protected BPMNShape findDIShape(BaseElement element) {
		return (BPMNShape) ModelHandler.findDIElement(getDiagram(), element);
	}

	/**
	 * Perform a post add operation once the shape with the given bounds
	 * has beed added.
	 * 
	 * @param context
	 * @param newShape
	 * @param elementBounds
	 * 
	 * @set {@link #postAddHook(IAddContext, PictogramElement)}
	 */
	protected void postAddHook(IAddContext context, ContainerShape newShape, IRectangle elementBounds) {
		postAddHook(context, newShape);
	}
	
	/**
	 * Return a label add context for the given context and shape bounds.
	 * 
	 * @param context
	 * @param newShapeBounds
	 * @return
	 */
	protected IAddContext getAddLabelContext(IAddContext context, IRectangle newShapeBounds) {

		GraphicsUtil.prepareLabelAddContext(context, 
			newShapeBounds.getWidth(), 
			newShapeBounds.getHeight(), 
			getBusinessObject(context));
		
		return context;
	}
	
	@Override
	public PictogramElement add(IAddContext context) {
		T activity = getBusinessObject(context);

		// compute actual add bounds
		IRectangle elementBounds = getAddBounds(context);
		
		// create graphical element
		ContainerShape newShape = createShape(context, elementBounds);

		// create di
		createDi(newShape, activity, context);
		
		// allow a post add operation
		postAddHook(context, newShape, elementBounds);
		
		// create label
		createLabel(context, elementBounds);
		
		// perform update and layouting
		updateAndLayout(newShape);
		
		return newShape;
	}

	/**
	 * Perform an initial update and layouting of the new shape as desired.
	 * 
	 * @param newShape
	 */
	protected void updateAndLayout(ContainerShape newShape) {
		// update
		updatePictogramElement(newShape);
		
		// layout
		layoutPictogramElement(newShape);
	}

	/**
	 * Creates a label for the newly created shape if any.
	 * 
	 * May be overridden by subclasses to perform actual actions.
	 * 
	 * @param context
	 * @param newShape
	 */
	protected void createLabel(IAddContext context, IRectangle newShapeBounds) {
		
		// create label if the add shape feature wishes to do so
		if (isCreateExternalLabel()) {
			IAddContext addLabelContext = getAddLabelContext(context, newShapeBounds);
			if (addLabelContext != null) {
				getFeatureProvider().getAddFeature(context).add(context);
			}
		}
	}

	/**
	 * Adjust the add context location and size 
	 * 
	 * @param context
	 * @param width
	 * @param height
	 */
	protected void adjustLocationAndSize(IAddContext context, int width, int height) {
		
		if (isImport(context)) {
			return;
		}
		
//		System.out.println(String.format("Adjust location <%s>", context));
		
		adjustLocation(context, width, height);
		adjustSize(context, width, height);
	}
	
	protected void adjustSize(IAddContext context, int width, int height) {
		if (context instanceof AddContext) {
			AddContext addContext = (AddContext) context;
			addContext.setSize(width, height);
		}
	}

	protected void adjustLocation(IAddContext context, int width, int height) {
		int x = context.getX() - width / 2;
		int y = context.getY() - height / 2;
		
		if (context instanceof AddContext) {
			AddContext addContext = (AddContext) context;
			addContext.setLocation(x, y);
		}
	}
	
	protected int getHeight(IAddContext context) {
		return context.getHeight() > 0 ? context.getHeight() :
			(isHorizontal(context) ? getDefaultHeight() : getDefaultWidth());
	}
	
	protected int getWidth(IAddContext context) {
		return context.getWidth() > 0 ? context.getWidth() :
			(isHorizontal(context) ? getDefaultWidth() : getDefaultHeight());
	}

	protected boolean isHorizontal(ITargetContext context) {
		if (!isImport(context)) {
			// not importing - set isHorizontal to be the same as parent Pool
			if (FeatureSupport.isTargetParticipant(context)) {
				Participant targetParticipant = FeatureSupport.getTargetParticipant(context);
				BPMNShape participantShape = findDIShape(targetParticipant);
				if (participantShape != null) {
					return participantShape.isIsHorizontal();
				}
			} else
			
			if (FeatureSupport.isTargetLane(context)) {
				Lane targetLane = FeatureSupport.getTargetLane(context);
				BPMNShape laneShape = findDIShape(targetLane);
				if (laneShape != null) {
					return laneShape.isIsHorizontal();
				}
			}
		}
		return Bpmn2Preferences.getInstance().isHorizontalDefault();
	}
	
	/**
	 * Return the default height
	 * 
	 * @return
	 */
	public abstract int getDefaultHeight();
	
	/**
	 * Return the default width
	 * @return
	 */
	public abstract int getDefaultWidth();
	
	/**
	 * Return true if the element should create a label
	 * 
	 * @return
	 */
	protected abstract boolean isCreateExternalLabel();
}