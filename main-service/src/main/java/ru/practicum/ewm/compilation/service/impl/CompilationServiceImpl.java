package ru.practicum.ewm.compilation.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.compilation.service.CompilationService;
import ru.practicum.ewm.error.ApiError;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;

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
    public ResponseEntity<Object> getAllCompilationsPublic(Boolean pinned, int from, int size) {

        if (pinned != null) {

            List<Compilation> compilations;

            if (pinned) {

                compilations = compilationRepository
                        .getAllByPinnedTrue(PageRequest.of(from, size));

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(CompilationMapper
                                .toCompilationWithShortEventDto(compilations)
                        );

            } else {

                compilations = compilationRepository
                        .getAllByPinnedFalse(PageRequest.of(from, size));

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(CompilationMapper
                                .toCompilationWithShortEventDto(compilations)
                        );
            }

        } else {

            Page<Compilation> compilations = compilationRepository
                    .findAll(PageRequest.of(from, size));

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(CompilationMapper
                            .toCompilationWithShortEventDto(compilations.getContent())
                    );

        }

    }

    @Override
    public ResponseEntity<Object> getCompilationByIdPublic(long compId) {

        Optional<Compilation> compilation = compilationRepository.findById(compId);
        if (compilation.isPresent() && compilation.get().getClass().equals(Compilation.class)) {

            Optional<List<Event>> events =
                    compilationRepository.getAllEventEdsByCompilation(compilation.get());

//            if (events.isPresent() && !events.get().isEmpty()) {
//                compilation.get().setEvents(events.get());
//            }

//            List<EventShortDto> eventShortDtos =
//                    CompilationMapper.toEventShortDtoList(compilationRepository.getAllEventEdsByCategory(compilation.get()));

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(CompilationMapper
                            .toCompilationWithShortEventDto(compilation.get())
                    );

        } else {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(
                            "404",
                            "Not Found.",
                            "Compilation doesn't exist."
                    ));
        }

    }

    @Override
    public ResponseEntity<Object> saveCompilationAdmin(NewCompilationDto newCompilationDto) {

        Compilation compilation;

        if (newCompilationDto.getEvents() == null) {

            compilation = new Compilation(
                    List.of(),
                    newCompilationDto.getPinned(),
                    newCompilationDto.getTitle()
            );

//            for (Event e : events.get()) {
//                e.setCompilation(compilation);
//            }

            if (compilation.getPinned() == null) {
                compilation.setPinned(false);
            }

            //eventRepository.saveAll(events.get());
            compilationRepository.save(compilation);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(CompilationMapper
                            .toCompilationWithShortEventDto(compilation)
                    );

        } else {

            Optional<List<Event>> events = eventRepository.findAllByIdIn(newCompilationDto.getEvents());
            if (events.isPresent() && events.get().size() == newCompilationDto.getEvents().size()) {

                compilation = new Compilation(
                        events.get(),
                        newCompilationDto.getPinned(),
                        newCompilationDto.getTitle()
                );

//            for (Event e : events.get()) {
//                e.setCompilation(compilation);
//            }
                if (compilation.getPinned() == null) {
                    compilation.setPinned(false);
                }

                //eventRepository.saveAll(events.get());
                compilationRepository.save(compilation);

                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(CompilationMapper
                                .toCompilationWithShortEventDto(compilation)
                        );

            } else {

                return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(
                            "404",
                            "Not Found.",
                            "Event doesn't exist."
                    ));

            }

        }
    }

    @Override
    public ResponseEntity<Object> patchCompilationAdmin(NewCompilationDto newCompilationDto, long compId) {

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

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(CompilationMapper.toCompilationWithShortEventDto(
                            compilationRepository.save(
                                    compilation.get()
                            )
                    ));

        } else {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(
                            "404",
                            "Not Found.",
                            "Compilation doesn't exist."
                    ));
        }
//        Optional<Compilation> compilation = compilationRepository.findById(compId);
//        if (compilation.isPresent() && compilation.get().getClass().equals(Compilation.class)) {
//
//            Optional<Set<Event>> events = eventRepository.findAllByIdIn(newCompilationDto.getEvents());
//
//            compilation.get().setEvents(events.get());
//            compilation.get().setPinned(newCompilationDto.getPinned());
//            compilation.get().setTitle(newCompilationDto.getTitle());
//
//            return ResponseEntity
//                    .status(HttpStatus.OK)
//                    .body(CompilationMapper
//                            .toCompilationWithShortEventDto(
//                                    compilationRepository.save(compilation.get())
//                            ));
//        } else {
//
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ApiError(
//                            "404",
//                            "Not Found.",
//                            "Compilation doesn't exist."
//                    ));
//        }

    }

    @Override
    public ResponseEntity<Object> deleteCompilationAdmin(long compId) {

        Optional<Compilation> compilation = compilationRepository.findById(compId);
        if (compilation.isPresent() && compilation.get().getClass().equals(Compilation.class)) {

            compilationRepository.delete(compilation.get());

            return ResponseEntity
                    .noContent().build();

        } else {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(
                            "404",
                            "Not Found.",
                            "Compilation doesn't exist."
                    ));
        }
    }

}
