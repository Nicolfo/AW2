package aw2.g33.server.attachment


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AttachmentRepository :JpaRepository<Attachment,UUID>