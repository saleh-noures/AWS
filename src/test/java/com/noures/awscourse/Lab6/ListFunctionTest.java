package com.noures.awscourse.Lab6;

import java.util.List;

import com.noures.awscourse.Lab6.functions.ListFunction;
import com.noures.awscourse.Lab6.pojo.Note;
import com.noures.awscourse.Lab6.solution.CreateUpdateFunctionSolution;
import org.junit.Assert;
import org.junit.Test;


import com.amazonaws.services.lambda.runtime.Context;

public class ListFunctionTest {

	// Input data for the List function
	private Note createListInput() {
		Note note = new Note();
		note.setUserId("testuser");
		return note;
	}
	
	// Input data for the CreateUpdate function
	private Note createCreateInput() {
		Note note = new Note();
		note.setUserId("testuser");
		note.setNoteId("001");
		note.setNote("This is a test");
		return note;
	}

	@Test
	public void testListFunctionTest() {
		// Create a Lambda fake context
		Context ctx = new TestContext();
		
		// Create the note in DynamoDB first
		CreateUpdateFunctionSolution createHandler = new CreateUpdateFunctionSolution();
		createHandler.handleRequest(createCreateInput(), ctx);
		
		// Execute the ListFunction
		ListFunction listHandler = new ListFunction();
		List<Note> output = listHandler.handleRequest(createListInput(), ctx);
		
		// Test to see if the output is empty
		Assert.assertFalse(output.isEmpty());
	}
}
