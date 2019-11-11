package com.prs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;


import com.prs.business.Vendor;
import com.prs.db.VendorRepository;



@CrossOrigin
@RestController
@RequestMapping("/vendors")



public class VendorController {

	
	@Autowired
	private VendorRepository vendorRepo;
	
	//list - Return all Vendors
		@GetMapping("/")
		public JsonResponse listVendors() {
			JsonResponse jr = null;
			try {
				jr = JsonResponse.getInstance(vendorRepo.findAll());
			} catch (Exception e) {
				jr = JsonResponse.getInstance(e);
				e.printStackTrace();
				
			}
			
			return jr;
		}
	
		
		//get - return one vendor for a given id
		@GetMapping("/{id}")
		public JsonResponse get(@PathVariable int id) {
			JsonResponse jr = null;
			try {
			jr = JsonResponse.getInstance(vendorRepo.findById(id));
			}catch (Exception e) {
				jr = JsonResponse.getInstance(e);
				e.printStackTrace();
				
			}
			return jr;
		}
			
		//Add - Adds a new vendor
		@PostMapping("/")
		public JsonResponse addVendor(@RequestBody Vendor v) {
			JsonResponse jr = null;
			
			try {
				jr = JsonResponse.getInstance(vendorRepo.save(v));
				
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
		
		//Update - update a vendor
		@PutMapping("/")
		public JsonResponse updateVendor(@RequestBody Vendor v) {
			JsonResponse jr = null;
			
			
			try {
				if (vendorRepo.existsById(v.getId())) {
					jr = JsonResponse.getInstance(vendorRepo.save(v));
				} else {
					//record doesn't exist
					jr = JsonResponse.getInstance("Error updating vendor. id: " + v.getId() + " doesn't exist.");
				}
				
				}catch (Exception e) {
					jr = JsonResponse.getInstance(e);
					e.printStackTrace();
					
				}
				return jr;
			
		}
			
		
		//Delete - delete a vendor
		@DeleteMapping("/{id}")
		public JsonResponse deleteVendor(@PathVariable int id) {
			JsonResponse jr = null;
			
			try {
				if (vendorRepo.existsById(id)) {
					vendorRepo .deleteById(id);
					
					jr = JsonResponse.getInstance("Delete Successful!");
					
				} else {
					//record doesn't exist
					jr = JsonResponse.getInstance("Error deleting Vendor. id: " + id + " doesn't exist.");
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
