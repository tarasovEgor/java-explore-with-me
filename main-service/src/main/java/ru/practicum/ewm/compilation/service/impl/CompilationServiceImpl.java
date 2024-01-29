package ru.practicum.ewm.compilation.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import ru.practicum.ewm.compilation.dto.CompilationWithShortEventDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.compilation.service.CompilationService;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.compilation.CompilationDoesNotExistException;
import ru.practicum.ewm.exception.event.EventDoesNotExistException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CompilationServiceImpl(CompilationRepository compilationRepository,
                                  EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<CompilationWithShortEventDto> getAllCompilationsPublic(Boolean pinned, int from, int size) {
        if (pinned != null) {
            List<Compilation> compilations;
            if (pinned) {
                compilations = compilationRepository
                        .getAllByPinnedTrue(PageRequest.of(from, size));

                return CompilationMapper
                        .toCompilationWithShortEventDto(compilations);
            } else {
                compilations = compilationRepository
                        .getAllByPinnedFalse(PageRequest.of(from, size));

                return CompilationMapper
                        .toCompilationWithShortEventDto(compilations);
            }
        } else {
            Page<Compilation> compilations = compilationRepository
                    .findAll(PageRequest.of(from, size));

            return CompilationMapper
                    .toCompilationWithShortEventDto(compilations.getContent());
        }
    }

    @Override
    public CompilationWithShortEventDto getCompilationByIdPublic(long compId) {
        Optional<Compilation> compilation = compilationRepository.findById(compId);
        if (compilation.isPresent() && compilation.get().getClass().equals(Compilation.class)) {
            return CompilationMapper
                    .toCompilationWithShortEventDto(compilation.get());
        } else {
            throw new CompilationDoesNotExistException("Compilation doesn't exist.");
        }
    }

    @Override
    public CompilationWithShortEventDto saveCompilationAdmin(NewCompilationDto newCompilationDto) {
        Compilation compilation;
        if (newCompilationDto.getEvents() == null) {
            compilation = new Compilation(
                    List.of(),
                    newCompilationDto.getPinned(),
                    newCompilationDto.getTitle()
            );
            if (compilation.getPinned() == null) {
                compilation.setPinned(false);
            }
            compilationRepository.save(compilation);

            return CompilationMapper
                    .toCompilationWithShortEventDto(compilation);
        } else {
            Optional<List<Event>> events = eventRepository.findAllByIdIn(newCompilationDto.getEvents());
            if (events.isPresent() && events.get().size() == newCompilationDto.getEvents().size()) {
                compilation = new Compilation(
                        events.get(),
                        newCompilationDto.getPinned(),
                        newCompilationDto.getTitle()
                );
                if (compilation.getPinned() == null) {
                    compilation.setPinned(false);
                }
                compilationRepository.save(compilation);

                return CompilationMapper
                        .toCompilationWithShortEventDto(compilation);
            } else {
                throw new EventDoesNotExistException("Event doesn't exist.");
            }
        }
    }

    @Override
    public CompilationWithShortEventDto patchCompilationAdmin(UpdateCompilationDto newCompilationDto, long compId) {
        Optional<Compilation> compilation = compilationRepository.findById(compId);
        if (compilation.isPresent() && compilation.get().getClass().equals(Compilation.class)) {
            if (newCompilationDto.getEvents() != null) {
                Optional<List<Event>> events =
                        eventRepository.findAllByIdIn(newCompilationDto.getEvents());
                compilation.get().setEvents(events.get());
            }
            if (newCompilationDto.getPinned() != null) {
                compilation.get().setPinned(newCompilationDto.getPinned());
            }
            if (newCompilationDto.getTitle() != null) {
                compilation.get().setTitle(newCompilationDto.getTitle());
            }
            return CompilationMapper
                    .toCompilationWithShortEventDto(
                            compilationRepository.save(compilation.get())
                    );
        } else {
            throw new CompilationDoesNotExistException("Compilation doesn't exist.");
        }
    }

    @Override
    public void deleteCompilationAdmin(long compId) {
        Optional<Compilation> compilation = compilationRepository.findById(compId);
        if (compilation.isPresent() && compilation.get().getClass().equals(Compilation.class)) {
            compilationRepository.delete(compilation.get());
        } else {
            throw new CompilationDoesNotExistException("Compilation doesn't exist.");
        }
    }

}
