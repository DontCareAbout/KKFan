package us.dontcareabout.kkfan.server.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import us.dontcareabout.kkfan.shared.vo.Location;

@Repository
@RepositoryRestResource(path = "location")
public interface LocationRepo extends JpaRepository<Location, Long> {

}
