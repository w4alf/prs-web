package com.prs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.prs.business.User;
import com.prs.db.UserRepository;





@CrossOrigin
@RestController
@RequestMapping("/users")



public class UserController {

	@Autowired
	private UserRepository userRepo;
	
	//list - Return all Users
		@GetMapping("/")
		public JsonResponse listUsers() {
			JsonResponse jr = null;
			try {
				jr = JsonResponse.getInstance(userRepo.findAll());
			} catch (Exception e) {
				jr = JsonResponse.getInstance(e);
				e.printStackTrace();
				
			}
			
			return jr;
		}
	
		
		//get - return one user for a given id
		@GetMapping("/{id}")
		public JsonResponse get(@PathVariable int id) {
			JsonResponse jr = null;
			try {
			jr = JsonResponse.getInstance(userRepo.findById(id));
			}catch (Exception e) {
				jr = JsonResponse.getInstance(e);
				e.printStackTrace();
				
			}
			return jr;
		}
		
		
		//Login - Login via Username and Password
		@PostMapping("/login")
		public JsonResponse login(@RequestBody User u) {
			JsonResponse jr = null;
			
			try {
				jr = JsonResponse.getInstance(userRepo.findByUserNameAndPassword(u.getUserName(),u.getPassword()));
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
			
		//Add - Adds a new user
		@PostMapping("/")
		public JsonResponse addUser(@RequestBody User u) {
			JsonResponse jr = null;
			
			try {
				jr = JsonResponse.getInstance(userRepo.save(u));
				
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
		
		//Update - update an user
		@PutMapping("/")
		public JsonResponse updateUser(@RequestBody User u) {
			JsonResponse jr = null;
			
			
			try {
				if (userRepo.existsById(u.getId())) {
					jr = JsonResponse.getInstance(userRepo.save(u));
				} else {
					//record doesn't exist
					jr = JsonResponse.getInstance("Error updating user. id: " + u.getId() + " doesn't exist.");
				}
				
				}catch (Exception e) {
					jr = JsonResponse.getInstance(e);
					e.printStackTrace();
					
				}
				return jr;
			
		}
			
		
		//Delete - delete a User
		@DeleteMapping("/{id}")
		public JsonResponse deleteUser(@PathVariable int id) {
			JsonResponse jr = null;
			
			try {
				if (userRepo.existsById(id)) {
					userRepo .deleteById(id);
					
					jr = JsonResponse.getInstance("Delete Successful!");
					
				} else {
					//record doesn't exist
					jr = JsonResponse.getInstance("Error deleting User. id: " + id + " doesn't exist.");
				}
				}
				catch (DataIntegrityViolationException dive) {
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
