package com.drozd.ecaps.repository;

import com.drozd.ecaps.model.attachment.GoogleAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<GoogleAttachment, Long> {
}