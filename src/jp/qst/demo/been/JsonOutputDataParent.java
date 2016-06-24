package jp.qst.demo.been;

import java.util.List;

public class JsonOutputDataParent {
	private String name=null;
	private List<JsonOutputDataParent> children=null;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<JsonOutputDataParent> getChildren() {
		return children;
	}
	public void setChildren(List<JsonOutputDataParent> children) {
		this.children = children;
	}

}
