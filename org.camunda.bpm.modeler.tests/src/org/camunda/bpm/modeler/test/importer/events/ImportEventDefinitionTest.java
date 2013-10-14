package org.camunda.bpm.modeler.test.importer.events;

import java.util.HashSet;
import java.util.Set;

import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.test.importer.AbstractImportBpmnModelTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.eclipse.bpmn2.EscalationEventDefinition;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.SignalEventDefinition;
import org.eclipse.bpmn2.TerminateEventDefinition;
import org.eclipse.bpmn2.TimerEventDefinition;
import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Assert;
import org.junit.Test;


public class ImportEventDefinitionTest extends AbstractImportBpmnModelTest {
	
	@Test
	@DiagramResource
	public void testImportEventDefinitions() {
		// when
		importDiagram();
		
		// then
		Set<EventDefinition> eventDefinitions = new HashSet<EventDefinition>();
		EList<Shape> children = diagram.getChildren();
		for (Shape child : children) {
		  PictogramLink link = child.getLink();
		  if (link != null && link.getBusinessObjects().size() > 0 && link.getBusinessObjects().get(0) instanceof Event) {
		    eventDefinitions.addAll(ModelUtil.getEventDefinitions((Event) link.getBusinessObjects().get(0)));
		  }
		}
		
		boolean timerDef = false;
		boolean signalDef = false;
		boolean messageDef = false;
		boolean terminateDef = false;
		boolean esacalationDef = false;
		
		for (EventDefinition eventDef : eventDefinitions) {
		  if (eventDef instanceof TimerEventDefinition) timerDef = true;
		  if (eventDef instanceof SignalEventDefinition) signalDef = true;
		  if (eventDef instanceof MessageEventDefinition) messageDef = true;
		  if (eventDef instanceof TerminateEventDefinition) terminateDef = true;
		  if (eventDef instanceof EscalationEventDefinition) esacalationDef = true;
		}
		
		Assert.assertEquals(5, eventDefinitions.size());
		Assert.assertTrue(timerDef && signalDef && messageDef && terminateDef && esacalationDef);
	}

}
