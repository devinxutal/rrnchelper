package rrnchelper.model;

public class Farm {
	private String farmAddress;
	private Product[] products;

	public Farm(String farmAddress) {
		this.farmAddress = farmAddress;
		products = new Product[4];
		products[0] = new Product(Product.Å©Ìï);
		products[1] = new Product(Product.¹ûÊ÷);
		products[2] = new Product(Product.ÐóÄÁ);
		products[3] = new Product(Product.»úÐµ);
	}

	public Product[] getProducts() {
		return products;
	}

	public String getFarmAddress() {
		return farmAddress;
	}
}
