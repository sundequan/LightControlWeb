package light.sundq.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author lenovo
 * @date 2018年6月26日
 */

@Controller
public class IndexController {

	@RequestMapping("/send")
	public String ws() {
		return "ws";
	}
}
