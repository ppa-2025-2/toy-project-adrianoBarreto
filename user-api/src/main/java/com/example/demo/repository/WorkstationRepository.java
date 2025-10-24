package com.example.demo.repository;

import java.util.Optional;
import org.springframework.data.repository.ListCrudRepository;
import com.example.demo.repository.entity.Workstation;

public interface WorkstationRepository extends ListCrudRepository<Workstation, Integer> {
    Optional<Workstation> findByUserId(Integer userId);
}