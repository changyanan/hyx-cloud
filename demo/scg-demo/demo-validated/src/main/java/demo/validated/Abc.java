package demo.validated;

import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;

public class Abc {
	
	@ApiModelProperty("参数ab不可以为空")
	@NotEmpty( message="ab不可以为空")
	private String ab;

	public String getAb() {
		return ab;
	}

	public void setAb(String ab) {
		this.ab = ab;
	}
}
