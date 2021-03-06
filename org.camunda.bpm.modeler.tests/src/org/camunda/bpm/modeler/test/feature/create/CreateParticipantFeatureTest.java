package org.camunda.bpm.modeler.test.feature.create;

import static org.camunda.bpm.modeler.test.util.operations.CreateParticipantOperation.createParticipant;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import org.camunda.bpm.modeler.core.di.DIUtils;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * @author adrobisch
 */
public class CreateParticipantFeatureTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource
	public void testParticipantToEmptyDefinitions() {
		createAndCheckSingleParticipant();
	}

	private void createAndCheckSingleParticipant() {

		assertThat(firstLinkedObject(diagram)).isInstanceOf(Process.class);
		
		assertThat(ModelUtil.getAllRootElements(getDefinitions(), Collaboration.class)).hasSize(0);
		
		createParticipant(20, 20, 400, 200, getDiagram(), getDiagramTypeProvider()).execute();
		
		// make sure diagram is linked to collaboration now
		assertThat(firstLinkedObject(diagram)).isInstanceOf(Collaboration.class);
		
		assertThat(ModelUtil.getAllRootElements(getDefinitions(), Collaboration.class)).hasSize(1);
		
		Collaboration collaboration = ModelUtil.getAllRootElements(getDefinitions(), Collaboration.class).get(0);
		assertThat(collaboration.getParticipants()).hasSize(1);
		
		BPMNDiagram bpmnDiagram = BusinessObjectUtil.getFirstElementOfType(getDiagram(), BPMNDiagram.class);
		
		assertThat(bpmnDiagram).isNotNull();
		assertThat(bpmnDiagram.getPlane().getBpmnElement()).isEqualTo(collaboration);
	}
	
	@Test
	@DiagramResource
	public void testMultipleParticipants() {
		createParticipant(20, 20, 400, 100, getDiagram(), getDiagramTypeProvider()).execute();

		assertThat(ModelUtil.getAllRootElements(getDefinitions(), Collaboration.class)).hasSize(1);
		
		Collaboration collaboration = ModelUtil.getAllRootElements(getDefinitions(), Collaboration.class).get(0);
		assertThat(collaboration.getParticipants()).hasSize(2);

		List<Process> processes = ModelUtil.getAllRootElements(getDefinitions(), org.eclipse.bpmn2.Process.class);
		assertThat(processes).hasSize(1); // process will be created when the first flow element is added
		
		Shape participantShape = Util.findShapeByBusinessObjectId(getDiagram(), "Participant_1");
		DiagramElement participantDi = DIUtils.findDiagramElement(getDefinitions().getDiagrams(), BusinessObjectUtil.getFirstBaseElement(participantShape));
		assertThat(participantDi).isNotNull();
	}
	
	@Test
	@DiagramResource
	public void testParticipantToNonCollaborationProcess() {
		List<Process> beforeProcesses = ModelUtil.getAllRootElements(getDefinitions(), org.eclipse.bpmn2.Process.class);
		assertThat(beforeProcesses).hasSize(1);
		
		Process process = beforeProcesses.get(0);
		
		createAndCheckSingleParticipant();
		
		List<Process> afterProcesses = ModelUtil.getAllRootElements(getDefinitions(), org.eclipse.bpmn2.Process.class);
		assertThat(afterProcesses).hasSize(1);
		assertThat(afterProcesses.get(0)).isEqualTo(process);
		
		Shape taskShape = Util.findShapeByBusinessObjectId(getDiagram(), "Task_1");
		DiagramElement taskDi = DIUtils.findDiagramElement(getDefinitions().getDiagrams(), BusinessObjectUtil.getFirstBaseElement(taskShape));
		assertThat(taskDi).isNotNull();

		// Shapes should be moved into partcipant shape
		Shape participantShape = getDiagram().getChildren().get(0);
		assertThat(taskShape.getContainer()).isEqualTo((ContainerShape) participantShape);
	}

	// helpers ////////////////////////////////
	
	private BaseElement firstLinkedObject(PictogramElement element) {
		return BusinessObjectUtil.getFirstBaseElement(element);
	}
}
