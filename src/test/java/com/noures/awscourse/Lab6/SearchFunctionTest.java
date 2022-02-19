package com.noures.awscourse.Lab6;

import java.util.List;

import com.noures.awscourse.Lab6.functions.SearchFunction;
import com.noures.awscourse.Lab6.pojo.Note;
import com.noures.awscourse.Lab6.solution.CreateUpdateFunctionSolution;
import org.junit.Assert;
import org.junit.Test;


import com.amazonaws.services.lambda.runtime.Context;

public class SearchFunctionTest {
	
	// Input data for the Search function
	private Note createSearchInput() {
		Note note = new Note();
		note.setUserId("testuser");
		note.setNote("test");
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
	public void testSearchFunctionTest() {
		// Create a Lambda fake context
		Context ctx = new TestContext();
		
		// Create the note in DynamoDB first
		CreateUpdateFunctionSolution createHandler = new CreateUpdateFunctionSolution();
		createHandler.handleRequest(createCreateInput(), ctx);
		
		// Execute the SearchFunction
		SearchFunction searchHandler = new SearchFunction();
		List<Note> output = searchHandler.handleRequest(createSearchInput(), ctx);
		
		// Test to see if the output is empty
		Assert.assertFalse(output.isEmpty());
	}
}
