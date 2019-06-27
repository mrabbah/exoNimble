package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MyData;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MyData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MyDataRepository extends JpaRepository<MyData, Long> {

}
