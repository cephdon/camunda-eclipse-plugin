package org.camunda.bpm.modeler.test.feature.move;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.operations.MoveShapeOperation.move;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

public class MoveParticipantFeatureTest extends AbstractFeatureTest {

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/move/MoveParticipantFeatureTest.testMoveParticipantBase.bpmn")
	public void testMoveParticipantMovesLabel() {

		// given
		Shape flowElementShape = Util.findShapeByBusinessObjectId(diagram, "StartEvent_2");

		ContainerShape participantShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Participant_1");
		
		Shape labelShape = LabelUtil.getLabelShape(flowElementShape, getDiagram());
		
		IRectangle preMoveLabelBounds = LayoutUtil.getAbsoluteBounds(labelShape);
		
		// assume
		// label is on diagram
		assertThat(labelShape).isContainedIn(diagram);
		
		// when
		// moving participant
		move(participantShape, diagramTypeProvider)
			.by(20, 20)
			.execute();
		
		// then 
		// label should have been moved accordingly
		assertThat(labelShape)
			.isContainedIn(diagram)
			.movedBy(point(20, 20), preMoveLabelBounds);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/move/MoveParticipantFeatureTest.testMoveParticipantBase.bpmn")
	public void testMoveParticipantMovesBoundaryAttachedLabel() {
		
		// given
		Shape flowElementShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_2");

		ContainerShape participantShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Participant_1");
		
		Shape labelShape = LabelUtil.getLabelShape(flowElementShape, getDiagram());
		
		IRectangle preMoveLabelBounds = LayoutUtil.getAbsoluteBounds(labelShape);
		
		// assume
		// label is on diagram
		assertThat(labelShape).isContainedIn(diagram);
		
		// when
		// moving participant
		move(participantShape, diagramTypeProvider)
			.by(20, 20)
			.execute();
		
		// then 
		// label should have been moved accordingly
		assertThat(labelShape)
			.isContainedIn(diagram)
			.movedBy(point(20, 20), preMoveLabelBounds);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/move/MoveParticipantFeatureTest.testMoveParticipantBase.bpmn")
	public void testMoveParticipantWithLaneMovesLabel() {

		// given
		Shape flowElementShape = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");

		ContainerShape participantShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "_Participant_3");
		
		Shape labelShape = LabelUtil.getLabelShape(flowElementShape, getDiagram());
		
		IRectangle preMoveLabelBounds = LayoutUtil.getAbsoluteBounds(labelShape);
		
		// assume
		// label is on diagram
		assertThat(labelShape).isContainedIn(diagram);
		
		// when
		// moving participant
		move(participantShape, diagramTypeProvider)
			.by(20, 20)
			.execute();
		
		// then 
		// label should have been moved accordingly
		assertThat(labelShape)
			.isContainedIn(diagram)
			.movedBy(point(20, 20), preMoveLabelBounds);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/move/MoveParticipantFeatureTest.testMoveParticipantBase.bpmn")
	public void testMoveParticipantWithLaneMovesBoundaryAttachedLabel() {
		
		// given
		Shape flowElementShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");

		ContainerShape participantShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "_Participant_3");
		
		Shape labelShape = LabelUtil.getLabelShape(flowElementShape, getDiagram());
		
		IRectangle preMoveLabelBounds = LayoutUtil.getAbsoluteBounds(labelShape);
		
		// assume
		// label is on diagram
		assertThat(labelShape).isContainedIn(diagram);
		
		// when
		// moving participant
		move(participantShape, diagramTypeProvider)
			.by(20, 20)
			.execute();
		
		// then 
		// label should have been moved accordingly
		assertThat(labelShape)
			.isContainedIn(diagram)
			.movedBy(point(20, 20), preMoveLabelBounds);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/move/MoveParticipantFeatureTest.testMoveParticipantWithSubProcessBase.bpmn")
	public void testMoveParticipantWithSubProcessMovesNestedEventLabel() {
		
		// given
		Shape flowElementShape = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");

		ContainerShape participantShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "_Participant_3");
		
		Shape labelShape = LabelUtil.getLabelShape(flowElementShape, getDiagram());
		
		IRectangle preMoveLabelBounds = LayoutUtil.getAbsoluteBounds(labelShape);
		
		// assume
		// label is on diagram
		assertThat(labelShape).isContainedIn(diagram);
		
		// when
		// moving participant
		move(participantShape, diagramTypeProvider)
			.by(20, 20)
			.execute();
		
		// then 
		// label should have been moved accordingly
		assertThat(labelShape)
			.isContainedIn(diagram)
			.movedBy(point(20, 20), preMoveLabelBounds);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/move/MoveParticipantFeatureTest.testMoveParticipantWithSubProcessBase.bpmn")
	public void testMoveParticipantWithSubProcessMovesNestedBoundaryEventLabel() {
		
		// given
		Shape flowElementShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_2");

		ContainerShape participantShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "_Participant_3");
		
		Shape labelShape = LabelUtil.getLabelShape(flowElementShape, getDiagram());
		
		IRectangle preMoveLabelBounds = LayoutUtil.getAbsoluteBounds(labelShape);
		
		// assume
		// label is on diagram
		assertThat(labelShape).isContainedIn(diagram);
		
		// when
		// moving participant
		move(participantShape, diagramTypeProvider)
			.by(20, 20)
			.execute();
		
		// then 
		// label should have been moved accordingly
		assertThat(labelShape)
			.isContainedIn(diagram)
			.movedBy(point(20, 20), preMoveLabelBounds);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/move/ParticipantTaskConnectedBoundaryEvent.testBase.bpmn")
	public void testMoveParticipantWithConnectedBoundaryEvent() {

		// given
		Shape boundaryShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");

		ContainerShape participantShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "_Participant_3");
		
		IRectangle preMoveBoundaryBounds = LayoutUtil.getAbsoluteBounds(boundaryShape);
		
		// assume
		// label is on diagram
		assertThat(boundaryShape).isContainedIn(participantShape);
		
		// when
		// moving participant
		move(participantShape, diagramTypeProvider)
			.by(20, 20)
			.execute();
		
		// then 
		// label should have been moved accordingly
		assertThat(boundaryShape)
			.isContainedIn(participantShape)
			.movedBy(point(20, 20), preMoveBoundaryBounds);
	}
}
