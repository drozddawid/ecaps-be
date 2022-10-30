package com.drozd.ecaps.repository;

import com.drozd.ecaps.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}