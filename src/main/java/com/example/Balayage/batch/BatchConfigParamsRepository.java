package com.example.Balayage.batch;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchConfigParamsRepository extends JpaRepository<BatchConfigParams, Long> {

}
