package com.mca.infrastructure.event;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Builder
@Data
public class VideoGameEvent {

	private Long stockId;

	private boolean availability;

	private Timestamp timeUpdate;

}
