package com.doubletrouble.plantynanny.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Command {
    CommandType type;
}