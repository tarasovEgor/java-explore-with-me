package ru.practicum.ewm.compilation.mapper;

import ru.practicum.ewm.compilation.dto.CompilationWithShortEventDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;

import java.util.ArrayList;
import java.util.List;

public class CompilationMapper {

    public static List<EventShortDto> toEventShortDtoList(List<Event> events) {

        List<EventShortDto> shortEventDtos = new ArrayList<>();

        for (Event e : events) {
            shortEventDtos.add(EventMapper.toEventShortDto(e));
        }

        return shortEventDtos;
    }

    public static CompilationWithShortEventDto toCompilationWithShortEventDto(Compilation compilation) {

        List<EventShortDto> eventShortDtos = toEventShortDtoList(compilation.getEvents());


        return new CompilationWithShortEventDto(
                compilation.getId(),
                eventShortDtos,
                compilation.getPinned(),
                compilation.getTitle()
        );

    }

    public static List<CompilationWithShortEventDto> toCompilationWithShortEventDto(List<Compilation> compilations) {

        List<CompilationWithShortEventDto> dtos = new ArrayList<>();

        for (Compilation c : compilations) {
            dtos.add(toCompilationWithShortEventDto(c));
        }

        return dtos;
    }
}
