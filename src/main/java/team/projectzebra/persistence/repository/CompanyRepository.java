package team.projectzebra.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import team.projectzebra.persistence.entity.Company;

public interface CompanyRepository extends  CrudRepository<Company,Integer> {
}
