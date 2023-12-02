package com.example.demo.metadata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface metadataRepository extends JpaRepository<metadata, String> {

    @Query(value="select d.Attribute_Name , d.IsMasked from Attribute_data d",nativeQuery = true)
    List<Object[]> findNameAndIsMasked();

    @Query(value="select count(*) as count from Attribute_data d where isMasked = 1 ",nativeQuery = true)
    List<Integer> countNameAndIsMaskedYes();

    @Query(value="select d.Attribute_Name from Attribute_data d where isMasked = 1 ",nativeQuery = true)
    List<String> findNameAndIsMaskedYes();

    @Query(value="select count(*) as count from Attribute_data d where isMasked = 0 ",nativeQuery = true)
    List<Integer> countNameAndIsMaskedNo();

    @Query(value="select d.Attribute_Name from Attribute_data d where isMasked = 0 ",nativeQuery = true)
    List<String> findNameAndIsMaskedNo();


}
