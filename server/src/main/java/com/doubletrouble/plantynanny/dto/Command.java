package com.doubletrouble.plantynanny.dto;

import com.doubletrouble.plantynanny.enums.CommandType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Command {
    CommandType type;
}