package com.saasant.firstSpringProject.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.saasant.firstSpringProject.entity.Customers;

@Repository
public interface CustomerRepository extends JpaRepository<Customers, String> {
}
