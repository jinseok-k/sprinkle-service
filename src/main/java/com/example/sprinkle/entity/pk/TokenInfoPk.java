package com.example.sprinkle.entity.pk;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfoPk implements Serializable {
	private String roomId;
	private String token;
}
