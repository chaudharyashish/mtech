package com.mtech.image.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api")

public class OLD_SSOController {

/*    @Autowired
    OLD_NoteRepository noteRepository;
	
    @GetMapping("/notes")
    public List<OLD_Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @GetMapping("/notes/{id}")
    public ResponseEntity<OLD_Note> getNoteById(@PathVariable(value = "id") Long noteId) {
        OLD_Note note = noteRepository.findOne(noteId);
        if(note == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(note);
    }

    @PostMapping("/notes")
    public OLD_Note createNote(@Valid @RequestBody OLD_Note note) {
        return noteRepository.save(note);
    }

    @PutMapping("/notes/{id}")
    public ResponseEntity<OLD_Note> updateNote(@PathVariable(value = "id") Long noteId,
                                           @Valid @RequestBody OLD_Note noteDetails) {
        OLD_Note note = noteRepository.findOne(noteId);
        if(note == null) {
            return ResponseEntity.notFound().build();
        }
        note.setTitle(noteDetails.getTitle());
        note.setContent(noteDetails.getContent());

        OLD_Note updatedNote = noteRepository.save(note);
        return ResponseEntity.ok(updatedNote);
    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<OLD_Note> deleteNote(@PathVariable(value = "id") Long noteId) {
        OLD_Note note = noteRepository.findOne(noteId);
        if(note == null) {
            return ResponseEntity.notFound().build();
        }

        noteRepository.delete(note);
        return ResponseEntity.ok().build();
    }*/
}
