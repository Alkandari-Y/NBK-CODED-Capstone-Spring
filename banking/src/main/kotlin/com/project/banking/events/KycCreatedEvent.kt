package com.project.banking.events

import com.project.banking.entities.KycEntity
import org.springframework.context.ApplicationEvent

class KycCreatedEvent(source: Any, val kyc: KycEntity) : ApplicationEvent(source)
