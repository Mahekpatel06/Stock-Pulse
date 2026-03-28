package com.ownProject.GINS.jpa;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ownProject.GINS.wareHouse.WareHouse;

import jakarta.transaction.Transactional;

@Transactional
public interface WareHouseRepository extends JpaRepository<WareHouse, Integer> {


}
