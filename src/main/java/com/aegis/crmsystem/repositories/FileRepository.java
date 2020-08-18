package com.aegis.crmsystem.repositories;

import com.aegis.crmsystem.models.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
