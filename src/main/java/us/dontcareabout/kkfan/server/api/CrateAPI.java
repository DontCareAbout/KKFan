package us.dontcareabout.kkfan.server.api;

import java.util.List;

import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import us.dontcareabout.kkfan.server.repo.CrateRepo;
import us.dontcareabout.kkfan.shared.vo.Crate;

@RestController
@RequestMapping("crate")
public class CrateAPI {
	@Autowired CrateRepo repo;

	@GetMapping("/list")
	public List<Crate> list() {
		return Lists.newArrayList(repo.findAll());
	}
}
