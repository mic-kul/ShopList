package pl.trou.shoplist.model;

public class Product {

	private Long id;

	private String name;

	private boolean selected;

	public Product(Long id, String name) {
		this.name = name;
		selected = false;
	}

	public Product(Long id, String name, String status) {
		this.id = id;
		this.name = name;
		this.setSelected(status);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void setSelected(String selected) {
		this.selected = selected.equals("0") ? false : true;
	}

}
