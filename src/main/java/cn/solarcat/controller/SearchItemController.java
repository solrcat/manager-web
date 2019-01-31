package cn.solarcat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.solarcat.common.util.SolarCatResult;
import cn.solarcat.search.service.SearchItemService;

@Controller
public class SearchItemController {
	@Reference(timeout = 600000)
	private SearchItemService searchItemService;

	@RequestMapping("/index/item/import")
	@ResponseBody
	public SolarCatResult importItemList() {
		SolarCatResult result = searchItemService.importAllItems();
		return result;
	}
}
