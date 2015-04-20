package com.recglobal.hibernate.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.recglobal.hibernate.model.Category;
import com.recglobal.hibernate.rest.json.JsonCategory;

@Path("/categories")
public class CategoryService extends BaseService {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addCategory(JsonCategory jsonCategory) {
		Category category = new Category(jsonCategory.getName());

		if (categoryDao.saveOrUpdate(category)) {
			int status = STATUS_OK;
			String result = "Saved category: " + jsonCategory.toString();
			return Response.status(status).entity(result).build();
		}

		int status = STATUS_ERROR;
		String result = "Can't save category: " + jsonCategory.toString();
		return Response.status(status).entity(result).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<JsonCategory> getCategories(@QueryParam("page") Integer pageNumber) {
		pageNumber = getPageNumber(pageNumber);

		List<JsonCategory> jsonCategories = new ArrayList<JsonCategory>();

		List<Category> categories = categoryDao.getAll(pageNumber, CATEGORIES_PER_PAGE, NAME_CATEGORY_PROPERTY);
		for (Category category : categories) {
			JsonCategory jsonCategory = new JsonCategory();
			jsonCategory.setId(category.getId());
			jsonCategory.setName(category.getName());

			jsonCategories.add(jsonCategory);
		}

		return jsonCategories;
	}

}
