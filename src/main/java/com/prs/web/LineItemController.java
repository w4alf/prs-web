package com.prs.web;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.prs.web.JsonResponse;
import com.prs.business.LineItem;
import com.prs.business.Request;
import com.prs.db.LineItemRepository;
import com.prs.db.RequestRepository;

import org.springframework.dao.DataIntegrityViolationException;



@CrossOrigin
@RestController
@RequestMapping("/line-items")


public class LineItemController {

	@Autowired
	private LineItemRepository lineItemRepo;
	
	@Autowired
	private RequestRepository requestRepo;
	
	
	//list - Return all Line Items
		@GetMapping("/")
		public JsonResponse listLineItems() {
			JsonResponse jr = null;
			try {
				jr = JsonResponse.getInstance(lineItemRepo.findAll());
			} catch (Exception e) {
				jr = JsonResponse.getInstance(e);
				e.printStackTrace();
			}
			
			return jr;
		}
	
		
		//get - return one line item for a given id
		@GetMapping("/{id}")
		public JsonResponse get(@PathVariable int id) {
			JsonResponse jr = null;
			try {
			jr = JsonResponse.getInstance(lineItemRepo.findById(id));
			}catch (Exception e) {
				jr = JsonResponse.getInstance(e);
				e.printStackTrace();
			}
			return jr;
		}
		
		
		
		//get all line items where request id =
		@GetMapping("/lines-for-pr/{id}")
		public JsonResponse getLineItemById(@PathVariable int id) {
			JsonResponse jr = null;
			try {
				jr = JsonResponse.getInstance(lineItemRepo.findByRequestId(id)); 
			} catch (Exception e) {
				jr = JsonResponse.getInstance(e);
				e.printStackTrace();
			}
			
			return jr;
		}
			
		//Add - Adds a new Line Item
		@PostMapping("/")
		public JsonResponse addLineItem(@RequestBody LineItem l) {
			JsonResponse jr = null;
			
			try {
				
				jr = JsonResponse.getInstance(lineItemRepo.save(l));
				
								
				recalcTotal(l.getRequest());
				
				}catch (DataIntegrityViolationException dive) {
					jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
					dive.printStackTrace();
				}
				catch (Exception e) {
					jr = JsonResponse.getInstance(e);
					e.printStackTrace();
				}
				return jr;
			
		}
		
		//Update - update a Line Item
		@PutMapping("/")
		public JsonResponse updateRequest(@RequestBody LineItem l) {
			JsonResponse jr = null;
			
			
			try {
				if (lineItemRepo.existsById(l.getId())) {
				
				
					jr = JsonResponse.getInstance(lineItemRepo.save(l));
					
								
					recalcTotal(l.getRequest());
					
					
				} else {
					//record doesn't exist
					jr = JsonResponse.getInstance("Error updating Line Item. id: " + l.getId() + " doesn't exist.");
				}
				
				}catch (DataIntegrityViolationException dive) {
					jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
					dive.printStackTrace();
				}
				catch (Exception e) {
					jr = JsonResponse.getInstance(e);
					e.printStackTrace();
				}
				return jr;
			
		}
			
		



		//Delete - delete a Line Item
		@DeleteMapping("/{id}")
		public JsonResponse deleteLineItem(@PathVariable int id) {
			JsonResponse jr = null;
			
			try {
				if (lineItemRepo.existsById(id)) {
					
					Request r = lineItemRepo.findById(id).get().getRequest();
					
					lineItemRepo.deleteById(id);
									
					recalcTotal(r);
			
					jr = JsonResponse.getInstance("Delete Successful!");
					
				} else {
					//record doesn't exist
					jr = JsonResponse.getInstance("Error deleting Line Item. id: " + id + " doesn't exist.");
				}
				
				}catch (Exception e) {
					jr = JsonResponse.getInstance(e);
					e.printStackTrace();
				}
			
				return jr;
		}
		

	
		
	
		void recalcTotal(Request request) {
			
			 double lineItemtotal = 0;
			
	        List<LineItem> lineItems = lineItemRepo.findByRequestId(request.getId());
	       
	        
	        for (LineItem line: lineItems) {
	        	lineItemtotal += line.getLineTotal();
	        }
	        

	        request.setTotal(lineItemtotal);
	        
	        try {
	            requestRepo.save(request);
	            
	        } catch (Exception e) {
	        	
	           throw e;
	        }
	    }
		
	
		
}
