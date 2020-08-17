package com.example.sprinkle.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class SprinkleHeader {

	//사용자 아이디
	private long userId;

	//대화방 아이디
	private String roomId;
}
