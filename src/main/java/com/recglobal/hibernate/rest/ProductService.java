package com.recglobal.hibernate.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.recglobal.hibernate.model.Category;
import com.recglobal.hibernate.model.Product;
import com.recglobal.hibernate.rest.json.JsonProduct;

@Path("/products")
public class ProductService extends BaseService {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addProduct(JsonProduct jsonProduct) {
		List<Category> categories = new ArrayList<Category>();
		if (jsonProduct.getCategoryIds() != null) {
			categories = getCategories(jsonProduct.getCategoryIds());
		}

		if (categories != null) {
			Product product = new Product(jsonProduct.getName(), jsonProduct.getPrice(),
					categories.toArray(new Category[categories.size()]));

			if (productDao.saveOrUpdate(product)) {
				int status = STATUS_OK;
				String result = "Saved product: " + jsonProduct.toString();
				return Response.status(status).entity(result).build();
			}
		}

		int status = STATUS_ERROR;
		String result = "Can't save product: " + jsonProduct.toString();
		return Response.status(status).entity(result).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<JsonProduct> getProducts(@QueryParam("page") Integer pageNumber) {
		pageNumber = getPageNumber(pageNumber);

		List<JsonProduct> jsonProducts = getJsonProducts(null, pageNumber);

		return jsonProducts;
	}

	@GET
	@Path("/categories/{categoryIds}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<JsonProduct> getProducts(@PathParam("categoryIds") String categoryIds,
			@QueryParam("page") Integer pageNumber) {
		pageNumber = getPageNumber(pageNumber);

		List<Integer> ids = getNumericIds(categoryIds);

		List<JsonProduct> jsonProducts = getJsonProducts(ids, pageNumber);

		return jsonProducts;
	}

	@PUT
	@Path("/{productId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateProduct(@PathParam("productId") Integer productId, JsonProduct jsonProduct) {
		if (jsonProduct.getPrice() != null || jsonProduct.getCategoryIds() != null) {
			Product product = productDao.get(productId);
			if (product != null) {
				if (jsonProduct.getPrice() != null) {
					product.setPrice(jsonProduct.getPrice());
				}

				if (jsonProduct.getCategoryIds() != null) {
					List<Category> categories = getCategories(jsonProduct.getCategoryIds());
					if (categories != null) {
						product.setCategories(categories.toArray(new Category[categories.size()]));
					}
				}

				if (productDao.saveOrUpdate(product)) {
					int status = STATUS_OK;
					String result = "Updated product: " + jsonProduct.toString();
					return Response.status(status).entity(result).build();
				}
			}
		}

		int status = STATUS_ERROR;
		String result = "Can't update product: " + jsonProduct.toString();
		return Response.status(status).entity(result).build();
	}

	/**
	 * return empty list if there was no category<br>
	 * product can exist without categories<br>
	 * return null in case of error (bad category id)
	 */
	private List<Category> getCategories(List<Integer> categoryIds) {
		List<Category> categories = new ArrayList<Category>();

		for (Integer categoryId : categoryIds) {
			Category category = categoryDao.get(categoryId);
			if (category != null) {
				categories.add(category);
			} else {
				categories = null;
				break;
			}
		}

		return categories;
	}

	private List<JsonProduct> getJsonProducts(List<Integer> categoryIds, int pageNumber) {
		List<JsonProduct> jsonProducts = new ArrayList<JsonProduct>();

		List<Product> products = productDao.getAll(categoryIds, pageNumber, PRODUCTS_PER_PAGE, NAME_PRODUCT_PROPERTY);
		for (Product product : products) {
			JsonProduct jsonProduct = new JsonProduct();
			jsonProduct.setId(product.getId());
			jsonProduct.setName(product.getName());
			jsonProduct.setPrice(product.getPrice());
			jsonProduct.setCategoryIds(getCategoryIds(product.getCategories()));

			jsonProducts.add(jsonProduct);
		}

		return jsonProducts;
	}

}
