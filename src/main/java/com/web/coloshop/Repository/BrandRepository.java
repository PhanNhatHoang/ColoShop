package com.web.coloshop.Repository;

import com.web.coloshop.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    @Query("select b from Brand b where b.is_activated = true and b.is_deleted = false")
    List<Brand> findAllByActivated();

    Brand findByName(String name);


}
