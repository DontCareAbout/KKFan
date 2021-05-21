package us.dontcareabout.kkfan.server.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import us.dontcareabout.kkfan.shared.vo.Crate;

@Repository
@RepositoryRestResource(path = "crate")
public interface CrateRepo extends JpaRepository<Crate, Long> {

}
