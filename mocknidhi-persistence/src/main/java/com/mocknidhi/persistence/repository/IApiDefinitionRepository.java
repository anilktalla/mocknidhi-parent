package com.mocknidhi.persistence.repository;

import com.mocknidhi.persistence.entity.ApiDefinition;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

/**
 *
 * @author  Anil.Talla
 */
public interface IApiDefinitionRepository extends MongoRepository<ApiDefinition,UUID>{

}
