package com.xiaozhi.blog.service.other.sina;

import java.util.List;


public class Faces {

	private String name;

	private List<Icon> icons;

	public Faces(String name, List<Icon> icons) {
		super();
		this.name = name;
		this.icons = icons;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}



	public List<Icon> getIcons() {
		return icons;
	}



	public void setIcons(List<Icon> icons) {
		this.icons = icons;
	}


	@Override
	public String toString() {
		return "Faces [name=" + name + ", icons=" + icons + "]";
	}



}
