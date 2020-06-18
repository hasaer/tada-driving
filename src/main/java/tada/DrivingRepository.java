package tada;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface DrivingRepository extends PagingAndSortingRepository<Driving, Long>{

    Driving findByDrivingId(Long drivingId);

}