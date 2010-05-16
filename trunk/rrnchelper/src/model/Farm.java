package model;

public class Farm {
	private String farmAddress;
	private Product[] products;

	public Farm() {
		farmAddress = "http://mapps.renren.com/rr_farm/farm/action/wap,indexAction.php?r=_9bd61b733642&sid=3c2ca7542a1c4351c6e03a23970027076";
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
