package com.egs.core.bank.repository;

import com.egs.core.bank.model.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankRepository extends JpaRepository<Bank, UUID> {

}
