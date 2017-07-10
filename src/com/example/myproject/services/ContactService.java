package com.example.myproject.services;

import com.example.myproject.pojo.Contact;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultList;

public class ContactService {

	private DatastoreService entityStore;

	public ContactService() {
		entityStore = DatastoreServiceFactory.getDatastoreService();
	}

	// return the entity if the username and password matches or else null
	public Entity loginUser(String userEmail, String userPassword) {
		Entity user = checkUser(userEmail);
		if (user != null) {
			String dbPasswordValue = (String) user.getProperty("password");
			if (userPassword.equals(dbPasswordValue)) {
				// System.out.println("password correct");
				return user;
			} else {
				// System.out.println("password missmatch");
				return null;
			}
		} else {
			// System.out.println("no user found");
			return null;
		}

	}

	// return the entity, new user created(stored in the database) else null
	// (already user exist in that name)
	public Entity signUpUser(String name, String password, String email) {
		Entity user = checkUser(email);
		// System.out.println("The signupuser");
		if (user == null) {// New user register into the database
			// System.out.println("user is going to create");
			/*user = new Entity("Contacts", email);
			user.setProperty("userName", email);
			user.setProperty("password", password);
			user.setProperty("name", name);
			user.setProperty("email", email);
			entityStore.put(user);*/
			return createEntity(email, name, password);
			// System.out.println("new user is create");
			//return user;
		}
		return null;
	}

	// return entity if the user exist or else null (No user found)
	public Entity checkUser(String email) {
		Key k = KeyFactory.createKey("Contacts", email);
		try {
			Entity entityAvailable = entityStore.get(k);
			// User available in the database
			// System.out.println("The user is "+entityAvailable);
			return entityAvailable;
		} catch (EntityNotFoundException excep) {
			// No such user found
			// System.out.println("No such user found ");
			return null;
		}
	}

	// Add Entity (userInformation) in to database if not exist in the database
	// and returns Entity
	public void addUser(Contact userInformation) {
		Entity user = checkUser(userInformation.getEmail());
		if (user == null) {
			// System.out.println("user is going to create");
			/*
			 * user = new Entity("Contacts", userInformation.getEmail());
			 * user.setProperty("userName", userInformation.getEmail());
			 * user.setProperty("name", userInformation.getName());
			 * user.setProperty("email", userInformation.getEmail());
			 * user.setProperty("password", userInformation.getPassword());
			 * entityStore.put(user);
			 */

			createEntity(userInformation.getEmail(), userInformation.getName(), userInformation.getPassword());
			// System.out.println("new user is create");
			// return newEntity;
		}
		// return user;
	}

	public Entity addGoogleUser(String email, String name) {
		Entity user = checkUser(email);
		if (user == null) {
			// Entity newEntity = new Entity("Contacts", email);
			/*
			 * user = new Entity("Contacts", email); user.setProperty("email",
			 * email); user.setProperty("userName", email);
			 * user.setProperty("name", name);
			 * 
			 * entityStore.put(user);
			 */
			return createEntity(email, name, "");
		}
		return user;
		// return user;
	}

	public String deleteUser(String email){
		Entity user = checkUser(email);
		if(user != null)
		{
			Key userKey = KeyFactory.createKey("Contacts", email);
			entityStore.delete(userKey);
			System.out.println("user deleted successfully");
			return "Success";
		}
		return "Failure";
	}
	
	public Entity createEntity(String email, String name, String password) {
		//System.out.println("creating new the name  " + name);
		Entity user = new Entity("Contacts", email);
		user.setProperty("email", email);
		user.setProperty("userName", email);
		user.setProperty("name", name);
		user.setProperty("password", password);

		entityStore.put(user);
		return user;
	}

	public Entity updateUserName(String emailId,String newName){
		Entity user= checkUser(emailId);
		if(user != null)
		{
			user.setProperty("name", newName);
			entityStore.put(user);
		}
		return user;
	}
	
	public QueryResultList<Entity> fetchUserInformationWithLimit(int limit, String startCursor) {

		FetchOptions fetchOptionsLimit = FetchOptions.Builder.withLimit(limit);
		if (startCursor != null) {
			fetchOptionsLimit.startCursor(Cursor.fromWebSafeString(startCursor));
		}
		Query query = new Query("Contacts");
		PreparedQuery preparedQuery = entityStore.prepare(query);

		return preparedQuery.asQueryResultList(fetchOptionsLimit);

	}
	
	/*public Entity fetchUserInformation(String email) {
		Key contactKey = KeyFactory.createKey("Contacts", email);
		Filter keyFilter =
				new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, contactKey);
		Query query = new Query("Contacts").setFilter(keyFilter);
		PreparedQuery preparedQuery = entityStore.prepare(query);

		return preparedQuery.asSingleEntity();
	}*/
}
