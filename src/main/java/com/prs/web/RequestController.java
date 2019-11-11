package com.prs.web;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.prs.web.JsonResponse;
import com.prs.business.Request;
import com.prs.business.User;
import com.prs.db.RequestRepository;
import com.prs.db.UserRepository;

import org.springframework.dao.DataIntegrityViolationException;



@CrossOrigin
@RestController
@RequestMapping("/requests")

public class RequestController {

	

	
	@Autowired
	private RequestRepository requestRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	//list - Return all Requests
		@GetMapping("/")
		public JsonResponse listRequests() {
			JsonResponse jr = null;
			try {
				jr = JsonResponse.getInstance(requestRepo.findAll());
			} catch (Exception e) {
				jr = JsonResponse.getInstance(e);
				e.printStackTrace();
			}
			
			return jr;
		}
	
		
		//get - return one Request for a given id
		@GetMapping("/{id}")
		public JsonResponse get(@PathVariable int id) {
			JsonResponse jr = null;
			try {
			jr = JsonResponse.getInstance(requestRepo.findById(id));
			}catch (Exception e) {
				jr = JsonResponse.getInstance(e);
				e.printStackTrace();
			}
			return jr;
		}
		
		
		//get - return one list of Requests that are in Review status and are not for the current userID
		@GetMapping("/list-review/{id}")
		public JsonResponse getListReview(@PathVariable int id) {
			JsonResponse jr = null;
			try {
		
				 User u	 = userRepo.findById(id).get();	
				
			jr = JsonResponse.getInstance(requestRepo.findByStatusEqualsAndUserNot("Review",u));
			}catch (Exception e) {
				jr = JsonResponse.getInstance(e);
				e.printStackTrace();
			}
			return jr;
		}
		
		
		//Add - Adds a new Request
		@PostMapping("/")
		public JsonResponse addRequest(@RequestBody Request r) {
			JsonResponse jr = null;
			
			try {
				//added 
				r.setSubmittedDate(LocalDateTime.now());
				r.setStatus("New");
				jr = JsonResponse.getInstance(requestRepo.save(r));
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
		
		//Update - update a Request
		@PutMapping("/")
		public JsonResponse updateRequest(@RequestBody Request r) {
			JsonResponse jr = null;
			
			
			try {
				if (requestRepo.existsById(r.getId())) {
					jr = JsonResponse.getInstance(requestRepo.save(r));
				} else {
					//record doesn't exist
					jr = JsonResponse.getInstance("Error updating Request. id: " + r.getId() + " doesn't exist.");
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
		
		
		
		@PutMapping("/approve")
		public JsonResponse approveRequest(@RequestBody Request r) {
			JsonResponse jr = null;
			
			//just call existing update method
			r.setStatus("Approved");
			jr = updateRequest(r);
			return jr;
			
		}
		
		@PutMapping("/reject")
		public JsonResponse rejectRequest(@RequestBody Request r) {
			JsonResponse jr = null;
			
			//just call existing update method
			r.setStatus("Rejected");
			jr = updateRequest(r);
			return jr;
			
		}
		
		
		//Delete - delete a Request
		@DeleteMapping("/{id}")
		public JsonResponse deleteRequest(@PathVariable int id) {
			JsonResponse jr = null;
			
			try {
				if (requestRepo.existsById(id)) {
					requestRepo.deleteById(id);
					
					jr = JsonResponse.getInstance("Delete Successful!");
					
				} else {
					//record doesn't exist
					jr = JsonResponse.getInstance("Error deleting Request. id: " + id + " doesn't exist.");
				}
				
				}catch (Exception e) {
					jr = JsonResponse.getInstance(e);
					e.printStackTrace();
				}
			
				return jr;
		}
		
		//Update - update a Request
		@PutMapping("/submit-review")
		public JsonResponse submitForReview(@RequestBody Request r) {
			JsonResponse jr = null;
			
			
			try {
				if (requestRepo.existsById(r.getId())) {
					
										
					requestRepo.findById(r.getId());
					
					r.setSubmittedDate(LocalDateTime.now());
					if (r.getTotal()<=50.0) {
						r.setStatus("Approved");
					} else {
						r.setStatus("Review");
					}
					
					
					jr = JsonResponse.getInstance(requestRepo.save(r));
				} else {
					//record doesn't exist
					jr = JsonResponse.getInstance("Error updating the Request for id: " + r.getId() + " doesn't exist.");
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

	
	
	
	
}
