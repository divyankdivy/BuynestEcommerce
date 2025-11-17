package com.myecom.myecomapp.repository;

import com.myecom.myecomapp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    public List<Product> findByIsActiveTrue();

    public List<Product> findByCategoryCategoryIdAndIsActiveTrue(Integer categoryId);

}
