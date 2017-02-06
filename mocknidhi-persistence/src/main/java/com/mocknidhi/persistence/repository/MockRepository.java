package com.mocknidhi.persistence.repository;

import com.mocknidhi.persistence.entity.Mock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Created by anilkumartalla on 2/5/17.
 */
@Repository
public interface MockRepository extends MongoRepository<Mock,UUID>{

}
