package demo.jsonPath;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.globalegrow.core.utils.JSONUtils;

public class MainTest {

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		String[] path= {
				"results.0.uid",
				"results.1.uid",
				"results.2.uid",
				"results.3.uid",
				"results.4.uid",
				"results.5.uid",
				"results.6.uid",
				"results.7.uid",
				"results.8.uid",
				"results.9.uid",
				"results.10.uid",
				"results.11.uid"
				};
		
		System.err.println(JSONUtils.getJsonObjectByPath(STR_JSON, path ));
	}
	 private static final String STR_JSON = "{\"message\":\"ok\",\"results\":[{\"uid\":\"fc7675242635ece34e776e7d\",\"detail\":\"1\",\"address\":\"青岛市南区延安一路75号(中山公园西,近一浴)\",\"location\":{\"lng\":\"120.348854\",\"lat\":\"36.067148\"},\"name\":\"黄海饭店\",\"telephone\":\"(0532)82870215\"},{\"uid\":\"78bd50f7d828a34231870a4e\",\"detail\":\"1\",\"address\":\"青岛李沧区京口路86号(利客来购物广场西200米,近君峰路)\",\"location\":{\"lng\":\"120.424502\",\"lat\":\"36.168142\"},\"name\":\"蓝海大饭店(京口路)\",\"telephone\":\"(0532)66089999,(0532)87639088\"},{\"uid\":\"38e48037fa89cc50373a7143\",\"detail\":\"1\",\"address\":\"市南区南海路9号\",\"location\":{\"lng\":\"120.351127\",\"lat\":\"36.062684\"},\"name\":\"汇泉王朝大饭店\",\"telephone\":\"0532-82999888\"},{\"uid\":\"341c35de82aa8ca7e6073875\",\"detail\":\"1\",\"address\":\"山东省青岛市崂山区苗岭路9-2号蓝海大饭店(近国际会展中心)\",\"location\":{\"lng\":\"120.476232\",\"lat\":\"36.109213\"},\"name\":\"蓝海大饭店（青岛崂山店）\",\"telephone\":\"(0532)88996666,(0532)88971888\"},{\"uid\":\"680fb703906f0b858ab988f4\",\"detail\":\"1\",\"address\":\"燕儿岛路49号（近海滨超市）\",\"location\":{\"lng\":\"120.415588\",\"lat\":\"36.07975\"},\"name\":\"东福楼饭店\",\"telephone\":\"0532-85871199\"},{\"uid\":\"b533ea4af8a283584f6d464c\",\"detail\":\"1\",\"address\":\"青岛市市南区香港中路28号\",\"location\":{\"lng\":\"120.39469\",\"lat\":\"36.071349\"},\"name\":\"青岛贵都大饭店\",\"telephone\":\"(0532)86681688\"},{\"uid\":\"3da907f36334f04ab320a87b\",\"detail\":\"1\",\"address\":\"青岛市南区香港中路66号（云霄路口）\",\"location\":{\"lng\":\"120.400478\",\"lat\":\"36.070824\"},\"name\":\"青岛饭店\",\"telephone\":\"(0532)85781888\"},{\"uid\":\"69130523221fb311725e805e\",\"detail\":\"1\",\"address\":\"青岛四方区杭州路30号紫丁香饭店内\",\"location\":{\"lng\":\"120.358714\",\"lat\":\"36.12003\"},\"name\":\"紫丁香饭店\",\"telephone\":\"(0532)89077666\"},{\"uid\":\"f2f15ffbfc486e91631c6759\",\"detail\":\"1\",\"address\":\"青岛经济技术开发区长江西路66号(中国石油大学旁边)\",\"location\":{\"lng\":\"120.184289\",\"lat\":\"35.952825\"},\"name\":\"蓝海金港大饭店\",\"telephone\":\"(0532)86986666\"},{\"uid\":\"fd13a3f2e095e8e3b1077d7c\",\"detail\":\"1\",\"address\":\"市南区汇泉路17号\",\"location\":{\"lng\":\"120.351635\",\"lat\":\"36.054346\"},\"name\":\"东海饭店\",\"telephone\":\"(0532)83887070\"}],\"status\":\"0\"}";  

	
}
