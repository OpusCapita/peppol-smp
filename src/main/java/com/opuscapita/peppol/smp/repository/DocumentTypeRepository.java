package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.entity.DocumentType;
import com.opuscapita.peppol.smp.entity.Smp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long>, JpaSpecificationExecutor<DocumentType> {

    List<DocumentType> findByDocumentTypeId(Integer documentTypeId);

    List<DocumentType> findByName(String name);

    List<DocumentType> findBySmp(Smp smp);

}
