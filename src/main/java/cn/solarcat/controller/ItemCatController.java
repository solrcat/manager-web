package cn.solarcat.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.solarcat.common.pojo.EasyUITreeNode;
import cn.solarcat.service.ItemCatService;

@Controller
public class ItemCatController {
	@Reference(timeout = 3000)
	private ItemCatService itemCatService;

	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EasyUITreeNode> getItemCatList(@RequestParam(name = "id", defaultValue = "0") long parentId) {
		List<EasyUITreeNode> list = itemCatService.getItemCatList(parentId);
		return list;

	}
}
