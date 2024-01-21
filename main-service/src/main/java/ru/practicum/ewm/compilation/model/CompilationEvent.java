//package ru.practicum.ewm.compilation.model;
//
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.DynamicInsert;
//import org.hibernate.annotations.DynamicUpdate;
//import ru.practicum.ewm.event.model.Event;
//
//import javax.persistence.*;
//
//@Data
//@Entity
//@Table(
//        name = "compilation_event",
//        schema = "public"
//)
//@DynamicUpdate
//@DynamicInsert
//@NoArgsConstructor
//public class CompilationEvent {
//
//    @Id
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "compilation_id")
//    Compilation compilation;
//
//    @ManyToOne
//    @JoinColumn(name = "event_id")
//    Event event;
//
//}
