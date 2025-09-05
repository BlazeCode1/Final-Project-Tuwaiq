package org.example.finalprojecttuwaiq.Repository;

import org.example.finalprojecttuwaiq.Model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {
    Document getDocumentsById(Integer id);
}
